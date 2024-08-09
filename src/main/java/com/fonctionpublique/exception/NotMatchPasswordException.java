package com.fonctionpublique.exception;

public class NotMatchPasswordException extends RuntimeException {
    public NotMatchPasswordException(String msg) {
        super(msg);
    }
}
