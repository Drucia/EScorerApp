package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.model.entities.MatchPlayer;

import java.util.List;
import java.util.Map;

public interface IRunningMatchMVP {
    interface IModel{

    }

    interface IView extends IOnPlayerTouchCallback{
        void showPopUpForLineUp(String teamName, List<MatchPlayer> players, boolean isFirst);
        void setAdapterWithPlayersLineUp(Map<Integer, MatchPlayer> lineUpA, Map<Integer, MatchPlayer> lineUpB);
    }

    interface IPresenter{
        void onActivityCreated();
        void onFirstLineUpSet();
        void onSecondLineUpSet();
    }
}
