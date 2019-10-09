package com.druciak.escorerapp.view.mainPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.model.interfaces.GameTypesClickCallback;
import com.druciak.escorerapp.presenter.interfaces.IMainPanelPresenter;
import com.druciak.escorerapp.presenter.MainPanelPresenter;
import com.druciak.escorerapp.view.GoodbyeActivity;
import com.druciak.escorerapp.view.login.LoginActivity;
import com.druciak.escorerapp.view.matchSettings.MatchSettingsActivity;
import com.google.android.material.navigation.NavigationView;

public class MainPanelActivity extends AppCompatActivity implements IMainPanelView, GameTypesClickCallback {
    public static final int MATCH_SETTINGS_REQ = 1;

    private AppBarConfiguration mAppBarConfiguration;
    private IMainPanelPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        presenter = new MainPanelPresenter(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_user_data, R.id.nav_matches,
                R.id.nav_tools)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Intent intent = getIntent(); // TODO get loggin in user
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
        Intent intent = new Intent(this, MatchSettingsActivity.class);
        intent.putExtra("kind", gameId);
        startActivityForResult(intent, MATCH_SETTINGS_REQ);
    }

    @Override
    public void onLogoutCompleted() {
        startActivity(new Intent(this, LoginActivity.class));
        Toast.makeText(this, "Nastąpiło wylogowanie", Toast.LENGTH_LONG).show();
        finish();
    }
}
