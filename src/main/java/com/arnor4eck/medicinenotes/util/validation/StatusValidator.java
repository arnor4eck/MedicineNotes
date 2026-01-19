package com.arnor4eck.medicinenotes.util.validation;

import com.arnor4eck.medicinenotes.entity.IntakesStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusValidator implements ConstraintValidator<Status, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try{
            IntakesStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
