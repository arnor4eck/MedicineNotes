package com.arnor4eck.medicinenotes.service;

import com.arnor4eck.medicinenotes.entity.Role;
import com.arnor4eck.medicinenotes.entity.User;
import com.arnor4eck.medicinenotes.repository.UserRepository;
import com.arnor4eck.medicinenotes.service.code_verifier.ExceededLimit;
import com.arnor4eck.medicinenotes.util.PreRegistration;
import com.arnor4eck.medicinenotes.util.dto.UserDto;
import com.arnor4eck.medicinenotes.util.exception.UserAlreadyExists;
import com.arnor4eck.medicinenotes.util.exception.not_found.UserNotFoundException;
import com.arnor4eck.medicinenotes.util.jwt.JwtAccessUtils;
import com.arnor4eck.medicinenotes.util.request.AuthenticationRequest;
import com.arnor4eck.medicinenotes.util.request.CreateUserRequest;
import com.arnor4eck.medicinenotes.util.request.NewCodeRequest;
import com.arnor4eck.medicinenotes.util.request.VerifyCodeRequest;
import com.arnor4eck.medicinenotes.util.response.AuthenticationResponse;
import com.arnor4eck.medicinenotes.util.response.VerifyEmailResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final JwtAccessUtils jwtUtils;

    private final AuthenticationManager manager;

    private final UserDetailsService userDetailsService;

    private final CodeVerifierService codeVerifierService;

    public void preregistration(CreateUserRequest request){
        try{
            userDetailsService.loadUserByUsername(request.email());
            throw new UserAlreadyExists("Пользователь с таким email уже существует");
        } catch (UserNotFoundException e) {
            if(codeVerifierService.isUserAlreadyInStorage(request.email())){ // Если пользователя уже есть код, ждем, когда он его отправит
                ExceededLimit limit = codeVerifierService.checkLimits(request.email()); // Не превысил ли пользователь лимиты

                switch (limit){
                    case NONE -> codeVerifierService.updateParamsAndSendCode(request); // Если пользователь отправил тот же запрос без нарушения лимитов
                    case MAX_SEND -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Количество запросов на повторную отправку превышено. Повторите попытку позже.");
                    case ATTEMPTS -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Количество неверных попыток превышено. Повторите попытку позже.");
                }
            }else // Если нет - отправляем код и настраиваем остальные параметры
                codeVerifierService.createAndSendCode(request);
        }
    }

    private boolean verifyCodeForRegistration(VerifyCodeRequest request){
        String email = request.email();
        String code = request.code();

        if (!codeVerifierService.isUserAlreadyInStorage(email)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Код не был запрошен. Запросите новый код."
            );
        }

        if (codeVerifierService.checkLimits(email) == ExceededLimit.ATTEMPTS) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Превышен лимит неверных попыток. Попробуйте позже."
            );
        }

        if(codeVerifierService.verifyCode(email, code)){
            PreRegistration savedPreRegistration = codeVerifierService.getPreRegistration(email);

            userRepository.save(
                User.builder()
                        .email(savedPreRegistration.email())
                        .password(savedPreRegistration.password())
                        .username(savedPreRegistration.username())
                        .role(Role.USER)
                        .build());
            codeVerifierService.removeAll(email);
            return true;
        }else
            codeVerifierService.increaseAttempts(email);
        return false;
    }

    public ResponseEntity<?> registration(VerifyCodeRequest request){
        return verifyCodeForRegistration(request) ?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().body(new VerifyEmailResponse("Код не совпадает с отправленным."));
    }

    public ResponseEntity<?> sendNewCode(NewCodeRequest request){
        if (!codeVerifierService.isUserAlreadyInStorage(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Код не был запрошен. Запросите новый код."
            );
        }

        if(codeVerifierService.checkLimits(request.email()) != ExceededLimit.MAX_SEND){
            codeVerifierService.sendNewCode(request.email());
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest()
                    .body(new VerifyEmailResponse(
                            "Превышено максимальное количество запросов. Повторите попытку позже."));
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        try{
            Authentication authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.email(),
                            authenticationRequest.password()));

            String token = jwtUtils.generateToken((User) authentication.getPrincipal());

            return new AuthenticationResponse(token);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    e.getMessage());
        }
    }

    public UserDto currentUser(String email){
        return UserDto.fromEntity(userRepository.findByEmail(email).get());
    }
}
