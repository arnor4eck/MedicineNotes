package com.arnor4eck.medicinenotes.controller;

import com.arnor4eck.medicinenotes.service.UserService;
import com.arnor4eck.medicinenotes.util.request.AuthenticationRequest;
import com.arnor4eck.medicinenotes.util.request.CreateUserRequest;
import com.arnor4eck.medicinenotes.util.response.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserRequest request) {
        userService.registration(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody @Valid AuthenticationRequest request) {
        AuthenticationResponse response = userService.authenticate(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}
