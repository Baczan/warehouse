package com.example.pkce.exceptions;

public class GenericBadRequestError extends RuntimeException {

    public GenericBadRequestError(String message) {
        super(message);
    }
}
