package com.fonctionpublique.exception;

public class ExpirationTokenException extends RuntimeException {
    public ExpirationTokenException(String msg) {
        super(msg);
    }
}
