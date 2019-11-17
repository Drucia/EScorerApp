package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.entities.SetInfo;
import com.druciak.escorerapp.interfaces.ISummaryMVP;
import com.druciak.escorerapp.model.SummaryModel;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.util.List;
import java.util.Map;

public class SummaryPresenter implements ISummaryMVP.IPresenter {
    private ISummaryMVP.IView view;
    private ISummaryMVP.IModel model;

    public SummaryPresenter(ISummaryMVP.IView view, MatchInfo matchInfo, LoggedInUser user) {
        this.view = view;
        model = new SummaryModel(this, matchInfo, user);
    }

    @Override
    public void onActivityCreated() {
        Map<Integer, SetInfo> sets = model.getSetsInfo();
        List<String> teamNames = model.getTeamsNames();
        List<Integer> setsScore = model.getSetsScores();
        view.setFields(teamNames, sets, setsScore);
    }

    @Override
    public void onAttentionsClicked() {
        view.showPopUpWithAttentions(model.getAttentions());
    }

    @Override
    public void onGenerateClicked() {
        view.showPopUpWithConfirmGeneration();
    }

    @Override
    public void onAttentionsSavedClicked(String attentions) {
        model.updateAttentions(attentions);
    }

    @Override
    public void onGenerateConfirm() {
        view.goToGenerateActivity(model.getMatchInfo(),
                model.getUser());
    }

    @Override
    public void discardMatch() {
        view.goToMainPanel(model.getUser());
    }

    @Override
    public void onSaveClicked() {
        model.saveSummaryOnServer();
    }

    @Override
    public void onSaveSummaryCompleted(Result<String> result) {
        if (result instanceof Result.Success)
            view.showToast(((Result.Success<String>) result).getData());
        else
            view.showToast(((Result.Error) result).getError().getMessage());
        view.goToMainPanel(model.getUser());
    }
}
