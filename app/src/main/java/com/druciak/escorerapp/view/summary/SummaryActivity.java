package com.druciak.escorerapp.view.summary;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.entities.SetInfo;
import com.druciak.escorerapp.interfaces.ISummaryMVP;
import com.druciak.escorerapp.presenter.SummaryPresenter;
import com.druciak.escorerapp.view.generateSheet.GenerateSheetActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.druciak.escorerapp.view.DrawActivity.MATCH_INFO_ID;

public class SummaryActivity extends AppCompatActivity implements ISummaryMVP.IView {
    private ISummaryMVP.IPresenter presenter;

    private TextView teamAName;
    private TextView teamBName;
    private TextView score;
    private RecyclerView scoreRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();
        MatchInfo matchInfo = intent.getParcelableExtra(MATCH_INFO_ID);
        presenter = new SummaryPresenter(this, matchInfo);

        teamAName = findViewById(R.id.teamAName);
        teamBName = findViewById(R.id.teamBName);
        score = findViewById(R.id.score);
        scoreRecycler = findViewById(R.id.scoreRecycler);

        ExtendedFloatingActionButton attentions = findViewById(R.id.attentions);
        ExtendedFloatingActionButton generate = findViewById(R.id.generate);
        attentions.setOnClickListener(view -> presenter.onAttentionsClicked());
        generate.setOnClickListener(view -> presenter.onGenerateClicked());

        presenter.onActivityCreated();
    }

    @Override
    public void setFields(List<String> teamNames, Map<Integer, SetInfo> sets, List<Integer> setsScore) {
        teamAName.setText(teamNames.get(0));
        teamBName.setText(teamNames.get(1));
        score.setText(setsScore.get(0) + " : " + setsScore.get(1));
        ArrayList<SetInfo> setsSc = new ArrayList<>(sets.values());
        setsSc.add(0, null);
        scoreRecycler.setLayoutManager(new LinearLayoutManager(this));
        scoreRecycler.setAdapter(new SetInfoAdapter(setsSc));
    }

    @Override
    public void showPopUpWithAttentions(String attentions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.label_attentions));
        View root = getLayoutInflater().inflate(R.layout.pop_up_attentions, null);
        TextInputLayout layout = root.findViewById(R.id.attentionsLayout);
        builder.setView(root);
        if (!attentions.equals(""))
            layout.getEditText().setText(attentions);
        builder.setPositiveButton("Zapisz", (dialogInterface, i) ->
                presenter.onAttentionsSavedClicked(layout.getEditText().getText().toString()));
        builder.setNegativeButton("Anuluj", (dialogInterface, i) -> {});
        builder.create().show();
    }

    @Override
    public void showPopUpWithConfirmGeneration() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.label_generate_sheet));
        builder.setMessage("Czy jesteś pewny? Nie będzie już możliwości zmiany danych.");
        builder.setPositiveButton("TAK", (dialogInterface, i) -> presenter.onGenerateConfirm());
        builder.setNegativeButton("NIE", (dialogInterface, i) -> {});
    }

    @Override
    public void goToGenerateActivity(MatchInfo matchInfo) {
        Intent intent = new Intent(this, GenerateSheetActivity.class);
        intent.putExtra(MATCH_INFO_ID, matchInfo);
        startActivity(intent);
        finish();
    }
}
