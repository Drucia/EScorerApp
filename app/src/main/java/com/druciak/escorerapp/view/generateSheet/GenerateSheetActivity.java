package com.druciak.escorerapp.view.generateSheet;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.interfaces.IGenerateSheetMVP;
import com.druciak.escorerapp.presenter.GenerateSheetPresenter;

import static com.druciak.escorerapp.view.DrawActivity.MATCH_INFO_ID;

public class GenerateSheetActivity extends AppCompatActivity implements IGenerateSheetMVP.IView {
    private IGenerateSheetMVP.IPresenter presenter;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_sheet);

        Intent intent = getIntent();
        MatchInfo matchInfo = intent.getParcelableExtra(MATCH_INFO_ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Generowanie protokołu...");
        builder.setView(getLayoutInflater().inflate(R.layout.pop_up_progress_bar, null));
        builder.setCancelable(false);
        progressDialog = builder.create();

        presenter = new GenerateSheetPresenter(this, matchInfo);
        presenter.onActivityCreated();
    }

    @Override
    public void showProgressBarPopUp() {
        progressDialog.show();
    }
}
