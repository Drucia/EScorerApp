package com.druciak.escorerapp.presenter;

import android.content.Context;
import android.util.Patterns;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.ILoginMVP;
import com.druciak.escorerapp.interfaces.IUserInfo;
import com.druciak.escorerapp.model.entities.LoggedInUser;
import com.druciak.escorerapp.model.firebaseService.FirebaseManager;
import com.druciak.escorerapp.model.firebaseService.Result;
import com.druciak.escorerapp.model.internalApiService.InternalApiManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter implements ILoginMVP.IPresenter, IUserInfo.IModel {
    private ILoginMVP.IView view;
    private ILoginMVP.IModel loginManager;
    private IUserInfo.IModel userManager;

    public LoginPresenter(ILoginMVP.IView view)
    {
        this.view = view;
        loginManager = new FirebaseManager(this);
        userManager = new InternalApiManager(this);
    }

    @Override
    public void onLoginEventComplete(Result<FirebaseUser> result) {
        if (result instanceof Result.Success)
            view.onLoginEventCompleteSuccessfully(((Result.Success<FirebaseUser>) result).getData());
        else
            view.onLoginEventCompleteError(((Result.Error) result).getError());
    }

    @Override
    public void onGetUserAdditionalInfoEventCompleteFailure(Result<LoggedInUser> user) {
        if (user instanceof Result.Success) {
            FirebaseUser loggedInUser = ((Result.Success<FirebaseUser>) user).getData();
            view.onGetUserLoggedInEventCompleteSuccess(loggedInUser);
        }
        else {
            view.onGetUserAdditionalInfoEventFailure(((Result.Error) user).getError().getMessage());
        }
    }

    @Override
    public void login(String username, String password) {
        loginManager.login(username, password);
    }

    @Override
    public void loginWithGoogle(GoogleSignInAccount account) {
        loginManager.loginWithGoogle(account);
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
        Result<FirebaseUser> result = loginManager.getLoggedIn();
        if (result instanceof Result.Success)
            view.onLoginEventCompleteSuccessfully(((Result.Success<FirebaseUser>) result).getData());
    }

    @Override
    public void getUserAdditionalInfo(FirebaseUser user) {
        userManager.getUserAdditionalInfo(user);
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
    public void onGetUserAdditionalInfoEventCompleteSuccess(Result.Success success) {
        view.onGetUserAdditionalInfoEventSuccess((LoggedInUser) success.getData());
    }
}
