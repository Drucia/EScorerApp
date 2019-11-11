package com.druciak.escorerapp.presenter;

import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.interfaces.IGenerateSheetMVP;
import com.druciak.escorerapp.model.generateSheetService.GenerateSheetModel;

public class GenerateSheetPresenter implements IGenerateSheetMVP.IPresenter {
    private IGenerateSheetMVP.IView view;
    private IGenerateSheetMVP.IModel model;

    public GenerateSheetPresenter(IGenerateSheetMVP.IView context, MatchInfo matchInfo) {
        this.view = context;
        model = new GenerateSheetModel(this, matchInfo);
    }

    @Override
    public void onActivityCreated() {
        view.showProgressBarPopUp();
        model.generateSheet();
    }
}
