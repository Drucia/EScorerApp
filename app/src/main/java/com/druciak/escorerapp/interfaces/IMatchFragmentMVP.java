package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.entities.MatchSummary;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.List;

public interface IMatchFragmentMVP {
    interface IModel {
        void prepareAllMatchesOfUser();
    }

    interface IView {
        void onPrepareMatchListEventSuccess(List<MatchSummary> summaries);
    }

    interface IPresenter {
        void onPrepareMatchListEventCompleted(Result<List<MatchSummary>> result);
        void onFragmentCreated();
    }
}
