package com.fonctionpublique.handleException;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NinAlreadyExistException extends RuntimeException {

    private String message;
    public NinAlreadyExistException(String message)  {
        super(message);
    }
}
