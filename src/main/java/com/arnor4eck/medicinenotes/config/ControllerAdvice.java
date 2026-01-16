package com.arnor4eck.medicinenotes.config;

import com.arnor4eck.medicinenotes.util.response.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ExceptionResponse methodArgumentNotValidException(MethodArgumentNotValidException e,
                                                             HttpServletResponse response){

        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(field ->
                        field.getField() + ": " + field.getDefaultMessage())
                .toList();

        int errorCode = HttpStatus.BAD_REQUEST.value();
        response.setStatus(errorCode);

        return new ExceptionResponse(errorCode, errors);
    }
}
