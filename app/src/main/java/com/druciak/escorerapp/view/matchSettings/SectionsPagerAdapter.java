package com.druciak.escorerapp.view.matchSettings;

import android.content.Context;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.Match;
import com.druciak.escorerapp.entities.MatchSettings;
import com.druciak.escorerapp.entities.Player;
import com.druciak.escorerapp.entities.Team;
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
    private List<Fragment> fragments = new ArrayList<>();
    private int currentFragment = 0;
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
        this.matchSettings = matchSettings;
        isSimplyMatch = true;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!fragments.isEmpty()) {
            if (fragments.get(currentFragment) instanceof TeamSettingsFragment)
                ((TeamSettingsFragment) fragments.get(currentFragment)).updatePlayersAdapter(players);
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                List<Player> playersOfHost = players.stream()
                        .filter(player -> player.getTeam().getId() == match.getHostTeam().getId())
                        .collect(Collectors.toList());
                fragments.add(new TeamSettingsFragment(mContext,
                        match.getHostTeam(), playersOfHost, isSimplyMatch));
                break;
            case 1:
                List<Player> playersOfGuest = players.stream()
                        .filter(player -> player.getTeam().getId() == match.getGuestTeam().getId())
                        .collect(Collectors.toList());
                fragments.add(new TeamSettingsFragment(mContext,
                        match.getGuestTeam(), playersOfGuest, isSimplyMatch));
                break;
            case 2:
                fragments.add(new MatchSettingsFragment((IMatchSettingsMVP.IView) mContext,
                        matchSettings));
                break;
            case 3:
                fragments.add(new MatchConfigsFragment((IMatchSettingsMVP.IView) mContext));
                break;
        }

        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    public void setPlayersOfTeams(List<Player> players) {
        this.players = players;
    }

    public void saveData(int fromPos) {
        if (fragments.get(fromPos) != null)
            ((ISaveData) fragments.get(currentFragment)).save();
        currentFragment = fromPos++;
    }

    public void setTeam(Team team, boolean isHostTeam) {
        if (isHostTeam)
            ((TeamSettingsFragment) fragments.get(0)).setTeam(team);
        else
            ((TeamSettingsFragment) fragments.get(1)).setTeam(team);
    }
}