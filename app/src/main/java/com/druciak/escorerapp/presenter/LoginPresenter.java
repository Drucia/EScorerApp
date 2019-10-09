package com.druciak.escorerapp.presenter;

import android.content.Context;
import android.util.Patterns;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.model.firebaseService.LoginManager;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.druciak.escorerapp.model.login.LoggedInUser;
import com.druciak.escorerapp.presenter.interfaces.ILoginPresenter;
import com.druciak.escorerapp.presenter.interfaces.ILoginPresenterForManager;
import com.druciak.escorerapp.view.login.ILoginView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class LoginPresenter implements ILoginPresenter, ILoginPresenterForManager {
    private ILoginView view;
    private LoginManager loginManager;

    public LoginPresenter(ILoginView view)
    {
        this.view = view;
        loginManager = new LoginManager(this);
    }

    @Override
    public void onLoginEventComplete(Result<LoggedInUser> result) {
        if (result instanceof Result.Success)
            view.onLoginEventCompleteSuccessfully(((Result.Success) result).getData());
        else
            view.onLoginEventCompleteError(((Result.Error) result).getError());
    }

    @Override
    public void login(Context context, String username, String password) {
        loginManager.login(context, username, password);
    }

    @Override
    public void loginWithGoogle(Context context, GoogleSignInAccount account) {
        loginManager.loginWithGoogle(context, account);
    }

    @Override
    public void isDataValid(String username, String password) {
        if (!isUserNameValid(username)) {
            view.onDataValidChecked(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            view.onDataValidChecked(new LoginFormState(null, R.string.invalid_password));
        }
        else {
            view.onDataValidChecked(new LoginFormState(true));
        }
    }

    @Override
    public void clickedLoginWithGoogle(Context context) {
        loginManager.initializeGoogleService(context);
        view.startGoogleLoginPopUp(loginManager.getGoogleClient().getSignInIntent());
    }

    @Override
    public void activityIsStarted() {
        Result<LoggedInUser> result = loginManager.getLoggedIn();
        if (result instanceof Result.Success)
            view.onLoginEventCompleteSuccessfully(((Result.Success) result).getData());
    }

    private boolean isUserNameValid(String username){
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    private boolean isPasswordValid(String password){
        return password != null && password.trim().length() > 5;
    }

    @Override
    public void signIn(Context context, String username, String password) {
        // TODO
    }
}
