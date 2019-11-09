package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.entities.SetInfo;

import java.util.HashMap;
import java.util.List;

public interface ISummaryMVP {
    interface  IModel {
        List<String> getTeamsNames();
        HashMap<Integer, SetInfo> getSetsInfo();
    }

    interface IView {
        void setFields(List<String> teamNames, HashMap<Integer, SetInfo> sets);
    }

    interface IPresenter {

        void onActivityCreated();
    }
}
