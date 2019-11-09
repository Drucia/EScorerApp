package com.druciak.escorerapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.entities.SetInfo;
import com.druciak.escorerapp.interfaces.ISummaryMVP;
import com.druciak.escorerapp.presenter.SummaryPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SummaryActivity extends AppCompatActivity implements ISummaryMVP.IView {
    private ISummaryMVP.IPresenter presenter;

    private TextView teamAName;
    private TextView teamBName;
    private RecyclerView scoreRecycler;

    private ArrayList<SetInfo> setsScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent intent = getIntent();
        MatchInfo matchInfo = intent.getParcelableExtra("dupa");
        presenter = new SummaryPresenter(this, matchInfo);

        teamAName = findViewById(R.id.teamAName);
        teamBName = findViewById(R.id.teamBName);
        scoreRecycler = findViewById(R.id.scoreRecycler);

        presenter.onActivityCreated();
    }

    @Override
    public void setFields(List<String> teamNames, HashMap<Integer, SetInfo> sets) {
        // todo
    }
}
