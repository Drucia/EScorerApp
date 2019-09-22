package com.druciak.escorerapp.data;

import android.content.Context;

import com.druciak.escorerapp.data.model.LoggedInUser;
import com.druciak.escorerapp.ui.interfaces.OnLoginListener;
import com.druciak.escorerapp.ui.login.LoginViewModel;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository implements OnLoginListener {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;
    private OnLoginListener onLoginListener;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        Result result = dataSource.isLoggedIn();
        if (result instanceof Result.Success)
        {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public void login(LoginViewModel loginViewModel, Context context, String username, String password) {
        // handle login
        dataSource.login(this, context, username, password);
        onLoginListener = loginViewModel;
    }

    public void signin(LoginViewModel loginViewModel, Context context, String username, String password) {
        // handle signin
        dataSource.signin(this, context, username, password);
        onLoginListener = loginViewModel;
    }

    @Override
    public void onLoginEventComplete(Result<LoggedInUser> result) {
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        onLoginListener.onLoginEventComplete(result);
    }
}
