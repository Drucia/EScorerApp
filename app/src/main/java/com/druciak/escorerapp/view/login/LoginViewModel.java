//package com.druciak.escorerapp.view.login;
//
//import android.content.Context;
//import android.util.Patterns;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.druciak.escorerapp.R;
//import com.druciak.escorerapp.service.login.LoginRepository;
//import com.druciak.escorerapp.model.service.Result;
//import com.druciak.escorerapp.model.login.LoggedInUser;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//
//public class LoginViewModel extends ViewModel implements ILoginView {
//
//    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
//    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
//    private LoginRepository loginRepository;
//
//    LoginViewModel(LoginRepository loginRepository) {
//        this.loginRepository = loginRepository;
//    }
//
//    LiveData<LoginFormState> getLoginFormState() {
//        return loginFormState;
//    }
//
//    LiveData<LoginResult> getLoginResult() {
//        return loginResult;
//    }
//
//    public void login(Context context, String username, String password) {
//        loginRepository.login(this, context, username, password);
//    }
//
//    public void signin(Context context, String username, String password) {
//        loginRepository.signin(this, context, username, password);
//    }
//
//    public void loginDataChanged(String username, String password) {
//        if (!isUserNameValid(username)) {
//            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
//        } else if (!isPasswordValid(password)) {
//            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
//        }
//        else {
//            loginFormState.setValue(new LoginFormState(true));
//        }
//    }
//
//    public boolean isLoggedIn()
//    {
//        return loginRepository.isLoggedIn();
//    }
//
//    public void logout()
//    {
//        loginRepository.logout();
//    }
//
//    // A placeholder username validation check
//    private boolean isUserNameValid(String username) {
//        if (username == null) {
//            return false;
//        }
//        if (username.contains("@")) {
//            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
//        } else {
//            return !username.trim().isEmpty();
//        }
//    }
//
//    // A placeholder password validation check
//    private boolean isPasswordValid(String password) {
//        return password != null && password.trim().length() > 5;
//    }
//
//    @Override
//    public void onLoginEventComplete(Result<LoggedInUser> result) {
//        if (result instanceof Result.Success) {
//            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data)));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }
//    }
//
//    public void loginByGoogle(Context context, GoogleSignInAccount account) {
//        loginRepository.loginWithGoogle(this, context, account);
//    }
//
//    public GoogleSignInClient getGoogleSignInClient() {
//        return loginRepository.getGoogleSignInClient();
//    }
//
//    public void setGoogleSignInOptions(Context context) {
//        loginRepository.setGoogleSignInOptions(context);
//    }
//}
