package com.druciak.escorerapp.view.mainPanel.matches;

import com.druciak.escorerapp.entities.MatchSummary;
import com.druciak.escorerapp.interfaces.IMatchFragmentMVP;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.List;

public class MatchFragmentPresenter implements IMatchFragmentMVP.IPresenter {
    private IMatchFragmentMVP.IModel model;
    private IMatchFragmentMVP.IView view;

    public MatchFragmentPresenter(String userId, IMatchFragmentMVP.IView view) {
        this.view = view;
        this.model = new MatchFragmentModel(userId, this);
    }

    @Override
    public void onPrepareMatchListEventCompleted(Result<List<MatchSummary>> result) {
        if (result instanceof Result.Success)
            view.onPrepareMatchListEventSuccess(((Result.Success<List<MatchSummary>>) result).getData());
    }

    @Override
    public void onRefresh() {
        model.prepareAllMatchesOfUser();
    }
}
