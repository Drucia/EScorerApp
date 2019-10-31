package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.model.entities.MatchPlayer;

import java.util.List;
import java.util.Map;

public interface IRunningMatchMVP {
    interface IModel{

    }

    interface IView extends IOnPlayerTouchCallback{
        void makeShiftInLineUp(int teamSideId);
        void setScore(String score);
        void showPopUpForLineUp(String teamName, List<MatchPlayer> players, boolean isFirst);
        void setAdapterWithPlayersLineUp(Map<Integer, MatchPlayer> lineUpA, Map<Integer, MatchPlayer> lineUpB);
        void showPopUpWithEndOfSet(String winner);
        void setFields(String fullName, String fullName1, int id);
    }

    interface IPresenter{
        void onActivityCreated();
        void onFirstLineUpSet();
        void onSecondLineUpSet();
        void onAttentionsClicked();
        void onReturnActionClicked();
        void onTeamsInfoClicked();
        void onFinishMatchClicked();
        void onAddPointClicked(int rightLineUpId);
        void onNextSetClicked();
    }
}
