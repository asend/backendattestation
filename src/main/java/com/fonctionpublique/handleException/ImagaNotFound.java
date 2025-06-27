package com.fonctionpublique.handleException;

public class ImagaNotFound extends RuntimeException{

    private String messge;
    public ImagaNotFound(String message) {
        super(message);
    }
}
