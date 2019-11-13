package com.druciak.escorerapp.interfaces;

import java.io.File;

public interface IGenerateSheetMVP {
    interface IModel {
        void generateSheet();
        void sheetGeneratedIn(File file);
    }

    interface IPresenter {
        void onActivityCreated();
        void onSheetGenerated(File file);
    }

    interface IView {
        void showProgressBarPopUp();
        void makeIntentWithPdfReader(File file);
    }
}
