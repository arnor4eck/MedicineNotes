package com.arnor4eck.medicinenotes.controller;

import com.arnor4eck.medicinenotes.service.UserService;
import com.arnor4eck.medicinenotes.util.dto.UserDto;
import com.arnor4eck.medicinenotes.util.request.AuthenticationRequest;
import com.arnor4eck.medicinenotes.util.request.CreateUserRequest;
import com.arnor4eck.medicinenotes.util.request.NewCodeRequest;
import com.arnor4eck.medicinenotes.util.request.VerifyCodeRequest;
import com.arnor4eck.medicinenotes.util.response.AuthenticationResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    @RateLimiter(name = "registrationLimiter")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void preregistration(@RequestBody @Valid CreateUserRequest request) {
        userService.preregistration(request);
    }

    @PostMapping("/registration/verify")
    public ResponseEntity<?> registration(@RequestBody @Valid VerifyCodeRequest request) {
        return userService.registration(request);
    }

    @PostMapping("/registration/new/code")
    public ResponseEntity<?> newCode(@RequestBody @Valid NewCodeRequest request) {
        return userService.sendNewCode(request);
    }

    @PostMapping("/authentication")
    @RateLimiter(name = "authLimiter")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody @Valid AuthenticationRequest request) {
        AuthenticationResponse response = userService.authenticate(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> currentUser(@AuthenticationPrincipal String email){
        return ResponseEntity.ok().body(userService.currentUser(email));
    }
}
