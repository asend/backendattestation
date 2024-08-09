package com.fonctionpublique.exception;

public class EntityNotMatchException extends RuntimeException {
    public EntityNotMatchException(String msg) {
        super(msg);
    }
}
