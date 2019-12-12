package com.druciak.escorerapp.interfaces;

import android.view.View;

import com.druciak.escorerapp.entities.MatchSummary;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.List;

public interface IMatchFragmentMVP {
    interface IModel {
        void prepareAllSummariesOfUserMatches();
        void deleteSummary(MatchSummary item);
    }

    interface IView {
        void onPrepareMatchListEventSuccess(List<MatchSummary> summaries);
        void onMatchClicked(int adapterPosition, MatchSummary summary, View foreground);
    }

    interface IPresenter {
        void onPrepareMatchListEventCompleted(Result<List<MatchSummary>> result);
        void onRefresh();
        void onMadeDelete(MatchSummary item);
    }
}
