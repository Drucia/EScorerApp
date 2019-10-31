package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.interfaces.IRunningMatchMVP;
import com.druciak.escorerapp.model.entities.MatchInfo;

public class RunningMatchPresenter implements IRunningMatchMVP.IPresenter {
    private IRunningMatchMVP.IView view;
    private MatchInfo matchInfo;

    public RunningMatchPresenter(IRunningMatchMVP.IView view, MatchInfo matchInfo) {
        this.view = view;
        this.matchInfo = matchInfo;
    }
}
