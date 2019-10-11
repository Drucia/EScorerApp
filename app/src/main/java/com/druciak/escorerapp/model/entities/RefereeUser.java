package com.druciak.escorerapp.model.entities;

import com.google.firebase.auth.FirebaseUser;

public class RefereeUser extends LoggedInUser {
    private String certNumber;
    private String refereeClass;

    public RefereeUser(FirebaseUser user) {
        super(user);
    }

    public String getCertNumber() {
        return certNumber;
    }

    public String getRefereeClass() {
        return refereeClass;
    }
}
