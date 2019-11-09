package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.google.firebase.auth.FirebaseUser;

public interface IUserInfo {
    interface IModel {
        void getUserAdditionalInfo(FirebaseUser user);
    }

    interface IPresenter{
        void onGetUserAdditionalInfoEventCompleteSuccess(Result.Success success);
        void onGetUserAdditionalInfoEventCompleteFailure(Result<LoggedInUser> result);
    }

    interface IView {
        void onGetUserAdditionalInfoEventFailure(String message);
        void onGetUserAdditionalInfoEventSuccess(LoggedInUser loggedInUser);
    }
}
