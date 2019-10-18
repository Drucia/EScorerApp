package com.druciak.escorerapp.view.matchSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IMatchSettingsMVP;
import com.druciak.escorerapp.model.entities.Player;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.List;

public class TeamSettingsFragment extends Fragment implements IMatchSettingsMVP.IFragmentView{
    private PlayersAdapter playersAdapter;
    private RecyclerView playerRecycler;
    private IMatchSettingsMVP.IView context;
    private SpeedDialView speedDialView;
    private String teamName;

    public TeamSettingsFragment(IMatchSettingsMVP.IView mContext, String fullName,
                                List<Player> playersOfHost) {
        context = mContext;
        teamName = fullName;
        playersAdapter = new PlayersAdapter(this);
        playersAdapter.setListItems(playersOfHost);
        playersAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_team_setting_page, container, false);
        playerRecycler = root.findViewById(R.id.playersRecyclerView);
        playerRecycler.setAdapter(playersAdapter);
        playerRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        speedDialView = root.findViewById(R.id.speedDial);
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.medicineFab, R.drawable.medicine)
                        .setLabel("Masażysta")
                        .create());
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.couachFab, R.drawable.coach)
                        .setLabel("Trener")
                        .create());
        ((TextView) root.findViewById(R.id.teamName)).setText(teamName);
        return root;
    }

    public void updatePlayersAdapter(List<Player> players)
    {
        playersAdapter.setListItems(players);
        playersAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlayerClicked(Player player, int adapterPosition) {
        // todo
    }
}
