package com.druciak.escorerapp.view.login;

import android.content.Intent;

import com.druciak.escorerapp.presenter.LoginFormState;

public interface ILoginView {
    void onDataValidChecked(LoginFormState loginFormState);
    void onLoginEventCompleteSuccessfully(Object data);
    void onLoginEventCompleteError(Exception error);
    void startGoogleLoginPopUp(Intent signInIntent);
}
