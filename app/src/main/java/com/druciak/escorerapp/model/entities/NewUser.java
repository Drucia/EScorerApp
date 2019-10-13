package com.druciak.escorerapp.model.entities;

public class NewUser {
    private String name;
    private String surname;
    private String fullName;
    private String email;
    private String password;
    private Boolean isReferee;
    private String certificate;
    private String refereClass;

    public NewUser(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.fullName = name + " " + surname;
        this.email = email;
        this.password = password;
        this.isReferee = false;
        this.certificate = null;
        this.refereClass = null;
    }

    public NewUser(String name, String surname, String email, String password,
                   String certificate, String refereClass) {
        this.name = name;
        this.surname = surname;
        this.fullName = name + " " + surname;
        this.email = email;
        this.password = password;
        this.isReferee = true;
        this.certificate = certificate;
        this.refereClass = refereClass;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
