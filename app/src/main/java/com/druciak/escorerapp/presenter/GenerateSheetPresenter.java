package com.druciak.escorerapp.presenter;

import android.content.Context;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.interfaces.IGenerateSheetMVP;
import com.druciak.escorerapp.model.generateSheetService.GenerateSheetModel;

import java.io.File;

public class GenerateSheetPresenter implements IGenerateSheetMVP.IPresenter {
    private IGenerateSheetMVP.IView view;
    private IGenerateSheetMVP.IModel model;

    public GenerateSheetPresenter(Context context, MatchInfo matchInfo, LoggedInUser user) {
        this.view = (IGenerateSheetMVP.IView) context;
        model = new GenerateSheetModel(this, matchInfo, user);
    }

    @Override
    public void onActivityCreated() {
        view.showProgressBarPopUp();
        model.generateSheet();
    }

    @Override
    public void onSheetGenerated(File file) {
        view.makeIntentWithPdfReader(file);
    }

    @Override
    public void onDiscardClicked() {
        view.goToMainPanel(model.getUser());
    }

    @Override
    public void onSendClicked() {
        model.sendSheetToServer();
    }

    @Override
    public void onSendSheetSucceeded() {
        view.showToast(R.string.msg_succ_on_send_sheet);
        view.goToMainPanel(model.getUser());
    }

    @Override
    public void onSendSheetFailed(String message) {
        view.showToast(R.string.msg_failed_on_send_sheet);
        view.goToMainPanel(model.getUser());
    }
}
