package com.druciak.escorerapp.model.entities;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    private String userId;
    private String name;
    private String surname;
    private String email;
    private Boolean isReferee;

    public LoggedInUser(NewUser user, String email){
        userId = user.getId();
        name = user.getName();
        surname = user.getSurname();
        this.email = email;
        isReferee = user.getIsReferee();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getReferee() {
        return isReferee;
    }

    public String getFullName() {
        return name + " " + surname;
    }
}
