package com.druciak.escorerapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.model.entities.Match;
import com.druciak.escorerapp.model.entities.MatchSettings;
import com.druciak.escorerapp.view.matchSettings.MatchSettingsActivity;
import com.druciak.escorerapp.view.runningMatch.RunningMatchActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;

import static com.druciak.escorerapp.view.runningMatch.RunningMatchActivity.IS_REQ_ID;

public class DrawActivity extends AppCompatActivity {
    public static final String MATCH_INFO_ID = "match_info";
    public static final String SERVE_TEAM_ID = "serve_team";
    public static final String LEFT_TEAM_ID = "left_team_id";

    private static final int NO_CHOICE_MADE_ID = -1;
    private static final int SERVE_ID = 1;
    private static final int ADOPTING_ID = 2;
    private static final int RESIZE_VALUE_BIG = 170;
    private static final int RESIZE_VALUE_SMALL = 80;

    private ImageView leftAdopt;
    private ImageView rightAdopt;
    private ImageView leftServe;
    private ImageView rightServe;
    private MaterialButton startMatchButton;

    private int leftTeamChoice = NO_CHOICE_MADE_ID;
    private int rightTeamChoice = NO_CHOICE_MADE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        Intent intent = getIntent();
        boolean isRequest = intent.getBooleanExtra(IS_REQ_ID, false);
        MatchSettings ms = intent.getParcelableExtra(MatchSettingsActivity.MACH_SETTINGS_ID);
        Match match = ms.getMatch();
        Spinner leftTeam = findViewById(R.id.leftTeamSpinner);
        Spinner rightTeam = findViewById(R.id.rightTeamSpinner);
        startMatchButton = findViewById(R.id.startMatch);
        startMatchButton.setOnClickListener(view -> {
            if (isRequest){
                Intent i = new Intent();
                int leftTeamId = leftTeam.getSelectedItem().toString().equals(match.getHostTeam()
                        .getShortName()) ? match.getHostTeam().getId() : match.getGuestTeam().getId();
                int serveTeam = leftTeamChoice == SERVE_ID ? match.getHostTeam().getId() == leftTeamId
                        ? leftTeamId : match.getGuestTeam().getId() :
                        match.getHostTeam().getId() != leftTeamId ? match.getGuestTeam().getId() : leftTeamId;
                i.putExtra(SERVE_TEAM_ID, serveTeam); //todo
                i.putExtra(LEFT_TEAM_ID, leftTeamId); //todo
                setResult(RESULT_OK, i);
            } else
            {
                Intent i = new Intent(this, RunningMatchActivity.class);
                i.putExtra(MATCH_INFO_ID, ""); // todo
                startActivity(i);
            }
            finish();
        });
        List<String> teamsNames = Arrays.asList(match.getHostTeam().getShortName(), match.getGuestTeam().getShortName());
//        List<String> teamsNames = Arrays.asList("Chełmiec", "Polonia"); // todo remove hardcode
        ArrayAdapter<String> data = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, teamsNames);
        leftTeam.setAdapter(data);
        rightTeam.setAdapter(data);
        rightTeam.setSelection(1);

        leftAdopt = findViewById(R.id.leftAdopting);
        rightAdopt = findViewById(R.id.rightAdopting);
        leftServe = findViewById(R.id.leftServe);
        rightServe = findViewById(R.id.rightServe);

        ImageView.OnClickListener listener = view -> {
            switch (view.getId()){
                case R.id.leftAdopting:
                    if (leftTeamChoice == SERVE_ID) {
                        changeSize(leftAdopt, RESIZE_VALUE_BIG);
                    }
                    if (leftTeamChoice != ADOPTING_ID) {
                        changeSize(leftServe, RESIZE_VALUE_SMALL);
                        leftTeamChoice = ADOPTING_ID;
                        if (leftTeamChoice == rightTeamChoice || rightTeamChoice == NO_CHOICE_MADE_ID)
                            rightServe.callOnClick();
                        else
                            startMatchButton.setEnabled(true);
                    }
                    break;
                case R.id.rightAdopting:
                    if (rightTeamChoice == SERVE_ID) {
                        changeSize(rightAdopt, RESIZE_VALUE_BIG);
                    }
                    if (rightTeamChoice != ADOPTING_ID) {
                        changeSize(rightServe, RESIZE_VALUE_SMALL);
                        rightTeamChoice = ADOPTING_ID;
                        if (leftTeamChoice == rightTeamChoice || leftTeamChoice == NO_CHOICE_MADE_ID)
                            leftServe.callOnClick();
                        else
                            startMatchButton.setEnabled(true);

                    }
                    break;
                case R.id.leftServe:
                    if (leftTeamChoice == ADOPTING_ID) {
                        changeSize(leftServe, RESIZE_VALUE_BIG);
                    }
                    if (leftTeamChoice != SERVE_ID) {
                        changeSize(leftAdopt, RESIZE_VALUE_SMALL);
                        leftTeamChoice = SERVE_ID;
                        if (leftTeamChoice == rightTeamChoice || rightTeamChoice == NO_CHOICE_MADE_ID)
                            rightAdopt.callOnClick();
                        else
                            startMatchButton.setEnabled(true);
                    }
                    break;
                case R.id.rightServe:
                    if (rightTeamChoice == ADOPTING_ID) {
                        changeSize(rightServe, RESIZE_VALUE_BIG);
                    }
                    if (rightTeamChoice != SERVE_ID) {
                        changeSize(rightAdopt, RESIZE_VALUE_SMALL);
                        rightTeamChoice = SERVE_ID;
                        if (leftTeamChoice == rightTeamChoice || leftTeamChoice == NO_CHOICE_MADE_ID)
                            leftAdopt.callOnClick();
                        else
                            startMatchButton.setEnabled(true);
                    }
                    break;
            }
        };
        leftAdopt.setOnClickListener(listener);
        leftServe.setOnClickListener(listener);
        rightAdopt.setOnClickListener(listener);
        rightServe.setOnClickListener(listener);
    }

    private void changeSize(ImageView toResize, int size){
        ViewGroup.LayoutParams params = toResize.getLayoutParams();
        params.height = size;
        params.width = size;
        toResize.setLayoutParams(params);
    }
}
