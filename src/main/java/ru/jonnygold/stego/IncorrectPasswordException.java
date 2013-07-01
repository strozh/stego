package ru.jonnygold.stego;

import java.security.GeneralSecurityException;

class IncorrectPasswordException extends GeneralSecurityException {

    public IncorrectPasswordException(String message) {
        super(message);
    }

    public IncorrectPasswordException(String message, Throwable throwable) {
        super(message, throwable);
    }
	
}