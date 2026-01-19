package com.arnor4eck.medicinenotes.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StatusValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank
public @interface Status {
    String message() default "Некорректный формат статуса";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
