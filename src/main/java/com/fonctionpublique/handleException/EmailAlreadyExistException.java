package com.fonctionpublique.handleException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailAlreadyExistException extends RuntimeException {
    private String messge;
    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
