package com.project.GlobalExceptionHandler;

import com.project.Constant.Status;
import com.project.DTO.Error.ErrorDTO;
import com.project.Exception.Note.InvalidNoteIdException;
import com.project.Exception.User.InvalidCredentialsException;
import com.project.Exception.User.UserAlreadyExistException;
import com.project.Exception.User.InvalidJWTTokenException;
import com.project.Response.ResponseWithError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler
{
    //handler for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWithError> methodArgumentExceptionHandler(MethodArgumentNotValidException exception)
    {
        List<ErrorDTO> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    ErrorDTO errorDTO = new ErrorDTO(error.getField(), error.getDefaultMessage());
                    errors.add(errorDTO);
                });
        ResponseWithError responseWithError = ResponseWithError.builder()
                .status(Status.FAILED)
                .errors(errors)
                .build();

        log.info("GlobalExceptionHandler.methodArgumentExceptionHandler responseWithError {}", responseWithError);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWithError);
    }
    @ExceptionHandler(InvalidNoteIdException.class)
    public ResponseEntity<ResponseWithError> invalidNoteIdExceptionHandler(InvalidNoteIdException exception)
    {
        ResponseWithError responseWithError = errorResponseBuilderForGlobalException(exception.getMessage());

        log.info("GlobalExceptionHandler.invalidNoteIdExceptionHandler responseWithError {}", responseWithError);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWithError);
    }
    @ExceptionHandler(InvalidJWTTokenException.class)
    public ResponseEntity<ResponseWithError> invalidJWTTokenExceptionHandler(InvalidJWTTokenException exception)
    {
        ResponseWithError responseWithError = errorResponseBuilderForGlobalException(exception.getMessage());

        log.info("GlobalExceptionHandler.invalidJWTTokenExceptionHandler responseWithError {}", responseWithError);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseWithError);

    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseWithError> usernameNotFoundExceptionHandler(UsernameNotFoundException exception)
    {
        ResponseWithError responseWithError = errorResponseBuilderForGlobalException(exception.getMessage());

        log.info("GlobalExceptionHandler.usernameNotFoundExceptionHandler responseWithError {}", responseWithError);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWithError);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ResponseWithError> invalidCredentialsExceptionHandler(InvalidCredentialsException exception)
    {
        ResponseWithError responseWithError = errorResponseBuilderForGlobalException(exception.getMessage());

        log.info("GlobalExceptionHandler.invalidCredentialsExceptionHandler responseWithError {}", responseWithError);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseWithError);
    }
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ResponseWithError> userAlreadyExistExceptionHandler(UserAlreadyExistException exception)
    {
        ResponseWithError responseWithError = errorResponseBuilderForGlobalException(exception.getMessage());
        log.info("GlobalExceptionHandler.userAlreadyExistExceptionHandler responseWithError {}", responseWithError);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseWithError);
    }
    @ExceptionHandler(Exception.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public ResponseEntity<ResponseWithError> genericExceptionHandler(Exception exception)
    {
        ResponseWithError responseWithError = errorResponseBuilderForGlobalException(exception.getMessage());

        log.info("GlobalExceptionHandler.userAlreadyExistExceptionHandler responseWithError {}", responseWithError);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseWithError);
    }
    private ResponseWithError errorResponseBuilderForGlobalException(String errorMessage)
    {
        return ResponseWithError.builder()
                .status(Status.FAILED)
                .errors(Collections.singletonList(new ErrorDTO("", errorMessage)))
                .build();
    }


}
