package com.arnor4eck.medicinenotes.service;

import com.arnor4eck.medicinenotes.config.LimitsProperties;
import com.arnor4eck.medicinenotes.service.code_verifier.CodeGenerator;
import com.arnor4eck.medicinenotes.service.code_verifier.ExceededLimit;
import com.arnor4eck.medicinenotes.service.code_verifier.RedisStorage;
import com.arnor4eck.medicinenotes.service.mail_sender.MailSenderService;
import com.arnor4eck.medicinenotes.util.PreRegistration;
import com.arnor4eck.medicinenotes.util.request.CreateUserRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CodeVerifierService {

    private final RedisStorage<PreRegistration> codeRedisStorage;
    private final RedisStorage<Integer> attemptsRedisStorage;
    private final RedisStorage<Integer> sendRedisStorage;

    private final CodeGenerator codeGenerator;
    private final MailSenderService mailSender;
    private final PasswordEncoder passwordEncoder;

    private final LimitsProperties limitsProperties;

    private static final String SUBJECT = "Подтверждение регистрации";
    private static final String TEXT_PATTERN = "Код для подтверждения регистрации на сайте: %s";

    public CodeVerifierService(@Qualifier("codeRedisStorage") RedisStorage<PreRegistration> codeStorage,
                               @Qualifier("attemptsRedisStorage") RedisStorage<Integer> attemptsStorage,
                               @Qualifier("sendRedisStorage") RedisStorage<Integer> sendStorage,
                               MailSenderService mailSender, CodeGenerator codeGenerator,
                               PasswordEncoder passwordEncoder, LimitsProperties limitsProperties) {
        this.codeRedisStorage = codeStorage;
        this.attemptsRedisStorage = attemptsStorage;
        this.sendRedisStorage = sendStorage;

        this.mailSender = mailSender;
        this.codeGenerator = codeGenerator;
        this.passwordEncoder = passwordEncoder;

        this.limitsProperties = limitsProperties;
    }

    public boolean isUserAlreadyInStorage(String email){
        return codeRedisStorage.get(email).isPresent();
    }

    public void createAndSendCode(CreateUserRequest request){
        String code = codeGenerator.generateCode();

        codeRedisStorage.put(request.email(),
                new PreRegistration(request.email(), request.username(),
                        passwordEncoder.encode(request.password()), code));

        attemptsRedisStorage.put(request.email(), 0);
        sendRedisStorage.put(request.email(), 1);

        mailSender.send(request.email(), SUBJECT, String.format(TEXT_PATTERN, code));
    }

    public ExceededLimit checkLimits(String email) {
        int attempts = attemptsRedisStorage.get(email).get();
        int send = sendRedisStorage.get(email).get();

        if(attempts > limitsProperties.getMaxCodeAttempts())
            return ExceededLimit.ATTEMPTS;
        if(send > limitsProperties.getMaxSendMail())
            return ExceededLimit.MAX_SEND;

        return ExceededLimit.NONE;
    }

    public void sendNewCode(String email){
        String code = codeGenerator.generateCode();
        PreRegistration savedCode = codeRedisStorage.get(email).get();

        codeRedisStorage.put(email, new PreRegistration(savedCode, code));
        sendRedisStorage.put(email, sendRedisStorage.get(email).get() + 1);
        attemptsRedisStorage.put(email, 0);

        mailSender.send(email, SUBJECT, String.format(TEXT_PATTERN, code));
    }

    public boolean verifyCode(String email, String code){
        String savedCode = codeRedisStorage.get(email).get().code();

        return savedCode.equals(code);
    }

    public PreRegistration getPreRegistration(String email){
        return codeRedisStorage.get(email).get();
    }

    public void increaseAttempts(String email){
        attemptsRedisStorage.put(email, attemptsRedisStorage.get(email).get() + 1);
    }

    public void removeAll(String email){
        codeRedisStorage.remove(email);
        attemptsRedisStorage.remove(email);
        sendRedisStorage.remove(email);
    }
}
