package com.druciak.escorerapp.data;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.data.model.LoggedInUser;
import com.druciak.escorerapp.ui.interfaces.OnLoginListener;
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

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private FirebaseAuth userAuth = FirebaseAuth.getInstance();

    public void login(final LoginRepository loginRepository, Context context, String username, String password)
    {
        userAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = userAuth.getCurrentUser();
                            ((OnLoginListener) loginRepository).onLoginEventComplete(new Result.Success<>(new LoggedInUser(user)));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            ((OnLoginListener) loginRepository).onLoginEventComplete(new Result.Error(new IOException("Error logging in")));
                        }
                    }
                });
    }

    public void signin(final LoginRepository loginRepository, Context context, String username, String password)
    {
        userAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = userAuth.getCurrentUser();
                            ((OnLoginListener) loginRepository).onLoginEventComplete(new Result.Success<>(new LoggedInUser(user)));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            ((OnLoginListener) loginRepository).onLoginEventComplete(new Result.Error(new IOException("Error logging in")));
                        }
                    }
                });
    }

    public Result<LoggedInUser> isLoggedIn()
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

    public void loginWithGoogle(final LoginRepository loginRepository, Context context, GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        userAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithGoogle:success");
                            FirebaseUser user = userAuth.getCurrentUser();
                            ((OnLoginListener) loginRepository).onLoginEventComplete(new Result.Success<>(new LoggedInUser(user)));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            ((OnLoginListener) loginRepository).onLoginEventComplete(new Result.Error(new IOException("Error logging in")));
                        }
                    }
                });
    }

    public void setGoogleSignInOptions(final LoginRepository loginRepository, Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        loginRepository.setGoogleSignInClient(mGoogleSignInClient);
    }
}
