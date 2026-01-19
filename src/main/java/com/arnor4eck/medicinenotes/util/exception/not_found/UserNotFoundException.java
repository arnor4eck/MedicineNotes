package com.arnor4eck.medicinenotes.util.exception.not_found;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
