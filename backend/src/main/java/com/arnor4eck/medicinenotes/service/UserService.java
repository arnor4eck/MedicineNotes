package com.arnor4eck.medicinenotes.service;

import com.arnor4eck.medicinenotes.entity.Role;
import com.arnor4eck.medicinenotes.entity.User;
import com.arnor4eck.medicinenotes.repository.UserRepository;
import com.arnor4eck.medicinenotes.util.jwt.JwtAccessUtils;
import com.arnor4eck.medicinenotes.util.request.AuthenticationRequest;
import com.arnor4eck.medicinenotes.util.request.CreateUserRequest;
import com.arnor4eck.medicinenotes.util.response.AuthenticationResponse;
import com.arnor4eck.medicinenotes.util.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtAccessUtils jwtUtils;

    private final AuthenticationManager manager;

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
