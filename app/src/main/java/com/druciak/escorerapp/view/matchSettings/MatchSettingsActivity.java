package com.druciak.escorerapp.view.matchSettings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.model.entities.Match;
import com.druciak.escorerapp.model.entities.Player;
import com.druciak.escorerapp.presenter.MatchSettingsPresenter;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MatchSettingsActivity extends AppCompatActivity implements IMatchSettingsMVP.IView {

    private ViewPager viewPager;
    private IMatchSettingsMVP.IPresenter presenter;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_settings);
        Intent intent = getIntent();
        Match match = intent.getParcelableExtra("match");
        sectionsPagerAdapter = new SectionsPagerAdapter(this,
                getSupportFragmentManager(), match);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        presenter = new MatchSettingsPresenter(this);
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
}