package com.druciak.escorerapp.interfaces;

import android.content.Context;
import android.content.Intent;

import com.druciak.escorerapp.model.firebaseService.Result;
import com.druciak.escorerapp.presenter.LoginFormState;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;

public interface ILoginMVP {
    interface IPresenter extends IUserInfo.IPresenter{
        void login(String username, String password);
        void loginWithGoogle(GoogleSignInAccount account);
        void isDataValid(String username, String password);
        void clickedLoginWithGoogle(Context context);
        void activityIsStarted();
        void getUserAdditionalInfo(FirebaseUser user);
        void onLoginEventComplete(Result<FirebaseUser> result);
    }

    interface IView extends IUserInfo.IView{
        void onDataValidChecked(LoginFormState loginFormState);
        void onLoginEventCompleteSuccessfully(FirebaseUser data);
        void onLoginEventCompleteError(Exception error);
        void startGoogleLoginPopUp(Intent signInIntent);
        void onGetUserLoggedInEventCompleteSuccess(FirebaseUser loggedInUser);
    }

    interface IModel{
        void initializeGoogleService(Context context);
        void login(String username, String password);
        Result<FirebaseUser> getLoggedIn();
        void logout();
        void loginWithGoogle(GoogleSignInAccount account);
        GoogleSignInClient getGoogleClient();
    }
}
