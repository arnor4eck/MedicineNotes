package com.arnor4eck.medicinenotes;

import com.arnor4eck.medicinenotes.entity.Role;
import com.arnor4eck.medicinenotes.entity.User;
import com.arnor4eck.medicinenotes.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev"})
@AllArgsConstructor
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(User.builder()
                .email("admin@admin.admin")
                .role(Role.USER)
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .build());
    }
}
