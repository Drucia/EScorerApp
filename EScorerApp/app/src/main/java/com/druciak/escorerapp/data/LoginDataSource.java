package com.druciak.escorerapp.data;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.druciak.escorerapp.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.concurrent.Executor;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private boolean isLogged = false;
    private boolean isSignedIn = false;

    public Result<LoggedInUser> login(Context context, String username, String password) {

        userAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            isLogged = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            isLogged = false;
                        }
                    }
                });

        return isLogged ? new Result.Success<>(new LoggedInUser(userAuth.getCurrentUser()))
                : new Result.Error(new IOException("Error logging in"));
    }

    public Result<LoggedInUser> signin(Context context, String username, String password) {

        userAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            isSignedIn = isLogged = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            isSignedIn = isLogged = false;
                        }
                    }
                });

        return isSignedIn ? new Result.Success<>(new LoggedInUser(userAuth.getCurrentUser()))
                : new Result.Error(new IOException("Error signing in"));
    }

    public void logout() {
        userAuth.signOut();
    }
}
