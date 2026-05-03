package com.Ar_Tech.infra.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.List;

@RestControllerAdvice
public class MyErrorHandler {

    @ExceptionHandler(MyIntegrityValidation.class)
    public ResponseEntity<?> myIntegrityValidationExceptionHandler(MyIntegrityValidation exception){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        response.put("message", exception.getMessage());
        response.put("status", 400);

        return ResponseEntity.badRequest().body(response);
    }

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
