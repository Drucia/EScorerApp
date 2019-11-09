package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.entities.SetInfo;
import com.druciak.escorerapp.interfaces.ISummaryMVP;
import com.druciak.escorerapp.model.SummaryModel;

import java.util.HashMap;
import java.util.List;

public class SummaryPresenter implements ISummaryMVP.IPresenter {
    private ISummaryMVP.IView view;
    private ISummaryMVP.IModel model;

    public SummaryPresenter(ISummaryMVP.IView view, MatchInfo matchInfo) {
        this.view = view;
        model = new SummaryModel(this, matchInfo);
    }

    @Override
    public void onActivityCreated() {
        HashMap<Integer, SetInfo> sets = model.getSetsInfo();
        List<String> teamNames = model.getTeamsNames();
        view.setFields(teamNames, sets);
    }
}
