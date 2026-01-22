package com.arnor4eck.medicinenotes;

import com.arnor4eck.medicinenotes.entity.Role;
import com.arnor4eck.medicinenotes.entity.User;
import com.arnor4eck.medicinenotes.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {

        if((userRepository.findByEmail("arnor4eck@mail.ru").isEmpty()))
            userRepository.save(User.builder()
                    .email("arnor4eck@mail.ru")
                    .role(Role.USER)
                    .password(passwordEncoder.encode("password"))
                    .username("arnor4eck")
                    .build());
    }
}
