package com.druciak.escorerapp.view.mainPanel.teams;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.Team;
import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.interfaces.ITeamCallback;
import com.druciak.escorerapp.interfaces.ITeamFragmentMVP;
import com.druciak.escorerapp.view.matchSettings.TeamsAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeamFragment extends Fragment implements ITeamFragmentMVP.IView, ITeamCallback {
    private ArrayList<Team> teams;
    private TextView noTeams;
    private IMainPanelMVP.IView view;
    private TeamsAdapter adapter;
    private ITeamFragmentMVP.IPresenter presenter;
    private RecyclerView matches;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teams, container, false);
        getActivity().setTitle(R.string.title_fragment_teams);
        view = ((IMainPanelMVP.IView) getActivity());
        matches = root.findViewById(R.id.teamsRecycler);
        teams = new ArrayList<>();
        adapter = new TeamsAdapter(this, teams, false);
        matches.setLayoutManager(new LinearLayoutManager(getContext()));
        matches.setAdapter(adapter);
        noTeams = root.findViewById(R.id.noTeams);
        SwipeRefreshLayout refresh = root.findViewById(R.id.pullToRefreshTeams);
        refresh.setOnRefreshListener(() -> {
            presenter.onRefresh();
            refresh.setRefreshing(false);
        });
        presenter = new TeamFragmentPresenter(this, view.getUserId());
        presenter.onRefresh();

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        view = (IMainPanelMVP.IView) context;
    }

    @Override
    public void onPrepareTeamsListEventSuccess(List<Team> teamsFromServer) {
        if (!teamsFromServer.isEmpty())
            noTeams.setVisibility(View.GONE);
        teams.clear();
        int size = adapter.getItemCount();
        teams.addAll(teamsFromServer);
        adapter.notifyItemRangeChanged(size, teamsFromServer.size());

    }

    @Override
    public void onTeamClicked(Team team, boolean isHostTeam) {
        // todo in the future
    }
}