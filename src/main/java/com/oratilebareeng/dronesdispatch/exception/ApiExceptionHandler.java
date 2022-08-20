package com.oratilebareeng.dronesdispatch.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    // handle failed validation errors
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(MethodArgumentNotValidException e){

        Map<String, String> apiErrors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) ->
        {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            apiErrors.put(fieldName, errorMessage);

        });
        return new ResponseEntity<>(
                apiErrors,
                HttpStatus.BAD_REQUEST
        );
    }

    // handle invalid data format provided
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> handleIllegalArgumentException(Exception e){
        return new ResponseEntity<>(
               "ERROR: check you have entered correct data.\n" + e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<String> handleExceptions(Exception e){
        return new ResponseEntity<>(
                "ERROR: " + e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }
}
