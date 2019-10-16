package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.model.entities.LoggedInUser;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.google.firebase.auth.FirebaseUser;

public interface IMainPanelMVP {
    interface IPresenter{
        void logout();
        void createdActivity();
        void onGetUserEventComplete(Result<LoggedInUser> user);
    }

    interface IView{
        void onClick(int gameId);
        void onLogoutCompleted();
        void setLoggedInUserFields(String fullName, String email);
    }

    interface ILoggedInUserModel{
        void getUserInformations(FirebaseUser firebaseUser);
    }
}
