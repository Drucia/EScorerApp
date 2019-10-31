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

    @Override
    public void onActivityCreated() {
        view.showPopUpForLineUp(matchInfo.getTeamAName(MatchInfo.TEAM_A_ID),
                matchInfo.getPlayers(MatchInfo.TEAM_A_ID), true);
    }

    @Override
    public void onFirstLineUpSet() {
        view.showPopUpForLineUp(matchInfo.getTeamAName(MatchInfo.TEAM_B_ID),
                matchInfo.getPlayers(MatchInfo.TEAM_B_ID), false);
    }

    @Override
    public void onSecondLineUpSet() {
        view.setAdapterWithPlayersLineUp(matchInfo.getTeamA().getLineUp(), matchInfo.getTeamB().getLineUp());
    }
}
