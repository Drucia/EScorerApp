package com.druciak.escorerapp.data.model;

import com.google.firebase.auth.FirebaseUser;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private FirebaseUser firebaseUser;

    public LoggedInUser(FirebaseUser user) {
        this.firebaseUser = user;
    }

    public String getUserId() {
        return "id";
    }

    public String getDisplayName() {
        return "displayname";
    }
}
