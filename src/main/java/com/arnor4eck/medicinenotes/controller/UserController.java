package com.arnor4eck.medicinenotes.controller;

import com.arnor4eck.medicinenotes.entity.User;
import com.arnor4eck.medicinenotes.service.UserService;
import com.arnor4eck.medicinenotes.util.request.CreateUserRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
