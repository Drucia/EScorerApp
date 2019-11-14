package com.druciak.escorerapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements Parcelable {
    String userId;
    String name;
    String surname;
    String email;
    Boolean isReferee;

    public LoggedInUser() {} // todo only for mock

    public LoggedInUser(NewUser user, String email){
        userId = user.getId();
        name = user.getName();
        surname = user.getSurname();
        this.email = email;
        isReferee = user.getIsReferee();
    }

    protected LoggedInUser(Parcel in) {
        userId = in.readString();
        name = in.readString();
        surname = in.readString();
        email = in.readString();
        byte tmpIsReferee = in.readByte();
        isReferee = tmpIsReferee == 0 ? null : tmpIsReferee == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(email);
        dest.writeByte((byte) (isReferee == null ? 0 : isReferee ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoggedInUser> CREATOR = new Creator<LoggedInUser>() {
        @Override
        public LoggedInUser createFromParcel(Parcel in) {
            return new LoggedInUser(in);
        }

        @Override
        public LoggedInUser[] newArray(int size) {
            return new LoggedInUser[size];
        }
    };

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
