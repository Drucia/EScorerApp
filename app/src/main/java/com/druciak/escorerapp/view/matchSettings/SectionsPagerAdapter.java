package com.druciak.escorerapp.view.matchSettings;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.model.entities.Player;
import com.druciak.escorerapp.model.entities.Team;

import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_host, R.string.tab_text_guest,
            R.string.tab_text_rest};
    private final Context mContext;
    private List<Team> teams;
    private List<Player> players;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        int layout = R.layout.fragment_match_settings;
        switch(position)
        {
            case 0:
            case 1:
                return new TeamSettingsFragment(position, (IMatchSettingsMVP.IView) mContext);
            case 2:
                return PlaceholderFragment.newInstance(layout);
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
}