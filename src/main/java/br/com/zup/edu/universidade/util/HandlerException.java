package br.com.zup.edu.universidade.util;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class HandlerException {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValid(MethodArgumentNotValidException ex){

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        List<String> response = fieldErrors.stream()
                .map(e -> String.format("O campo %s %s", e.getField(), e.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(response);
    }
}
