package com.druciak.escorerapp.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.interfaces.ILoginMVP;
import com.druciak.escorerapp.presenter.LoginFormState;
import com.druciak.escorerapp.presenter.LoginPresenter;
import com.druciak.escorerapp.view.createAccount.CreateAccountActivity;
import com.druciak.escorerapp.view.mainPanel.MainPanelActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.LOGGED_IN_USER_ID;
import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.USER_ADDITIONAL_INFO_ID;

public class LoginActivity extends AppCompatActivity implements ILoginMVP.IView {

    private static final int SIGN_IN_BY_GOOGLE_REQ = 0;

    private ILoginMVP.IPresenter presenter;

    private MaterialButton loginButton;
    private TextInputLayout usernameEditTextLayout;
    private TextInputLayout passwordEditTextLayout;
    private AlertDialog loginProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(this);

        usernameEditTextLayout = findViewById(R.id.usernameLayout);
        passwordEditTextLayout = findViewById(R.id.passwordLayout);
        final TextInputEditText usernameEditText = findViewById(R.id.usernameInput);
        final TextInputEditText passwordEditText = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.login);
        final TextView forgotPassword = findViewById(R.id.forgotPassword);
        final TextView createAccount = findViewById(R.id.createAccount);
        final TextView googleText = findViewById(R.id.googleText);
        final ImageView googleImage = findViewById(R.id.googleImage);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logowanie...");
        builder.setView(getLayoutInflater().inflate(R.layout.pop_up_progress_bar, null));
        builder.setCancelable(false);
        loginProgress = builder.create();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.isDataValid(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);

        loginButton.setOnClickListener(v -> {
            loginProgress.show();
            presenter.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });

        forgotPassword.setOnClickListener(view -> {
            //TODO make popup with reset password
//                loginViewModel.forgotPassword(LoginActivity.this, usernameEditText.getEditText().getText().toString(),
//                        passwordEditText.getEditText().getText().toString());
        });

        createAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });

        View.OnClickListener googleLoginListener = view -> {
            presenter.clickedLoginWithGoogle(LoginActivity.this);
            loginProgress.show();
        };

        googleText.setOnClickListener(googleLoginListener);
        googleImage.setOnClickListener(googleLoginListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_BY_GOOGLE_REQ) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                presenter.loginWithGoogle(account);
            } catch (ApiException e) {
                // todo do sth
            }
        }
    }

    private void showLoginFailed(String errorString) {
        loginProgress.dismiss();
        makeErrorAlert(errorString).setPositiveButton(getString(R.string.ok),
                (dialogInterface, i) -> {}).create().show();
    }

    @Override
    public void onDataValidChecked(LoginFormState loginFormState) {
        loginButton.setEnabled(loginFormState.isDataValid());
        if (loginFormState.getUsernameError() != null) {
            usernameEditTextLayout.setError(getString(loginFormState.getUsernameError()));
        } else
        {
            usernameEditTextLayout.setError(null);
        }
        if (loginFormState.getPasswordError() != null) {
            passwordEditTextLayout.setError(getString(loginFormState.getPasswordError()));
        }
        else
        {
            passwordEditTextLayout.setError(null);
        }
    }

    @Override
    public void onLoginEventCompleteSuccessfully(FirebaseUser data) {
        loginProgress.show();
        presenter.getUserAdditionalInfo(data);
    }

    @Override
    public void onLoginEventCompleteError(Exception error) {
        showLoginFailed(error.getMessage());
    }

    @Override
    public void startGoogleLoginPopUp(Intent signInIntent) {
        startActivityForResult(signInIntent, SIGN_IN_BY_GOOGLE_REQ);
    }

    @Override
    public void onGetUserLoggedInEventCompleteSuccess(FirebaseUser loggedInUser) {
        Intent intent = new Intent(this, MainPanelActivity.class);
        intent.putExtra(USER_ADDITIONAL_INFO_ID, false);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.activityIsStarted();
    }

    @Override
    public void onGetUserAdditionalInfoEventFailure(String message) {
        loginProgress.dismiss();
        AlertDialog.Builder dialog = makeErrorAlert(message);
        dialog.setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> finish());
        dialog.create().show();
    }

    @Override
    public void onGetUserAdditionalInfoEventSuccess(LoggedInUser loggedInUser) {
        Intent intent = new Intent(this, MainPanelActivity.class);
        intent.putExtra(LOGGED_IN_USER_ID, loggedInUser);
        intent.putExtra(USER_ADDITIONAL_INFO_ID, true);
        startActivity(intent);
        finish();
    }

    private AlertDialog.Builder makeErrorAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.label_error));
        builder.setMessage(message);
        return builder;
    }
}
