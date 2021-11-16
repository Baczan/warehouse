package com.example.pkce.exceptions;

public class GenericUnauthorizedException extends Exception {

    public GenericUnauthorizedException() {
        super("Unauthorized");
    }
}
