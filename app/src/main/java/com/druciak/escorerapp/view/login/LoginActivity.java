package com.druciak.escorerapp.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.ILoginMVP;
import com.druciak.escorerapp.model.entities.LoggedInUser;
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

public class LoginActivity extends AppCompatActivity implements ILoginMVP.IView {

    private static final int SIGN_IN_BY_GOOGLE_REQ = 0;

    private ILoginMVP.IPresenter presenter;

    private MaterialButton loginButton;
    private TextInputLayout usernameEditTextLayout;
    private TextInputLayout passwordEditTextLayout;

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
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                presenter.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        forgotPassword.setOnClickListener(new TextView.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                //TODO make popup with reset password
//                loginViewModel.forgotPassword(LoginActivity.this, usernameEditText.getEditText().getText().toString(),
//                        passwordEditText.getEditText().getText().toString());
            }
        });

        createAccount.setOnClickListener(new TextView.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        View.OnClickListener googleLoginListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.clickedLoginWithGoogle(LoginActivity.this);
                loadingProgressBar.setVisibility(View.VISIBLE);
            }
        };

        googleText.setOnClickListener(googleLoginListener);
        googleImage.setOnClickListener(googleLoginListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == SIGN_IN_BY_GOOGLE_REQ) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                presenter.loginWithGoogle(account);
            } catch (ApiException e) {

            }
        }
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
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
    public void onLoginEventCompleteSuccessfully(LoggedInUser data) {
        Intent intent = new Intent(this, MainPanelActivity.class);
        intent.putExtra("user", data);
        startActivity(intent);
        finish();
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
    protected void onStart() {
        super.onStart();
        presenter.activityIsStarted();
    }
}
