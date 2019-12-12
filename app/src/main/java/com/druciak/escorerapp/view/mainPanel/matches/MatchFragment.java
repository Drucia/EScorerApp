package com.druciak.escorerapp.view.mainPanel.matches;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.MatchSummary;
import com.druciak.escorerapp.interfaces.IMainPanelMVP;
import com.druciak.escorerapp.interfaces.IMatchFragmentMVP;
import com.druciak.escorerapp.view.mainPanel.MatchDetailsActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MatchFragment extends Fragment implements IMatchFragmentMVP.IView {
    public static final String EXTRA_MATCH_SUMMARY_ITEM = "match_item";
    public static final String EXTRA_TRANSITION_NAME = "transition_name";
    public static final String EXTRA_MATCH_POSITION = "position";

    private ArrayList<MatchSummary> summaries;
    private TextView noMatches;
    private IMainPanelMVP.IView view;
    private MatchAdapter adapter;
    private IMatchFragmentMVP.IPresenter presenter;
    private Context context;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_matches, container, false);
        getActivity().setTitle(R.string.title_fragment_matches);
        view = ((IMainPanelMVP.IView) getActivity());
        RecyclerView matches = root.findViewById(R.id.matchesRecycler);
        summaries = new ArrayList<>();
        adapter = new MatchAdapter(context, this, summaries);
        matches.setLayoutManager(new LinearLayoutManager(getContext()));
        matches.setAdapter(adapter);
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final MatchSummary item = summaries.get(position);

                adapter.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(root, "Mecz " + (position+1) + " został usunięty.", Snackbar.LENGTH_LONG)
                        .addCallback(new Snackbar.Callback() {

                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                    presenter.onMadeDelete(item);
                                }
                            }

                            @Override
                            public void onShown(Snackbar snackbar) {
                                super.onShown(snackbar);
                            }
                        });
                snackbar.setAction("COFNIJ", view -> {
                    adapter.restoreItem(item, position);
                    matches.scrollToPosition(position);
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };

        new ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(matches);
        noMatches = root.findViewById(R.id.noMatches);
        SwipeRefreshLayout refresh = root.findViewById(R.id.pullToRefresh);
        refresh.setOnRefreshListener(() -> {
            presenter.onRefresh();
            refresh.setRefreshing(false);
        });
        presenter = new MatchFragmentPresenter(view.getUserId(), this);
        presenter.onRefresh();

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        view = (IMainPanelMVP.IView) context;
        this.context = context;
    }

    @Override
    public void onPrepareMatchListEventSuccess(List<MatchSummary> summ) {
        if (!summ.isEmpty())
            noMatches.setVisibility(View.GONE);
        summaries.clear();
        int size = adapter.getItemCount();
        summaries.addAll(summ);
        adapter.notifyItemRangeChanged(size, summ.size());
    }

    @Override
    public void onMatchClicked(int adapterPosition, MatchSummary summary, View foreground) {
        Intent intent = new Intent(getContext(), MatchDetailsActivity.class);
        intent.putExtra(EXTRA_MATCH_SUMMARY_ITEM, summary);
        intent.putExtra(EXTRA_TRANSITION_NAME, summary.getMatch().getName());
        intent.putExtra(EXTRA_MATCH_POSITION, String.valueOf(adapterPosition));
        view.goToDetails(intent);
//        view.goToDetails(summary.getMatch().getName(), summary, foreground);
    }
}