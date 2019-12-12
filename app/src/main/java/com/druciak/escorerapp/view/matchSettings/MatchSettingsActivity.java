package com.druciak.escorerapp.view.matchSettings;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.entities.Match;
import com.druciak.escorerapp.entities.MatchSettings;
import com.druciak.escorerapp.entities.Player;
import com.druciak.escorerapp.entities.Team;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.interfaces.ITeamCallback;
import com.druciak.escorerapp.presenter.GameTypesRepository;
import com.druciak.escorerapp.presenter.MatchSettingsPresenter;
import com.druciak.escorerapp.view.DrawActivity;
import com.druciak.escorerapp.view.mainPanel.MainPanelActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.LOGGED_IN_USER_ID;
import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.MATCH_ID;
import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.MATCH_KIND_ID;
import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.USER_ADDITIONAL_INFO_ID;

public class MatchSettingsActivity extends AppCompatActivity implements IMatchSettingsMVP.IView,
        ITeamCallback {
    public static final String MACH_SETTINGS_ID = "settings";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabLayoutAdapter tabLayoutAdapter;
    private IMatchSettingsMVP.IPresenter presenter;
    private SectionsPagerAdapter sectionsPagerAdapter;

    private boolean firstShowGuest;
    private TextView noItems;
    private AlertDialog teamsDialog;
    private RecyclerView teamsRecyclerView;
    private TeamsAdapter teamsAdapter;
    private boolean isHomeTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_settings);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        int matchKind = intent.getIntExtra(MATCH_KIND_ID, -1);
        Match match = intent.getParcelableExtra(MATCH_ID);
        LoggedInUser loggedInUser = intent.getParcelableExtra(LOGGED_IN_USER_ID);

        if (matchKind == GameTypesRepository.DZPS_VOLLEYBALL_ID) {
            presenter = new MatchSettingsPresenter(this, match, loggedInUser, false);
            sectionsPagerAdapter = new SectionsPagerAdapter(this,
                    getSupportFragmentManager(), match, presenter.getMatchSettings());
            presenter.preparePlayersOfTeams(match.getHostTeam().getId(),
                    match.getGuestTeam().getId());
        }
        else {
            Match newMatch = new Match(new Team(-1), new Team(-2), loggedInUser.getUserId());
            presenter = new MatchSettingsPresenter(this, newMatch, loggedInUser, true);
            sectionsPagerAdapter = new SectionsPagerAdapter(this,
                    getSupportFragmentManager(), newMatch, matchKind,
                    presenter.getMatchSettings());
            firstShowGuest = true;
            presenter.prepareTeams();
            showPopUpWithChooseTeam(true);
        }
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
                if (position == 1 && firstShowGuest)
                {
                    showPopUpWithChooseTeam(false);
                    firstShowGuest = false;
                }
                sectionsPagerAdapter.saveData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findViewById(R.id.backArrow).setOnClickListener(view -> showConfirmBack());
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayoutAdapter = new TabLayoutAdapter(this);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(tabLayoutAdapter.getTabView(i));
        }
        highLightCurrentTab(0);
    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(tabLayoutAdapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(tabLayoutAdapter.getSelectedTabView(position));
    }

    private void showPopUpWithChooseTeam(boolean isHomeTeam) {
        this.isHomeTeam = isHomeTeam;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View teamsPopUpLayout = getLayoutInflater().inflate(R.layout.pop_up_teams, null);
        teamsRecyclerView = teamsPopUpLayout.findViewById(R.id.teamsRecycler);
        noItems = teamsPopUpLayout.findViewById(R.id.noItems);
        Optional<ArrayList<Team>> teams = presenter.getTeamsList();
        if (teams.isPresent()){
            teamsAdapter = new TeamsAdapter(this, teams.get(), isHomeTeam);
            teamsRecyclerView.setAdapter(teamsAdapter);
            if (teams.get().isEmpty())
                noItems.setVisibility(View.VISIBLE);
            else
                noItems.setVisibility(View.GONE);
        }
        teamsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ExtendedFloatingActionButton fab = teamsPopUpLayout.findViewById(R.id.buttonNew);
        builder.setTitle("Wybierz swoją drużynę");
        builder.setCancelable(false);
        builder.setView(teamsPopUpLayout);
        teamsDialog = builder.create();
        fab.setOnClickListener(view -> teamsDialog.dismiss());
        teamsDialog.show();
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
    public void startMatch(MatchSettings matchSettings, LoggedInUser loggedInUser,
                           boolean isSimplyMatch) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.label_go_to_draw));
        builder.setMessage(getString(R.string.msg_no_more_change));
        builder.setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
            Intent intent = new Intent(this, DrawActivity.class);
            intent.putExtra(MATCH_KIND_ID, isSimplyMatch);
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
        builder.setTitle(getString(R.string.label_error));
        View root = getLayoutInflater().inflate(R.layout.pop_up_msg, null);
        TextView msg = root.findViewById(R.id.msg);
        msg.setTextColor(getResources().getColor(R.color.design_default_color_error));
        msg.setText(error);
        builder.setView(root);
        builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    @Override
    public void setMatchSettingsParams(String sTournamentName, String sType, boolean isFin,
                                       String sTown, String sStreet, String sHall,
                                       String sRefereeFirst, String sRefereeSnd, String sLine1,
                                       String sLine2, String sLine3, String sLine4, boolean isMan) {
        presenter.setMatchSettingsParams(sTournamentName, sType, isFin, sTown, sStreet,
                sHall, sRefereeFirst, sRefereeSnd, sLine1, sLine2, sLine3, sLine4, isMan);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            showConfirmBack();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void goToMainPanel(LoggedInUser user) {
        Intent intent = new Intent(this, MainPanelActivity.class);
        intent.putExtra(USER_ADDITIONAL_INFO_ID, true);
        intent.putExtra(LOGGED_IN_USER_ID, user);
        startActivity(intent);
        finish();
    }

    @Override
    public void updateTeamName(String name, int teamId) {
        presenter.updateTeamName(name, teamId);
    }

    @Override
    public void updateTeamsInPopUp(ArrayList<Team> teams) {
        if (teamsDialog != null && teamsDialog.isShowing()){
            teamsAdapter = new TeamsAdapter(this, teams, isHomeTeam);
            teamsRecyclerView.setAdapter(teamsAdapter);
            if (teams.isEmpty())
                noItems.setVisibility(View.VISIBLE);
            else
                noItems.setVisibility(View.GONE);
        }
    }

    @Override
    public void startMatchWithSaveDataOnServer(MatchSettings matchSettings, LoggedInUser loggedInUser,
                                               boolean isSimplyMatch) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.label_go_to_draw));
        View root = getLayoutInflater().inflate(R.layout.pop_up_save_data_on_server, null);
        ((TextView) root.findViewById(R.id.msg)).setText(getString(R.string.msg_no_more_change));
        CheckBox home = root.findViewById(R.id.checkBoxHome);
        CheckBox guest = root.findViewById(R.id.checkBoxGuest);
        CheckBox conf = root.findViewById(R.id.checkBoxConf);
        builder.setView(root);
        builder.setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
            presenter.saveDataOnServer(home.isChecked(), guest.isChecked(), conf.isChecked());
            Intent intent = new Intent(this, DrawActivity.class);
            intent.putExtra(MATCH_KIND_ID, isSimplyMatch);
            intent.putExtra(MACH_SETTINGS_ID, matchSettings);
            intent.putExtra(LOGGED_IN_USER_ID, loggedInUser);
            MatchSettingsActivity.this.startActivity(intent);
            MatchSettingsActivity.this.finish();
        });
        builder.setNegativeButton("NIE", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    private void showConfirmBack()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.label_attention));
        builder.setMessage(getString(R.string.msg_attention));
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) ->
                presenter.onDiscardClicked());
        builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {});
        builder.create().show();
    }

    @Override
    public void onTeamClicked(Team team, boolean isHostTeam) {
        presenter.preparePlayersOfTeams(team, isHostTeam);
        sectionsPagerAdapter.setTeam(team, isHostTeam);
        teamsDialog.dismiss();
        if (isHostTeam)
            viewPager.setCurrentItem(0);
        else
            viewPager.setCurrentItem(1);
    }
}