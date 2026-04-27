package com.arnor4eck.medicinenotes.service;

import com.arnor4eck.medicinenotes.config.LimitsProperties;
import com.arnor4eck.medicinenotes.service.code_verifier.CodeGenerator;
import com.arnor4eck.medicinenotes.service.code_verifier.ExceededLimit;
import com.arnor4eck.medicinenotes.service.code_verifier.RedisStorage;
import com.arnor4eck.medicinenotes.service.mail_sender.SimpleMailSender;
import com.arnor4eck.medicinenotes.util.PreRegistration;
import com.arnor4eck.medicinenotes.util.exception.CodeVerifierException;
import com.arnor4eck.medicinenotes.util.request.CreateUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CodeVerifierService {

    private final RedisStorage<PreRegistration> codeRedisStorage;
    private final RedisStorage<Integer> attemptsRedisStorage;
    private final RedisStorage<Integer> sendRedisStorage;

    private final CodeGenerator codeGenerator;
    private final SimpleMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    private final LimitsProperties limitsProperties;

    private static final String SUBJECT = "Подтверждение регистрации";
    private static final String TEXT_PATTERN = "Код для подтверждения регистрации на сайте: %s";

    public CodeVerifierService(@Qualifier("codeRedisStorage") RedisStorage<PreRegistration> codeStorage,
                               @Qualifier("attemptsRedisStorage") RedisStorage<Integer> attemptsStorage,
                               @Qualifier("sendRedisStorage") RedisStorage<Integer> sendStorage,
                               SimpleMailSender mailSender, CodeGenerator codeGenerator,
                               PasswordEncoder passwordEncoder, LimitsProperties limitsProperties) {
        this.codeRedisStorage = codeStorage;
        this.attemptsRedisStorage = attemptsStorage;
        this.sendRedisStorage = sendStorage;

        this.mailSender = mailSender;
        this.codeGenerator = codeGenerator;
        this.passwordEncoder = passwordEncoder;

        this.limitsProperties = limitsProperties;
    }

    /**
     * Проверяет, существует ли пользователь в системе проверки кода.
     *
     * @param email Электронная почта пользователя, существование которого необходимо проверить
     * */
    public boolean isUserAlreadyInStorage(String email){
        return codeRedisStorage.get(email).isPresent();
    }

    /**
     * Отправляет код подтверждения и создает записи.
     * Предварительно не проверяет возможное превышение лимитов.
     *
     * @param request Запрос на регистрацию нового пользователя
     * */
    public void createAndSendCode(CreateUserRequest request){
        String code = codeGenerator.generateCode();

        mailSender.send(request.email(), SUBJECT, String.format(TEXT_PATTERN, code));

        codeRedisStorage.put(request.email(),
                new PreRegistration(request.email(), request.username(),
                        passwordEncoder.encode(request.password()), code));

        log.info("обнуление попыток");
        attemptsRedisStorage.put(request.email(), 0);
        sendRedisStorage.put(request.email(), 1);
    }

    /**
     * Обновляет данные о пользователе и отправляет новый код.
     * Применяется в случае, если процесс регистрации пользователя сохранен в хранилище, но он отправил данные о регистрации снова.
     *
     * @param request Запрос на регистрацию нового пользователя
     * */
    public void updateParamsAndSendCode(CreateUserRequest request){
        try{
            PreRegistration saved = getValueWithCheck(codeRedisStorage.get(request.email()));

            if(saved.username().equals(request.username()) &&
                passwordEncoder.matches(request.password(), saved.password()))
                return;

        }catch (CodeVerifierException e) {}

        String code = codeGenerator.generateCode();

        mailSender.send(request.email(), SUBJECT, String.format(TEXT_PATTERN, code));
        codeRedisStorage.put(request.email(),
                new PreRegistration(request.email(), request.username(),
                        passwordEncoder.encode(request.password()), code));

        attemptsRedisStorage.put(request.email(), 0);
        try{
            sendRedisStorage.put(request.email(),
                    getValueWithCheck(sendRedisStorage.get(request.email())) + 1);
        }catch (CodeVerifierException e) {
            sendRedisStorage.put(request.email(), 1);
        }
    }

    /**
     * Проверяет пользователя на возможные нарушения количества лимитов.
     * Сначала проверяется количество попыток, а после - количество запросов кода.
     *
     * @param email Электронная почта пользователя, чьи лимиты необходимо проверить
     * @throws CodeVerifierException Если записи с заданным ключем не существует
     * @return Возможное нарушение лимита
     * */
    public ExceededLimit checkLimits(String email) {
        log.info("Проверка лимитов");
        int attempts = getValueWithCheck(attemptsRedisStorage.get(email));
        int send = getValueWithCheck(sendRedisStorage.get(email));

        if(attempts > limitsProperties.getMaxCodeAttempts())
            return ExceededLimit.ATTEMPTS;
        if(send > limitsProperties.getMaxSendMail())
            return ExceededLimit.MAX_SEND;

        return ExceededLimit.NONE;
    }

    /**
     * Отправляет новый код подтверждения.
     * Предварительно не проверяет возможное превышение лимитов.
     * В случае успешной повторной отправки увеличивает счетчик запрошенных писем и обнуляет счетчик попыток.
     *
     * @param email Электронная почта пользователя, запросившего код
     * @throws CodeVerifierException Если записи с заданным ключем не существует
     * */
    public void sendNewCode(String email){
        PreRegistration savedCode = getValueWithCheck(codeRedisStorage.get(email));
        String code = codeGenerator.generateCode();

        mailSender.send(email, SUBJECT, String.format(TEXT_PATTERN, code));

        sendRedisStorage.put(email, getValueWithCheck(sendRedisStorage.get(email)) + 1);
        codeRedisStorage.put(email, new PreRegistration(savedCode, code));
        attemptsRedisStorage.put(email, 0);
        log.info("Отправлен новый код");
    }

    /**
     * Проверяет на соответствие сохранённый и отправленный коды.
     * В случае несовпадение не увеличивает количество неудачных попыток.
     *
     * @param email Электронная почта пользователя, приславшего код
     * @param code Код подтверждения, отправленный пользователем
     * @throws CodeVerifierException Если записи с заданным ключем не существует
     * @return Истина, если коды совпадают, иначе - ложь
     * */
    public boolean verifyCode(String email, String code){
        String savedCode = getValueWithCheck(codeRedisStorage.get(email)).code();

        return savedCode.equals(code);
    }

    /**
     * Возвращает сохранённый объект типа PreRegistration.
     *
     * @param email Электронная почта пользователя, чей объект необходимо вернуть
     * @throws CodeVerifierException Если записи с заданным ключем не существует
     * */
    public PreRegistration getPreRegistration(String email){
        return getValueWithCheck(codeRedisStorage.get(email));
    }

    /**
     * Увеличивает количетво использованных попыток на 1.
     *
     * @param email Электронная почта пользователя, чей счетчик нужно увеличить
     * @throws CodeVerifierException Если записи с заданным ключем не существует
     * */
    public void increaseAttempts(String email){
        attemptsRedisStorage.put(email, getValueWithCheck(attemptsRedisStorage.get(email)) + 1);
    }

    /**
     * Удаляет записи со всеми префиксами.
     *
     * @param email Электронная почта удаляемого пользователя
     * */
    public void removeAll(String email){
        codeRedisStorage.remove(email);
        attemptsRedisStorage.remove(email);
        sendRedisStorage.remove(email);
        log.info("Данные стерты");
    }

    private <T> T getValueWithCheck(Optional<? extends T> optional){
        return optional
                .orElseThrow(() -> new CodeVerifierException("Данные неактуальны. Отправьте код заново."));
    }
}
