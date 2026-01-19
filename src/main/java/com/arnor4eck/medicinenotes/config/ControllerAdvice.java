package com.arnor4eck.medicinenotes.config;

import com.arnor4eck.medicinenotes.util.exception.illegal_argument.LimitExceededException;
import com.arnor4eck.medicinenotes.util.exception.not_found.NotFoundException;
import com.arnor4eck.medicinenotes.util.response.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
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

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ExceptionResponse handleValidationException(HandlerMethodValidationException ex,
                                                       HttpServletResponse response) {
        // Извлекаем все ошибки валидации
        List<String> messages = ex.getParameterValidationResults().stream()
                .flatMap(r -> r.getResolvableErrors().stream())
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList();

        int errorCode = HttpStatus.BAD_REQUEST.value();

        response.setStatus(errorCode);

        return new ExceptionResponse(errorCode, messages);
    }

    @ExceptionHandler({LimitExceededException.class})
    public ExceptionResponse responseLimitExceededException(LimitExceededException e,
                                                            HttpServletResponse response) {
        int errorCode = HttpStatus.BAD_REQUEST.value();
        response.setStatus(errorCode);

        return new ExceptionResponse(errorCode, e.getMessage());
    }

    @ExceptionHandler({NotFoundException.class})
    public ExceptionResponse responseStatusException(NotFoundException e,
                                                     HttpServletResponse response){
        int errorCode = HttpStatus.NOT_FOUND.value();
        response.setStatus(errorCode);

        return new ExceptionResponse(errorCode, e.getMessage());
    }
}
