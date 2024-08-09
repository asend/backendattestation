package com.fonctionpublique.exception;

public class NotMatchPasswordOldException extends RuntimeException {
    public NotMatchPasswordOldException(String message) {
        super(message);
    }
}
