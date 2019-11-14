package com.druciak.escorerapp.entities;

import android.os.Parcel;

public class RefereeUser extends LoggedInUser {
    private String certNumber;
    private String refereeClass;

    public RefereeUser(){}

    public RefereeUser(NewUser user, String email) {
        super(user, email);
        certNumber = user.getCertificate();
        refereeClass = user.getRefereeClass();
    }

    protected RefereeUser(Parcel in) {
        super(in);
        certNumber = in.readString();
        refereeClass = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(email);
        dest.writeByte((byte) (isReferee == null ? 0 : isReferee ? 1 : 2));

        dest.writeString(certNumber);
        dest.writeString(refereeClass);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RefereeUser> CREATOR = new Creator<RefereeUser>() {
        @Override
        public RefereeUser createFromParcel(Parcel in) {
            return new RefereeUser(in);
        }

        @Override
        public RefereeUser[] newArray(int size) {
            return new RefereeUser[size];
        }
    };

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }

    public void setRefereeClass(String refereeClass) {
        this.refereeClass = refereeClass;
    }

    public String getCertNumber() {
        return certNumber;
    }

    public String getRefereeClass() {
        return refereeClass;
    }
}
