package com.arnor4eck.medicinenotes.util.exception.illegal_argument;

public class LimitExceededException extends IllegalArgumentException {
    public LimitExceededException(String message) {
        super(message);
    }
}
