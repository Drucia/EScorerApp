package com.druciak.escorerapp.view.generateSheet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.interfaces.IGenerateSheetMVP;
import com.druciak.escorerapp.presenter.GenerateSheetPresenter;
import com.druciak.escorerapp.view.mainPanel.MainPanelActivity;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import static com.druciak.escorerapp.view.DrawActivity.MATCH_INFO_ID;
import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.LOGGED_IN_USER_ID;
import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.USER_ADDITIONAL_INFO_ID;

public class GenerateSheetActivity extends AppCompatActivity implements IGenerateSheetMVP.IView {
    private IGenerateSheetMVP.IPresenter presenter;
    private AlertDialog progressDialog;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_sheet);

        Intent intent = getIntent();
        MatchInfo matchInfo = intent.getParcelableExtra(MATCH_INFO_ID);
        LoggedInUser user = intent.getParcelableExtra(LOGGED_IN_USER_ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Generowanie protokołu...");
        builder.setView(getLayoutInflater().inflate(R.layout.pop_up_progress_bar, null));
        builder.setCancelable(false);
        progressDialog = builder.create();
        pdfView = findViewById(R.id.pdfView);

        presenter = new GenerateSheetPresenter(this, matchInfo, user);
        presenter.onActivityCreated();
    }

    @Override
    public void showProgressBarPopUp() {
        progressDialog.show();
    }

    @Override
    public void makeIntentWithPdfReader(File file) {
        progressDialog.dismiss();
        Toast.makeText(this, getString(R.string.msg_generated_sheet) + file.getPath(),
                Toast.LENGTH_SHORT).show();
        pdfView.fromFile(file).pages(0).load();
    }

    @Override
    public void goToMainPanel(LoggedInUser user) {
        Intent intent = new Intent(this, MainPanelActivity.class);
        intent.putExtra(USER_ADDITIONAL_INFO_ID, true);
        intent.putExtra(LOGGED_IN_USER_ID, user);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.label_attention));
        builder.setMessage(getString(R.string.msg_attention_after_generate));
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) ->
                presenter.onDiscardClicked());
        builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {});
        builder.create().show();
    }
}
