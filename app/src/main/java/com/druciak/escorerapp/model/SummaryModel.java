package com.druciak.escorerapp.model;

import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.entities.SetInfo;
import com.druciak.escorerapp.interfaces.ISummaryMVP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryModel implements ISummaryMVP.IModel {
    private ISummaryMVP.IPresenter presenter;
    private MatchInfo matchInfo;

    public SummaryModel(ISummaryMVP.IPresenter presenter, MatchInfo matchInfo) {
        this.presenter = presenter;
        this.matchInfo = matchInfo;
    }

    @Override
    public List<String> getTeamsNames() {
        return new ArrayList<>(Arrays.asList(matchInfo.getTeamA().getFullName(),
                matchInfo.getTeamB().getFullName()));
    }

    @Override
    public HashMap<Integer, SetInfo> getSetsInfo() {
        Map<Integer, Integer> times = matchInfo.getTimesOfSets();

        return null;
    }
}
