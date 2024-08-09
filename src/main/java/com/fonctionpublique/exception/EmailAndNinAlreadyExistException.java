package com.fonctionpublique.exception;

public class EmailAndNinAlreadyExistException extends RuntimeException {
    public EmailAndNinAlreadyExistException(String msg) {
        super(msg);
    }
}
