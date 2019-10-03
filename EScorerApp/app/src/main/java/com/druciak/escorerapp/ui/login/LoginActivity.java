package com.druciak.escorerapp.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.activities.CreateAccountActivity;
import com.druciak.escorerapp.activities.MainPanelActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final TextInputLayout usernameEditTextLayout = findViewById(R.id.usernameLayout);
        final TextInputLayout passwordEditTextLayout = findViewById(R.id.passwordLayout);
        final TextInputEditText usernameEditText = findViewById(R.id.usernameInput);
        final TextInputEditText passwordEditText = findViewById(R.id.passwordInput);
        final MaterialButton loginButton = findViewById(R.id.login);
        final TextView forgotPassword = findViewById(R.id.forgotPassword);
        final TextView createAccount = findViewById(R.id.createAccount);
        final TextView googleText = findViewById(R.id.googleText);
        final ImageView googleImage = findViewById(R.id.googleImage);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
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
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());

                    //Complete and destroy login activity once successful
                    finish();
                }
                setResult(Activity.RESULT_OK);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(LoginActivity.this, usernameEditText.getText().toString(),
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
                // TODO google login
            }
        };

        googleText.setOnClickListener(googleLoginListener);
        googleImage.setOnClickListener(googleLoginListener);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        Intent intent = new Intent(this, MainPanelActivity.class);
        intent.putExtra("user", model);
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginViewModel.isLoggedIn();
    }
}
