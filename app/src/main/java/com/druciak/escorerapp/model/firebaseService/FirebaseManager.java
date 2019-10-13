package com.druciak.escorerapp.model.firebaseService;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.ICreateAccountMVP;
import com.druciak.escorerapp.interfaces.ILoginMVP;
import com.druciak.escorerapp.model.entities.LoggedInUser;
import com.druciak.escorerapp.model.entities.NewUser;
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
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirebaseManager implements ILoginMVP.IModel, ICreateAccountMVP.IModel {
    private ILoginMVP.IPresenter loginPresenter;
    private ICreateAccountMVP.IPresenter createPresenter;
    private FirebaseAuth userAuth;
    private GoogleSignInClient mGoogleSignInClient;

    public FirebaseManager(ILoginMVP.IPresenter loginPresenter)
    {
        this.loginPresenter = loginPresenter;
        userAuth = FirebaseAuth.getInstance();
    }

    public FirebaseManager(ICreateAccountMVP.IPresenter createPresenter)
    {
        this.createPresenter = createPresenter;
        userAuth = FirebaseAuth.getInstance();
    }

    public FirebaseManager()
    {
        loginPresenter = null;
        userAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void initializeGoogleService(Context context){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    @Override
    public void login(String username, String password)
    {
        userAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

    @Override
    public void signIn(final NewUser newUser)
    {
        userAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = userAuth.getCurrentUser();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(user.getUid())
                                    .setValue(newUser);
                            createPresenter.onCreateAccountEventComplete(new Result.Success<>(new LoggedInUser(user)));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            createPresenter.onCreateAccountEventComplete(new Result.Error(new IOException("Error logging in")));
                        }
                    }
                });
    }

    @Override
    public Result<LoggedInUser> getLoggedIn()
    {
        FirebaseUser user = userAuth.getCurrentUser();

        if (user != null)
            return new Result.Success<>(new LoggedInUser(user));
        else
            return new Result.Error(new IOException("No user log in"));
    }

    @Override
    public void logout() {
        userAuth.signOut();
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut();
            mGoogleSignInClient.revokeAccess();
        }
    }

    @Override
    public void loginWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        userAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

    @Override
    public GoogleSignInClient getGoogleClient() {
        return mGoogleSignInClient;
    }
}
