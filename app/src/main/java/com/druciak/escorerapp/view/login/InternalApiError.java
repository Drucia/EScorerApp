package com.druciak.escorerapp.view.login;

public class InternalApiError extends Exception{
    private int id;
    private String message;

    public InternalApiError(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
