package com.druciak.escorerapp.view.matchSettings;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.model.entities.Match;
import com.druciak.escorerapp.model.entities.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_host, R.string.tab_text_guest,
            R.string.tab_text_rest};
    private final Context mContext;
    private List<Player> players;
    private Match match;
    private Fragment currentFragment;

    public SectionsPagerAdapter(Context context, FragmentManager fm, Match match) {
        super(fm);
        mContext = context;
        this.match = match;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (currentFragment instanceof TeamSettingsFragment)
            ((TeamSettingsFragment) currentFragment).updatePlayersAdapter(players);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        int layout = R.layout.fragment_match_settings;
        switch(position)
        {
            case 0:
                List<Player> playersOfHost = players.stream()
                        .filter(player -> player.getTeam().getId() == match.getHostTeam().getId())
                        .collect(Collectors.toList());
                currentFragment = new TeamSettingsFragment((IMatchSettingsMVP.IView) mContext,
                        match.getHostTeam(), playersOfHost);
                return currentFragment;
            case 1:
                List<Player> playersOfGuest = players.stream()
                        .filter(player -> player.getTeam().getId() == match.getGuestTeam().getId())
                        .collect(Collectors.toList());
                currentFragment = new TeamSettingsFragment((IMatchSettingsMVP.IView) mContext,
                        match.getGuestTeam(), playersOfGuest);
                return currentFragment;
            case 2:
                currentFragment = PlaceholderFragment.newInstance(layout);
                return currentFragment;
        }
        return PlaceholderFragment.newInstance(layout);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    public void setPlayersOfTeams(List<Player> players) {
        this.players = players;
    }
}