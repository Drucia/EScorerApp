package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.model.entities.LoggedInUser;
import com.druciak.escorerapp.model.entities.NewUser;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.google.firebase.auth.FirebaseUser;

public interface IMainPanelMVP {
    interface IPresenter{
        void logout();
        void createdActivity();
        void onGetUserEventComplete(Result<LoggedInUser> user);
        void onCustomUserFieldsSaveClicked(String name, String surname, String certificate,
                                           String class_);
        void onCustomUserFieldsSaveClicked(String name, String surname);
    }

    interface IView{
        void onClick(int gameId);
        void onLogoutCompleted();
        void setLoggedInUserFields(String fullName, String email);

        void showPopUpWithSetUserFields();
    }

    interface ILoggedInUserModel{
        void getUserInformation(FirebaseUser firebaseUser);
        void setUserInformation(FirebaseUser firebaseUser, NewUser user);
    }
}
