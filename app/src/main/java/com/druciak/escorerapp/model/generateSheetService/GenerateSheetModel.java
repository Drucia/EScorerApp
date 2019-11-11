package com.druciak.escorerapp.model.generateSheetService;

import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.interfaces.IGenerateSheetMVP;

public class GenerateSheetModel implements IGenerateSheetMVP.IModel{
    private SheetGenerator sheetGenerator;
    private IGenerateSheetMVP.IPresenter presenter;

    public GenerateSheetModel(IGenerateSheetMVP.IPresenter presenter, MatchInfo matchInfo) {
        this.presenter = presenter;
        this.sheetGenerator = new SheetGenerator(matchInfo, this);
    }

    @Override
    public void generateSheet() {
        sheetGenerator.generateSheet();
    }
}
