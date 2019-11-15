package com.druciak.escorerapp.view.matchSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.interfaces.ISaveData;

public class MatchConfigsFragment extends Fragment implements ISaveData {
    private IMatchSettingsMVP.IView matchSettingsView;

    public MatchConfigsFragment(IMatchSettingsMVP.IView matchSettingsView) {
        this.matchSettingsView = matchSettingsView;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_match_configs, container, false);
        return root;
    }

    @Override
    public void save() {
        // save data
    }
}