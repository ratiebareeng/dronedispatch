package com.oratilebareeng.dronesdispatch.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    // handle failed validation errors
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request){

        Map<String, String> apiErrors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) ->
        {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            apiErrors.put(fieldName, errorMessage);

        });
        return new ResponseEntity<>(
                apiErrors,
                status
        );

    }

    /*@Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)*/

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
    public ResponseEntity<String> customHandleExceptions(Exception e){
        return new ResponseEntity<>(
                "ERROR: " + e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ObjectNotFoundException.class, ValidationException.class, ObjectExistsException.class})
    public ResponseEntity<ApiException> handleDroneExistsExceptions(Exception e){
        return new ResponseEntity<>(
                new ApiException(
                        e.getMessage(),
                        HttpStatus.NOT_FOUND,
                        ZonedDateTime.now(ZoneId.of("Z"))
                ),
                HttpStatus.NOT_FOUND
        );
    }
}
