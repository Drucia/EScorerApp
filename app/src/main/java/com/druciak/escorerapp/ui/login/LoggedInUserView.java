package com.druciak.escorerapp.ui.login;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView implements Parcelable {
    private String displayName;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    protected LoggedInUserView(Parcel in) {
        displayName = in.readString();
    }

    public static final Creator<LoggedInUserView> CREATOR = new Creator<LoggedInUserView>() {
        @Override
        public LoggedInUserView createFromParcel(Parcel in) {
            return new LoggedInUserView(in);
        }

        @Override
        public LoggedInUserView[] newArray(int size) {
            return new LoggedInUserView[size];
        }
    };

    String getDisplayName() {
        return displayName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(displayName);
    }
}
