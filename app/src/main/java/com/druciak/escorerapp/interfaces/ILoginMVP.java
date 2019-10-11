package com.druciak.escorerapp.interfaces;

import android.content.Context;
import android.content.Intent;

import com.druciak.escorerapp.model.entities.LoggedInUser;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.druciak.escorerapp.presenter.LoginFormState;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public interface ILoginMVP {
    interface IPresenter{
        void login(String username, String password);
        void signIn(String username, String password); // TODO make user login entity
        void loginWithGoogle(GoogleSignInAccount account);
        void isDataValid(String username, String password);
        void clickedLoginWithGoogle(Context context);
        void activityIsStarted();
        void onLoginEventComplete(Result<LoggedInUser> result);
    }

    interface IView{
        void onDataValidChecked(LoginFormState loginFormState);
        void onLoginEventCompleteSuccessfully(Object data);
        void onLoginEventCompleteError(Exception error);
        void startGoogleLoginPopUp(Intent signInIntent);
    }

    interface IModel{
        void initializeGoogleService(Context context);
        void login(String username, String password);
        void signin(String username, String password);
        Result<LoggedInUser> getLoggedIn();
        void logout();
        void loginWithGoogle(GoogleSignInAccount account);
        GoogleSignInClient getGoogleClient();
    }
}
