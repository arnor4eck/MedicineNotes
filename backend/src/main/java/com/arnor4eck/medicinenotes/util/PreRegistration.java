package com.arnor4eck.medicinenotes.util;

public record PreRegistration(String email, String username, String password, String code) {
    public PreRegistration(PreRegistration preRegistration, String code) {
        this(preRegistration.email(), preRegistration.username(),
                preRegistration.password(), code);
    }
}
