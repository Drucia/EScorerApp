package com.druciak.escorerapp.view.createAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.ICreateAccountMVP;
import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.presenter.CreateAccountPresenter;
import com.druciak.escorerapp.view.mainPanel.MainPanelActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.LOGGED_IN_USER_ID;
import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.USER_ADDITIONAL_INFO_ID;

public class CreateAccountActivity extends AppCompatActivity implements ICreateAccountMVP.IView, View.OnClickListener {

    private ICreateAccountMVP.IPresenter presenter;
    private ImageView imageViewLeft;
    private ImageView imageViewMiddle;
    private ImageView imageViewRight;
    private RadioButton radioButtonReferee;
    private RadioButton radioButtonOrganizer;
    private TextView refereeClassLabel;
    private TextInputLayout refereeCer;
    private Spinner refereeClass;
    private AlertDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        presenter = new CreateAccountPresenter(this);
        imageViewLeft = findViewById(R.id.imageViewLeft);
        imageViewMiddle = findViewById(R.id.imageViewMiddle);
        imageViewRight = findViewById(R.id.imageViewRight);
        radioButtonReferee = findViewById(R.id.radioButtonReferee);
        radioButtonOrganizer = findViewById(R.id.radioButtonOrganizer);
        refereeClassLabel = findViewById(R.id.labelRefereeClass);
        refereeCer = findViewById(R.id.textInputRefereeCer);
        refereeClass = findViewById(R.id.spinnerRefereeClass);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tworzenie konta...");
        builder.setView(getLayoutInflater().inflate(R.layout.pop_up_progress_bar, null));
        builder.setCancelable(false);
        progressBar = builder.create();

        final TextInputLayout nameLayout = findViewById(R.id.textInputName);
        final TextInputLayout surnameLayout = findViewById(R.id.textInputSurname);
        final TextInputLayout emailLayout = findViewById(R.id.textInputEmail);
        final TextInputLayout passwordLayout = findViewById(R.id.textInputPassword);
        final TextInputLayout passwordAgainLayout = findViewById(R.id.textInputPasswordAgain);

        // TODO check if all fields are right fill in

        RadioGroup radioGroup = findViewById(R.id.radioGroupAccount);
        radioGroup.setOnCheckedChangeListener((radioGroup1, id) -> {
            switch(id)
            {
                case R.id.radioButtonReferee:
                    onClick(imageViewLeft);
                    break;
                case R.id.radioButtonOrganizer:
                    onClick(imageViewRight);
                    break;
            }
        });

        MaterialButton button = findViewById(R.id.createAccountLabel);
        button.setOnClickListener(view -> {
            progressBar.show();
            if (radioButtonReferee.isChecked()) {
                presenter.clickedCreateAccount(
                        nameLayout.getEditText().getText().toString(),
                        surnameLayout.getEditText().getText().toString(),
                        emailLayout.getEditText().getText().toString(),
                        passwordLayout.getEditText().getText().toString(),
                        refereeCer.getEditText().getText().toString(),
                        refereeClass.getSelectedItem().toString());
            }
            else {
                presenter.clickedCreateAccount(
                        nameLayout.getEditText().getText().toString(),
                        surnameLayout.getEditText().getText().toString(),
                        emailLayout.getEditText().getText().toString(),
                        passwordLayout.getEditText().getText().toString());
            }
        });

        imageViewLeft.setOnClickListener(this);
        imageViewRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imageViewLeft:
                imageViewLeft.setVisibility(View.INVISIBLE);
                if (imageViewRight.getVisibility() == View.INVISIBLE)
                    imageViewRight.setVisibility(View.VISIBLE);
                imageViewMiddle.setImageDrawable(getResources().getDrawable(R.drawable.referee));
                radioButtonReferee.setChecked(true);
                setVisibilityOfFieldsOn(View.VISIBLE);
                break;
            case R.id.imageViewRight:
                imageViewRight.setVisibility(View.INVISIBLE);
                if (imageViewLeft.getVisibility() == View.INVISIBLE)
                    imageViewLeft.setVisibility(View.VISIBLE);
                imageViewMiddle.setImageDrawable(getResources().getDrawable(R.drawable.boss));
                radioButtonOrganizer.setChecked(true);
                setVisibilityOfFieldsOn(View.GONE);
                break;
        }

        if (imageViewMiddle.getVisibility() == View.INVISIBLE)
            imageViewMiddle.setVisibility(View.VISIBLE);
    }

    private void setVisibilityOfFieldsOn(int visibility)
    {
        refereeClassLabel.setVisibility(visibility);
        refereeClass.setVisibility(visibility);
        refereeCer.setVisibility(visibility);
    }

    @Override
    public void onCreateAccountSuccess(LoggedInUser data) {
        Intent intent = new Intent(this, MainPanelActivity.class);
        intent.putExtra(LOGGED_IN_USER_ID, data);
        intent.putExtra(USER_ADDITIONAL_INFO_ID, true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCreateAccountFailed(String error) {
        progressBar.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
