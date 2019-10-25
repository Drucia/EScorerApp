package com.druciak.escorerapp.model.firebaseService;

import android.content.Context;
import android.util.Log;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.ICreateAccountMVP;
import com.druciak.escorerapp.interfaces.ILoginMVP;
import com.druciak.escorerapp.model.entities.NewUser;
import com.druciak.escorerapp.model.internalApiService.InternalApiManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.IOException;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirebaseManager implements ILoginMVP.IModel, ICreateAccountMVP.IFirebaseModel{
    private ILoginMVP.IPresenter loginPresenter;
    private ICreateAccountMVP.IPresenter createPresenter;
    private static final FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private static String test = "wspólne";
    private GoogleSignInClient mGoogleSignInClient;
    private InternalApiManager internalApiManager;

    public FirebaseManager() {
        //userAuth = FirebaseAuth.getInstance();

    }

    public FirebaseManager(ILoginMVP.IPresenter loginPresenter)
    {
        this.loginPresenter = loginPresenter;
        //userAuth = FirebaseAuth.getInstance();
    }

    public FirebaseManager(ICreateAccountMVP.IPresenter createPresenter)
    {
        this.createPresenter = createPresenter;
        //userAuth = FirebaseAuth.getInstance();
        internalApiManager = new InternalApiManager(createPresenter);
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
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = userAuth.getCurrentUser();
                        loginPresenter.onLoginEventComplete(new Result.Success<>(user));
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        loginPresenter.onLoginEventComplete(new Result.Error(new IOException("Error logging in")));
                    }
                });
    }

    @Override
    public void signIn(final String name, final String surname, final String email, String password,
                       final String certificate, final String class_)
    {
        userAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        final FirebaseUser firebaseUser = userAuth.getCurrentUser();
                        NewUser user = new NewUser(firebaseUser.getUid(), name,
                            surname, certificate, class_);
                        internalApiManager.createUser(user, email);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        createPresenter.onCreateAccountEventComplete(new Result.Error(new IOException("Bład logowania")));
                    }
                });
    }

    @Override
    public void signIn(final String name, final String surname, final String email, String password)
    {
        userAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        final FirebaseUser firebaseUser = userAuth.getCurrentUser();
                        NewUser user = new NewUser(firebaseUser.getUid(), name, surname);
                        internalApiManager.createUser(user, email);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        createPresenter.onCreateAccountEventComplete(new Result.Error(new IOException("Bład logowania")));
                    }
                });
    }

    @Override
    public Result<FirebaseUser> getLoggedIn()
    {
        FirebaseUser user = userAuth.getCurrentUser();

        if (user != null) {
            return new Result.Success<>(user);
        }
        else
            return new Result.Error(new IOException("No user log in"));
    }

    @Override
    public void logout() {
        userAuth.signOut();
        if (mGoogleSignInClient != null )
            mGoogleSignInClient.signOut();
        FirebaseUser user = userAuth.getCurrentUser();
        String p = "dupa";
    }

    @Override
    public void loginWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        userAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithGoogle:success");
                        FirebaseUser user = userAuth.getCurrentUser();
                        loginPresenter.onLoginEventComplete(new Result.Success<>(user));
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        loginPresenter.onLoginEventComplete(new Result.Error(new IOException("Bład logowania")));
                    }
                });
    }

    @Override
    public GoogleSignInClient getGoogleClient() {
        return mGoogleSignInClient;
    }
}
