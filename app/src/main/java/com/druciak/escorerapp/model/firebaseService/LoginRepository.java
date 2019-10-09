//package com.druciak.escorerapp.service.login;
//
//import android.content.Context;
//
//import com.druciak.escorerapp.model.login.LoggedInUser;
//import com.druciak.escorerapp.view.login.ILoginView;
//import com.druciak.escorerapp.view.login.LoginViewModel;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//
///**
// * Class that requests authentication and user information from the remote data source and
// * maintains an in-memory cache of login status and user credentials information.
// */
//public class LoginRepository implements ILoginView {
//
//    private static volatile LoginRepository instance;
//
//    private LoginDataService dataSource;
//    private ILoginView onLoginListener;
//
//    // If user credentials will be cached in local storage, it is recommended it be encrypted
//    // @see https://developer.android.com/training/articles/keystore
//    private LoggedInUser user = null;
//    private GoogleSignInClient googleSignInClient = null;
//
//    // private constructor : singleton access
//    private LoginRepository(LoginDataService dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    public static LoginRepository getInstance(LoginDataService dataSource) {
//        if (instance == null) {
//            instance = new LoginRepository(dataSource);
//        }
//        return instance;
//    }
//
//    public GoogleSignInClient getGoogleSignInClient()
//    {
//        return googleSignInClient;
//    }
//
//    public void setGoogleSignInClient(GoogleSignInClient client)
//    {
//        googleSignInClient = client;
//    }
//
//    public boolean isLoggedIn() {
//        Result result = dataSource.isLoggedIn();
//        if (result instanceof Result.Success)
//        {
//            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
//        }
//        return user != null;
//    }
//
//    public void logout() {
//        user = null;
//        dataSource.logout();
//    }
//
//    private void setLoggedInUser(LoggedInUser user) {
//        this.user = user;
//        // If user credentials will be cached in local storage, it is recommended it be encrypted
//        // @see https://developer.android.com/training/articles/keystore
//    }
//
//    public void login(LoginViewModel loginViewModel, Context context, String username, String password) {
//        // handle login
//        dataSource.login(this, context, username, password);
//        onLoginListener = loginViewModel;
//    }
//
//    public void signin(LoginViewModel loginViewModel, Context context, String username, String password) {
//        // handle signin
//        dataSource.signin(this, context, username, password);
//        onLoginListener = loginViewModel;
//    }
//
//    @Override
//    public void onLoginEventComplete(Result<LoggedInUser> result) {
//        if (result instanceof Result.Success) {
//            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
//        }
//        onLoginListener.onLoginEventComplete(result);
//    }
//
//    public void loginWithGoogle(LoginViewModel loginViewModel, Context context, GoogleSignInAccount account) {
//        dataSource.loginWithGoogle(this, context, account);
//        onLoginListener = loginViewModel;
//    }
//
//    public void setGoogleSignInOptions(Context context) {
//        dataSource.setGoogleSignInOptions(this, context);
//    }
//}
