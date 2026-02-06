package com.arnor4eck.medicinenotes.util.controller_advice;

import com.arnor4eck.medicinenotes.util.response.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExceptionResponseFactory {
    public ExceptionResponse create(String error,
                                    HttpServletResponse response,
                                    HttpStatus status){
        return create(List.of(error), response, status);
    }

    public ExceptionResponse create(List<String> errors,
                                    HttpServletResponse response,
                                    HttpStatus status){
        response.setStatus(status.value());
        return new ExceptionResponse(status.value(), errors);
    }
}
