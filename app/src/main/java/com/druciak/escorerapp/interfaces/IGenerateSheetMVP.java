package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.entities.LoggedInUser;

import java.io.File;

public interface IGenerateSheetMVP {
    interface IModel {
        void generateSheet();
        LoggedInUser getUser();
        void sheetGeneratedIn(File file);
    }

    interface IPresenter {
        void onActivityCreated();
        void onSheetGenerated(File file);
        void onDiscardClicked();
    }

    interface IView {
        void showProgressBarPopUp();
        void makeIntentWithPdfReader(File file);
        void goToMainPanel(LoggedInUser user);
    }
}
