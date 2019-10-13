package com.druciak.escorerapp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements Parcelable {
    private String userId;
    private String fullName;
    private String email;
    private String photo;

    public LoggedInUser(FirebaseUser firebaseUser)
    {
        userId = firebaseUser.getUid();
        fullName = firebaseUser.getDisplayName();
        email = firebaseUser.getEmail();
    }

    protected LoggedInUser(Parcel in) {
        userId = in.readString();
        fullName = in.readString();
        email = in.readString();
        photo = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(fullName);
        parcel.writeString(email);
        parcel.writeString(photo);
    }
}
