package com.arnor4eck.medicinenotes.config;

import com.arnor4eck.medicinenotes.util.exception.NotFoundException;
import com.arnor4eck.medicinenotes.util.response.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

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

    @ExceptionHandler({ResponseStatusException.class})
    public ExceptionResponse responseStatusException(ResponseStatusException e,
                                                     HttpServletResponse response){
        int errorCode = e.getStatusCode().value();
        response.setStatus(errorCode);

        return new ExceptionResponse(errorCode, e.getReason());
    }

    @ExceptionHandler({NotFoundException.class})
    public ExceptionResponse responseStatusException(NotFoundException e,
                                                     HttpServletResponse response){
        int errorCode = HttpStatus.NOT_FOUND.value();
        response.setStatus(errorCode);

        return new ExceptionResponse(errorCode, e.getMessage());
    }
}
