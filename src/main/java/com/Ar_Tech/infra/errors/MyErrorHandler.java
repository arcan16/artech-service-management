package com.Ar_Tech.infra.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class MyErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<errors> errorsList = e.getFieldErrors().stream().map(errors::new).toList();

        return ResponseEntity.badRequest().body(errorsList);
    }

    public record errors(String field, String message){
        public errors (FieldError f){
            this(f.getField(),f.getDefaultMessage());
        }
    }
}
