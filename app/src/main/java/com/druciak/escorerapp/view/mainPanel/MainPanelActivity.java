package com.druciak.escorerapp.view.mainPanel;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.model.entities.Match;
import com.druciak.escorerapp.presenter.GameTypesRepository;
import com.druciak.escorerapp.presenter.MainPanelPresenter;
import com.druciak.escorerapp.view.GoodbyeActivity;
import com.druciak.escorerapp.view.login.LoginActivity;
import com.druciak.escorerapp.view.matchSettings.MatchSettingsActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.stream.Collectors;

public class MainPanelActivity extends AppCompatActivity implements IMainPanelMVP.IView {
    private AppBarConfiguration mAppBarConfiguration;
    private IMainPanelMVP.IPresenter presenter;
    private TextView fullName;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_user_data, R.id.nav_matches,
                R.id.nav_tools)
                .setDrawerLayout(drawer)
                .build();
        fullName = headerView.findViewById(R.id.userName);
        email = headerView.findViewById(R.id.userEmail);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        presenter = new MainPanelPresenter(this);
        presenter.createdActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_panel, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_logout:
                logoutUser();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {
        presenter.logout();
        startActivity(new Intent(this, LoginActivity.class));
        Toast.makeText(this, "Nastąpiło wylogowanie", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onClickedExit(MenuItem item) {
        startActivity(new Intent(this, GoodbyeActivity.class));
        finish();
    }

    @Override
    public void onClick(int gameId) {
        if (gameId == GameTypesRepository.DZPS_VOLLEYBALL_ID) {
            presenter.clickOnDZPSMatch();
        }
    }

    @Override
    public void onLogoutCompleted() {
        startActivity(new Intent(this, LoginActivity.class));
        Toast.makeText(this, "Nastąpiło wylogowanie", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void setLoggedInUserFields(String fullName, String email) {
        this.fullName.setText(fullName);
        this.email.setText(email);
    }

    @Override
    public void showPopUpWithSetUserFields() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.pop_up_user_fields, null);
        dialogBuilder.setView(view);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("Uzupełnij dane konta");
        final RadioButton radioButtonReferee = view.findViewById(R.id.radioButtonRefereePopUp);
        final TextInputLayout refereeCer = view.findViewById(R.id.textInputRefereeCerPopUp);
        final Spinner refereeClass = view.findViewById(R.id.spinnerRefereeClassPopUp);
        final TextView refereeClassLabel = view.findViewById(R.id.labelRefereeClassPopUp);

        final TextInputLayout nameLayout = view.findViewById(R.id.textInputNamePopUp);
        final TextInputLayout surnameLayout = view.findViewById(R.id.textInputSurnamePopUp);

        RadioGroup radioGroup = view.findViewById(R.id.radioGroupAccountPopUp);
        radioGroup.setOnCheckedChangeListener((radioGroup1, id) -> {
            switch(id)
            {
                case R.id.radioButtonRefereePopUp:
                    refereeClassLabel.setVisibility(View.VISIBLE);
                    refereeClass.setVisibility(View.VISIBLE);
                    refereeCer.setVisibility(View.VISIBLE);
                    break;
                case R.id.radioButtonOrganizerPopUp:
                    refereeClassLabel.setVisibility(View.GONE);
                    refereeClass.setVisibility(View.GONE);
                    refereeCer.setVisibility(View.GONE);
                    break;
            }
        });
        dialogBuilder.setPositiveButton("Zatwierdź", (dialogInterface, i) -> {
            if (radioButtonReferee.isChecked()) {
                presenter.onCustomUserFieldsSaveClicked(
                        nameLayout.getEditText().getText().toString(),
                        surnameLayout.getEditText().getText().toString(),
                        refereeCer.getEditText().getText().toString(),
                        refereeClass.getSelectedItem().toString());
            }
            else {
                presenter.onCustomUserFieldsSaveClicked(
                        nameLayout.getEditText().getText().toString(),
                        surnameLayout.getEditText().getText().toString());
            }
            dialogInterface.dismiss();
        });

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void showPopUpWithMatchToChoose(List<Match> matches) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wybierz mecz");
        List<String> names = matches.stream().map(Match::getName).collect(Collectors.toList());
        CharSequence[] items = names.toArray(new CharSequence[names.size()]);
        builder.setItems(items, (dialogInterface, i) -> {
            Intent intent = new Intent(this, MatchSettingsActivity.class);
            intent.putExtra("kind", GameTypesRepository.DZPS_VOLLEYBALL_ID);
            intent.putExtra("match", matches.get(i));
            startActivity(intent);
        });
        builder.create().show();
    }
}
