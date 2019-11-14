package com.druciak.escorerapp.model.generateSheetService;

import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.interfaces.IGenerateSheetMVP;

import java.io.File;

public class GenerateSheetModel implements IGenerateSheetMVP.IModel{
    private SheetGenerator sheetGenerator;
    private LoggedInUser user;
    private IGenerateSheetMVP.IPresenter presenter;

    public GenerateSheetModel(IGenerateSheetMVP.IPresenter presenter, MatchInfo matchInfo,
                              LoggedInUser user) {
        this.presenter = presenter;
        this.user = user;
        this.sheetGenerator = new SheetGenerator(matchInfo, this, user.getFullName());
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
    public void sheetGeneratedIn(File file) {
        presenter.onSheetGenerated(file);
    }
}
