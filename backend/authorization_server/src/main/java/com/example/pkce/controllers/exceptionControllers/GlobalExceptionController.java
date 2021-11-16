package com.example.pkce.controllers.exceptionControllers;

import com.example.pkce.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController {

  @ExceptionHandler(
      value = {
        ClientNotFoundException.class,
        GenericBadRequestError.class,
        ElasticUserNotFound.class,
        BadPageableProperty.class
      })
  public ResponseEntity<?> handleBadRequestError(Exception exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {GenericUnauthorizedException.class})
  public ResponseEntity<?> handleUnauthorizedError(Exception exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
  }
}
