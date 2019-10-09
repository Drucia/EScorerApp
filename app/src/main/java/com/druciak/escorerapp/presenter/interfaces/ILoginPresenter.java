package com.druciak.escorerapp.presenter.interfaces;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface ILoginPresenter {
    void login(Context context, String username, String password);
    void loginWithGoogle(Context context, GoogleSignInAccount account);
    void signIn(Context context, String username, String password); // TODO make user login entity
    void isDataValid(String username, String password);
    void clickedLoginWithGoogle(Context context);
    void activityIsStarted();
}
