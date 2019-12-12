package com.druciak.escorerapp.view.mainPanel.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IMainPanelMVP;

public class ToolsFragment extends Fragment {
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tools, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment((IMainPanelMVP.IView) getActivity()))
                .commit();
        getActivity().setTitle(R.string.title_fragment_tools);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        IMainPanelMVP.IView activity;

        public SettingsFragment() {
        }

        SettingsFragment(IMainPanelMVP.IView activity) {
            this.activity = activity;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference darkMode = findPreference("theme");
            darkMode.setOnPreferenceChangeListener((preference, isDarkThemeEnabled) -> {
//                if ((boolean) isDarkThemeEnabled)
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                else
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                activity.changeMode();
                return true;
            });
        }
    }
}