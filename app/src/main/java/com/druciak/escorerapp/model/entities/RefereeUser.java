package com.druciak.escorerapp.model.entities;

public class RefereeUser extends LoggedInUser {
    private String certNumber;
    private String refereeClass;

    public RefereeUser(NewUser user, String email) {
        super(user, email);
        certNumber = user.getCertificate();
        refereeClass = user.getRefereeClass();
    }

    public String getCertNumber() {
        return certNumber;
    }

    public String getRefereeClass() {
        return refereeClass;
    }
}
