package com.druciak.escorerapp.view.ui.login;

import android.os.Parcel;
import android.os.Parcelable;

import com.druciak.escorerapp.viewModel.model.LoggedInUser;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView implements Parcelable {
    private String fullName;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(LoggedInUser loginUser) {
        this.fullName = loginUser.getFullName();
    }

    protected LoggedInUserView(Parcel in) {
        fullName = in.readString();
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

    String getFullName() {
        return fullName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fullName);
    }
}
