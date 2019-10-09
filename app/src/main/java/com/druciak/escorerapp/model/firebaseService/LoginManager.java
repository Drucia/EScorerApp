package com.druciak.escorerapp.model.firebaseService;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.model.login.LoggedInUser;
import com.druciak.escorerapp.presenter.interfaces.ILoginPresenterForManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.IOException;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LoginManager {
    private final ILoginPresenterForManager loginPresenter;
    private FirebaseAuth userAuth;
    private GoogleSignInClient mGoogleSignInClient;

    public LoginManager(ILoginPresenterForManager loginPresenter)
    {
        this.loginPresenter = loginPresenter;
        userAuth = FirebaseAuth.getInstance();
    }

    public LoginManager()
    {
        loginPresenter = null;
        userAuth = FirebaseAuth.getInstance();
    }

    public void initializeGoogleService(Context context){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void login(Context context, String username, String password)
    {
        userAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = userAuth.getCurrentUser();
                            loginPresenter.onLoginEventComplete(new Result.Success<>(new LoggedInUser(user)));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            loginPresenter.onLoginEventComplete(new Result.Error(new IOException("Error logging in")));
                        }
                    }
                });
    }

    public void signin(Context context, String username, String password)
    {
        userAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = userAuth.getCurrentUser();
                            loginPresenter.onLoginEventComplete(new Result.Success<>(new LoggedInUser(user)));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            loginPresenter.onLoginEventComplete(new Result.Error(new IOException("Error logging in")));
                        }
                    }
                });
    }

    public Result<LoggedInUser> getLoggedIn()
    {
        FirebaseUser user = userAuth.getCurrentUser();

        if (user != null)
            return new Result.Success<>(new LoggedInUser(user));
        else
            return new Result.Error(new IOException("No user log in"));
    }

    public void logout() {
        userAuth.signOut();
    }

    public void loginWithGoogle(Context context, GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        userAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithGoogle:success");
                            FirebaseUser user = userAuth.getCurrentUser();
                            loginPresenter.onLoginEventComplete(new Result.Success<>(new LoggedInUser(user)));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            loginPresenter.onLoginEventComplete(new Result.Error(new IOException("Error logging in")));
                        }
                    }
                });
    }

    public GoogleSignInClient getGoogleClient() {
        return mGoogleSignInClient;
    }
}
