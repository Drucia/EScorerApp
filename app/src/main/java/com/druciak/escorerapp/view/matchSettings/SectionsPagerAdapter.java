package com.druciak.escorerapp.view.matchSettings;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.Match;
import com.druciak.escorerapp.entities.MatchSettings;
import com.druciak.escorerapp.entities.Player;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.interfaces.ISaveData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private int[] tabTitles;
    private final Context mContext;
    private List<Player> players;
    private Match match;
    private MatchSettings matchSettings;
    private Fragment currentFragment;
    private boolean isSimplyMatch = false;

    public SectionsPagerAdapter(Context context, FragmentManager fm, Match match,
                                MatchSettings matchSettings) {
        super(fm);
        mContext = context;
        tabTitles = new int[]{R.string.tab_text_host, R.string.tab_text_guest,
                R.string.tab_text_rest};
        this.match = match;
        this.matchSettings = matchSettings;
    }

    public SectionsPagerAdapter(Context context, FragmentManager fm, Match match, int matchKind,
                                MatchSettings matchSettings) {
        super(fm);
        mContext = context;
        this.match = match;
        tabTitles = new int[]{R.string.tab_text_host, R.string.tab_text_guest,
                R.string.tab_text_rest, R.string.tab_text_different};
        players = new ArrayList<>();
        this.matchSettings = matchSettings;
        isSimplyMatch = true;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (currentFragment instanceof TeamSettingsFragment)
            ((TeamSettingsFragment) currentFragment).updatePlayersAdapter(players);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                List<Player> playersOfHost = players.stream()
                        .filter(player -> player.getTeam().getId() == match.getHostTeam().getId())
                        .collect(Collectors.toList());
                currentFragment = new TeamSettingsFragment((IMatchSettingsMVP.IView) mContext,
                        match.getHostTeam(), playersOfHost, isSimplyMatch);
                return currentFragment;
            case 1:
                List<Player> playersOfGuest = players.stream()
                        .filter(player -> player.getTeam().getId() == match.getGuestTeam().getId())
                        .collect(Collectors.toList());
                currentFragment = new TeamSettingsFragment((IMatchSettingsMVP.IView) mContext,
                        match.getGuestTeam(), playersOfGuest, isSimplyMatch);
                return currentFragment;
            case 2:
                currentFragment = new MatchSettingsFragment((IMatchSettingsMVP.IView) mContext,
                        matchSettings);
                return currentFragment;
            case 3:
                currentFragment = new MatchConfigsFragment((IMatchSettingsMVP.IView) mContext);
                return currentFragment;
        }
        return new MatchSettingsFragment((IMatchSettingsMVP.IView) mContext, matchSettings);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(tabTitles[position]);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    public void setPlayersOfTeams(List<Player> players) {
        this.players = players;
    }

    public void saveData() {
        if (currentFragment != null)
            ((ISaveData) currentFragment).save();
    }
}