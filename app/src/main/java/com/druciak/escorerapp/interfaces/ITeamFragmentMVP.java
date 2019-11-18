package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.entities.Team;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.List;

public interface ITeamFragmentMVP {
    interface IModel {
        void prepareAllTeamsOfUser();
    }

    interface IView {
        void onPrepareTeamsListEventSuccess(List<Team> summaries);
    }

    interface IPresenter {
        void onPrepareTeamsListEventCompleted(Result<List<Team>> result);
        void onRefresh();
    }
}
