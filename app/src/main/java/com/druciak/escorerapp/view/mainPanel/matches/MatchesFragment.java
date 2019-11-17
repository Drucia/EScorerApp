package com.druciak.escorerapp.view.mainPanel.matches;

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

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.MatchSummary;
import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.interfaces.IMatchFragmentMVP;

import java.util.ArrayList;
import java.util.List;

public class MatchesFragment extends Fragment implements IMatchFragmentMVP.IView {
    private ArrayList<MatchSummary> summaries;
    private TextView noMatches;
    private IMainPanelMVP.IView view;
    private MatchesAdapter adapter;
    private IMatchFragmentMVP.IPresenter presenter;
    private RecyclerView matches;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_matches, container, false);
        view = ((IMainPanelMVP.IView) getActivity());
        matches = root.findViewById(R.id.matchesRecycler);
        summaries = new ArrayList<>();
        adapter = new MatchesAdapter(context, summaries);
        matches.setLayoutManager(new LinearLayoutManager(context));
        noMatches = root.findViewById(R.id.noMatches);
        presenter = new MatchesFragmentPresenter(view.getUserId(), this);
        presenter.onFragmentCreated();

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        view = (IMainPanelMVP.IView) context;
        this.context = context;
    }

    @Override
    public void onPrepareMatchListEventSuccess(List<MatchSummary> summaries) {
        if (!summaries.isEmpty())
            noMatches.setVisibility(View.GONE);
        int size = adapter.getItemCount();
        this.summaries.addAll(summaries);
        adapter.notifyItemRangeChanged(size, summaries.size());
    }
}