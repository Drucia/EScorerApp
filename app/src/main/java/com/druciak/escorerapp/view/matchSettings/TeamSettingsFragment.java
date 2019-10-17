package com.druciak.escorerapp.view.matchSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.model.entities.Player;
import com.druciak.escorerapp.model.entities.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamSettingsFragment extends Fragment {
    private static List<Team> teams;
    private ArrayList<Player> players;
    private Spinner teamSpinner;
    private RecyclerView playerRecycler;
    private IMatchSettingsMVP.IView context;

    public TeamSettingsFragment(int contentLayoutId, IMatchSettingsMVP.IView context) {
        super(contentLayoutId);
        this.players = new ArrayList<>();
        this.context = context;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_team_setting_page, container, false);
        teamSpinner = root.findViewById(R.id.spinner);
        playerRecycler = root.findViewById(R.id.playersRecyclerView);
        playerRecycler.setAdapter(new PlayersAdapter(context));

        return root;
    }
}
