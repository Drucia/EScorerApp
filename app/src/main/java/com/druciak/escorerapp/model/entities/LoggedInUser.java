package com.druciak.escorerapp.model.entities;

import com.google.firebase.auth.FirebaseUser;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String fullName;
    private String email;
    private String photo;

    public LoggedInUser(FirebaseUser firebaseUser)
    {
        fullName = firebaseUser.getDisplayName();
        email = firebaseUser.getEmail();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
