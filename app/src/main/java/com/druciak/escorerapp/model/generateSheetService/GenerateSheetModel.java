package com.druciak.escorerapp.model.generateSheetService;

import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.interfaces.IGenerateSheetMVP;
import com.druciak.escorerapp.model.externalApiService.ExternalApiManager;
import com.druciak.escorerapp.model.firebaseService.Result;

import java.io.File;

public class GenerateSheetModel implements IGenerateSheetMVP.IModel{
    private SheetGenerator sheetGenerator;
    private LoggedInUser user;
    private MatchInfo matchInfo;
    private File file;
    private ExternalApiManager apiManager;
    private IGenerateSheetMVP.IPresenter presenter;

    public GenerateSheetModel(IGenerateSheetMVP.IPresenter presenter, MatchInfo matchInfo,
                              LoggedInUser user) {
        this.presenter = presenter;
        this.user = user;
        this.matchInfo = matchInfo;
        this.file = null;
        this.sheetGenerator = new SheetGenerator(matchInfo, this, user.getFullName());
        this.apiManager = new ExternalApiManager(this);
    }

    @Override
    public void generateSheet() {
        sheetGenerator.generateSheet();
    }

    @Override
    public LoggedInUser getUser() {
        return user;
    }

    @Override
    public void sheetGenerated(File file) {
        this.file = file;
        presenter.onSheetGenerated(file);
    }

    @Override
    public void sendSheetToServer() {
        apiManager.sendSheetToServer(matchInfo.getSettings().getMatch().getId(),
                file, user.getUserId(), user.getFullName());
    }

    @Override
    public void onSendSheetCompleted(Result result) {
        if (result instanceof Result.Success)
            presenter.onSendSheetSucceeded();
        else
            presenter.onSendSheetFailed(((Result.Error) result).getError().getMessage());
    }
}
