package com.druciak.escorerapp.interfaces;

import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.io.File;

public interface IGenerateSheetMVP {
    interface IModel {
        void generateSheet();
        LoggedInUser getUser();
        void sheetGenerated(File file);
        void sendSheetToServer();
        void onSendSheetCompleted(Result result);
    }

    interface IPresenter {
        void onActivityCreated();
        void onSheetGenerated(File file);
        void onDiscardClicked();
        void onSendClicked();
        void onSendSheetSucceeded();
        void onSendSheetFailed(String message);
    }

    interface IView {
        void showProgressBarPopUp();
        void makeIntentWithPdfReader(File file);
        void goToMainPanel(LoggedInUser user);
        void showToast(int stringRes);
    }
}
