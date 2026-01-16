package com.arnor4eck.medicinenotes.service;

import com.arnor4eck.medicinenotes.entity.Role;
import com.arnor4eck.medicinenotes.entity.User;
import com.arnor4eck.medicinenotes.repository.UserRepository;
import com.arnor4eck.medicinenotes.util.request.CreateUserRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void registration(CreateUserRequest request){
        userRepository.save(
                User.builder()
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .username(request.username())
                        .role(Role.USER)
                        .build()
        );
    }
}
