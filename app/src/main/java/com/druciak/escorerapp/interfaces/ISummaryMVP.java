package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.entities.SetInfo;

import java.util.List;
import java.util.Map;

public interface ISummaryMVP {
    interface  IModel {
        List<String> getTeamsNames();
        Map<Integer, SetInfo> getSetsInfo();
        List<Integer> getSetsScores();
        String getAttentions();
        void updateAttentions(String attentions);
        MatchInfo getMatchInfo();
    }

    interface IView {
        void setFields(List<String> teamNames, Map<Integer, SetInfo> sets, List<Integer> setsScore);
        void showPopUpWithAttentions(String attentions);
        void showPopUpWithConfirmGeneration();
        void goToGenerateActivity(MatchInfo matchInfo);
    }

    interface IPresenter {

        void onActivityCreated();
        void onAttentionsClicked();
        void onGenerateClicked();
        void onAttentionsSavedClicked(String attentions);
        void onGenerateConfirm();
    }
}
