package com.druciak.escorerapp.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final TextInputLayout usernameEditText = findViewById(R.id.username);
        final TextInputLayout passwordEditText = findViewById(R.id.password);
        final MaterialButton loginButton = findViewById(R.id.login);
        final TextView forgotPassword = findViewById(R.id.forgotPassword);
        final TextView createAccount = findViewById(R.id.createAccount);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
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
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

//        TextWatcher afterTextChangedListener = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // ignore
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // ignore
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                loginViewModel.loginDataChanged(usernameEditText.getEditText().getText().toString(),
//                        passwordEditText.getEditText().getText().toString());
//            }
//        };
//        usernameEditText.getEditText().addTextChangedListener(afterTextChangedListener);
//        passwordEditText.getEditText().addTextChangedListener(afterTextChangedListener);
        passwordEditText.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(LoginActivity.this, usernameEditText.getEditText().getText().toString(),
                            passwordEditText.getEditText().getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(LoginActivity.this, usernameEditText.getEditText().getText().toString(),
                        passwordEditText.getEditText().getText().toString());
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
