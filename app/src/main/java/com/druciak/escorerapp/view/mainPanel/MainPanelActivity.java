package com.druciak.escorerapp.view.mainPanel;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.entities.Match;
import com.druciak.escorerapp.entities.MatchSummary;
import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.presenter.GameTypesRepository;
import com.druciak.escorerapp.presenter.MainPanelPresenter;
import com.druciak.escorerapp.view.GoodbyeActivity;
import com.druciak.escorerapp.view.login.LoginActivity;
import com.druciak.escorerapp.view.mainPanel.home.HomeFragment;
import com.druciak.escorerapp.view.mainPanel.matches.MatchFragment;
import com.druciak.escorerapp.view.mainPanel.teams.TeamFragment;
import com.druciak.escorerapp.view.mainPanel.tools.ToolsFragment;
import com.druciak.escorerapp.view.mainPanel.userData.UserDataFragment;
import com.druciak.escorerapp.view.matchSettings.MatchSettingsActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.stream.Collectors;

public class MainPanelActivity extends AppCompatActivity implements IMainPanelMVP.IView {
    private static final int STORAGE_PERMISSION_CODE = 100;

    public static final String MATCH_KIND_ID = "kind";
    public static final String MATCH_ID = "match";
    public static final String LOGGED_IN_USER_ID = "user";
    public static final String USER_ADDITIONAL_INFO_ID = "additional_info";

    private AppBarConfiguration mAppBarConfiguration;
    private IMainPanelMVP.IPresenter presenter;
    private DrawerLayout drawer;
    private TextView fullName;
    private TextView email;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showConfirmDialog();
        }
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.label_attention);
        builder.setMessage(R.string.out_confirm);
        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> super.onBackPressed());
        builder.setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel());
        builder.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        fullName = headerView.findViewById(R.id.userName);
        email = headerView.findViewById(R.id.userEmail);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.nav_app_bar_open_drawer_description,
                R.string.nav_app_bar_navigate_up_description);
        drawer.addDrawerListener(toogle);
        toogle.syncState();

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new HomeFragment()).commit();
                    break;
                case R.id.nav_matches:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new MatchFragment()).commit();
                    break;
                case R.id.nav_teams:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new TeamFragment()).commit();
                    break;
                case R.id.nav_tools:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new ToolsFragment()).commit();
                    break;
                case R.id.nav_user_data:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new UserDataFragment()).commit();
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    STORAGE_PERMISSION_CODE);
        }

        presenter = new MainPanelPresenter(this);
        Intent intent = getIntent();
        boolean isAdditionalInfo = intent.getBooleanExtra(USER_ADDITIONAL_INFO_ID, false);
        if (!isAdditionalInfo)
            showPopUpWithSetUserFields();
        else {
            LoggedInUser user = intent.getParcelableExtra(LOGGED_IN_USER_ID);
            presenter.setLoggedInUser(user);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // todo
            }
            else {
                Toast.makeText(this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
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
        switch (gameId)
        {
            case GameTypesRepository.DZPS_VOLLEYBALL_ID:
                presenter.clickOnDZPSMatch();
                break;
            case GameTypesRepository.VOLLEYBALL_ID:
                presenter.clickOnVolleyballMatch();
                break;
        }
    }

    @Override
    public void onLogoutCompleted() {
        startActivity(new Intent(this, LoginActivity.class));
        Toast.makeText(this, "Nastąpiło wylogowanie", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void showErrorMsgAndFinish(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void setLoggedInUserFields(String fullName, String email) {
        this.fullName.setText(fullName);
        this.email.setText(email);
    }

    private void showPopUpWithSetUserFields() {
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
    public void showPopUpWithMatchToChoose(List<Match> matches, LoggedInUser user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wybierz mecz");
        List<String> names = matches.stream().map(Match::getName).collect(Collectors.toList());
        CharSequence[] items = names.toArray(new CharSequence[names.size()]);
        builder.setItems(items, (dialogInterface, i) -> {
            Intent intent = new Intent(this, MatchSettingsActivity.class);
            intent.putExtra(MATCH_KIND_ID, GameTypesRepository.DZPS_VOLLEYBALL_ID);
            intent.putExtra(MATCH_ID, matches.get(i));
            intent.putExtra(LOGGED_IN_USER_ID, user);
            startActivity(intent);
            finish();
        });
        builder.create().show();
    }

    @Override
    public boolean isRefereeUser() {
        return presenter.isRefereeUser();
    }

    @Override
    public String getUserId() {
        return presenter.getUserId();
    }

    @Override
    public void goToMatchSettings(LoggedInUser loggedInUser) {
        Intent intent = new Intent(this, MatchSettingsActivity.class);
        intent.putExtra(MATCH_KIND_ID, GameTypesRepository.VOLLEYBALL_ID);
        intent.putExtra(LOGGED_IN_USER_ID, loggedInUser);
        startActivity(intent);
        finish();
    }

    @Override
    public void goToDetails(Intent intent, String transitionName, MatchSummary matchSummary, View foreground) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                foreground,
                getString(R.string.transition)
        );
        startActivity(intent, optionsCompat.toBundle());
    }
}
