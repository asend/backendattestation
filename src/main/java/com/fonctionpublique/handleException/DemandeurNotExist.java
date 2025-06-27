package com.fonctionpublique.handleException;

public class DemandeurNotExist extends RuntimeException{
    private String messge;
    public DemandeurNotExist(String message) {
        super(message);
    }
}
