package com.druciak.escorerapp.view.matchSettings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.entities.Match;
import com.druciak.escorerapp.entities.MatchSettings;
import com.druciak.escorerapp.entities.Player;
import com.druciak.escorerapp.presenter.MatchSettingsPresenter;
import com.druciak.escorerapp.view.DrawActivity;
import com.druciak.escorerapp.view.mainPanel.MainPanelActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.LOGGED_IN_USER_ID;
import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.MATCH_ID;
import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.MATCH_KIND_ID;
import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.USER_ADDITIONAL_INFO_ID;

public class MatchSettingsActivity extends AppCompatActivity implements IMatchSettingsMVP.IView {
    public static final String MACH_SETTINGS_ID = "settings";

    private ViewPager viewPager;
    private IMatchSettingsMVP.IPresenter presenter;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_settings);
        Intent intent = getIntent();
        Match match = intent.getParcelableExtra(MATCH_ID);
        LoggedInUser loggedInUser = intent.getParcelableExtra(LOGGED_IN_USER_ID);
        int matchKind = intent.getIntExtra(MATCH_KIND_ID, -1); // todo
        sectionsPagerAdapter = new SectionsPagerAdapter(this,
                getSupportFragmentManager(), match);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        presenter = new MatchSettingsPresenter(this, match, loggedInUser);
        presenter.preparePlayersOfTeams(match.getHostTeam().getId(), match.getGuestTeam().getId());
    }

    @Override
    public void onPreparePlayerListsEventSucceeded(List<Player> players) {
        sectionsPagerAdapter.setPlayersOfTeams(players);
        sectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPreparePlayerListEventFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addPlayer(Player player) {
        presenter.addPlayer(player);
    }

    @Override
    public void removePlayer(Player player) {
        presenter.removePlayer(player);
    }

    @Override
    public void removeAdditionalMember(String name, int teamId, int memberId) {
        presenter.removeAdditionalMember(name, teamId, memberId);
    }

    @Override
    public void addAdditionalMember(String name, int teamId, int memberId) {
        presenter.addAdditionalMember(name, teamId, memberId);
    }

    @Override
    public void onMatchStartClicked() {
        presenter.onMatchStartClicked();
    }

    @Override
    public void startMatch(MatchSettings matchSettings, LoggedInUser loggedInUser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Przejście do losowania");
        builder.setMessage("Wprowadzone dane nie będą mogły ulec zmianie," +
                " czy chcesz kontynuować?");
        builder.setPositiveButton("TAK", (dialogInterface, i) -> {
            Intent intent = new Intent(this, DrawActivity.class);
            intent.putExtra(MACH_SETTINGS_ID, matchSettings);
            intent.putExtra(LOGGED_IN_USER_ID, loggedInUser);
            MatchSettingsActivity.this.startActivity(intent);
            MatchSettingsActivity.this.finish();
        });
        builder.setNegativeButton("NIE", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    @Override
    public void showPopUpWithErrorMatchSettings(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Błąd");
        View root = getLayoutInflater().inflate(R.layout.pop_up_msg, null);
        TextView msg = root.findViewById(R.id.msg);
        msg.setTextColor(getResources().getColor(R.color.design_default_color_error));
        msg.setText(error);
        builder.setView(root);
        builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    @Override
    public void setMatchSettingsParams(String sTournamentName, String sType, boolean isZas,
                                       String sTown, String sStreet, String sHall,
                                       String sRefereeFirst, String sRefereeSnd, String sLine1,
                                       String sLine2, String sLine3, String sLine4, boolean isMan) {
        presenter.setMatchSettingsParams(sTournamentName, sType, isZas, sTown, sStreet,
                sHall, sRefereeFirst, sRefereeSnd, sLine1, sLine2, sLine3, sLine4, isMan);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.label_attention));
        builder.setMessage(getString(R.string.msg_attention));
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) ->
                presenter.onDiscardClicked());
        builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {});
        builder.create().show();
    }

    @Override
    public void goToMainPanel(LoggedInUser user) {
        Intent intent = new Intent(this, MainPanelActivity.class);
        intent.putExtra(USER_ADDITIONAL_INFO_ID, true);
        intent.putExtra(LOGGED_IN_USER_ID, user);
        startActivity(intent);
        finish();
    }
}