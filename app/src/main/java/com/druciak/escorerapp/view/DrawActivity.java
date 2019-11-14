package com.druciak.escorerapp.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.LoggedInUser;
import com.druciak.escorerapp.entities.Match;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.entities.MatchSettings;
import com.druciak.escorerapp.entities.Player;
import com.druciak.escorerapp.entities.Team;
import com.druciak.escorerapp.view.matchSettings.MatchSettingsActivity;
import com.druciak.escorerapp.view.runningMatch.RunningMatchActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.druciak.escorerapp.view.mainPanel.MainPanelActivity.LOGGED_IN_USER_ID;
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
    private Spinner leftTeam;
    private Spinner rightTeam;

    private int leftTeamChoice = NO_CHOICE_MADE_ID;
    private int rightTeamChoice = NO_CHOICE_MADE_ID;
    private MatchSettings matchSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();
        boolean isRequest = intent.getBooleanExtra(IS_REQ_ID, false);
        matchSettings = intent.getParcelableExtra(MatchSettingsActivity.MACH_SETTINGS_ID);
        LoggedInUser user = intent.getParcelableExtra(LOGGED_IN_USER_ID);
        leftTeam = findViewById(R.id.leftTeamSpinner);
        rightTeam = findViewById(R.id.rightTeamSpinner);
        startMatchButton = findViewById(R.id.startMatch);
        startMatchButton.setOnClickListener(view -> {
            int leftTeamId = getLeftTeamId();
            int serveTeam = getServeTeamId(leftTeamId);

            if (isRequest){
                Intent i = new Intent();
                i.putExtra(SERVE_TEAM_ID, serveTeam);
                i.putExtra(LEFT_TEAM_ID, leftTeamId);
                i.putExtra(LOGGED_IN_USER_ID, user);
                setResult(RESULT_OK, i);
            } else
            {
                Intent i = new Intent(this, RunningMatchActivity.class);
                i.putExtra(MATCH_INFO_ID, generateMatchInfo(leftTeamId, serveTeam));
                i.putExtra(LOGGED_IN_USER_ID, user);
                startActivity(i);
            }
            finish();
        });
        List<String> teamsNames = Arrays.asList(matchSettings.getMatch().getHostTeam().getShortName(),
                matchSettings.getMatch().getGuestTeam().getShortName());
        ArrayAdapter<String> data = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, teamsNames);
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

    private MatchInfo generateMatchInfo(int leftTeamId, int serveTeamId) {
        Match match = matchSettings.getMatch();
        Team teamA = match.getHostTeam().getId() == leftTeamId ? match.getHostTeam() :
                match.getGuestTeam();
        Team teamB = match.getHostTeam().getId() != leftTeamId ? match.getHostTeam() :
                match.getGuestTeam();
        List<Player> playersA = matchSettings.getPlayers().stream()
                .filter(player -> player.getTeam().getId() == teamA.getId())
                .collect(Collectors.toList());
        List<Player> playersB = matchSettings.getPlayers().stream()
                .filter(player -> player.getTeam().getId() == teamB.getId())
                .collect(Collectors.toList());
        return new MatchInfo(teamA, playersA, teamB, playersB,
                serveTeamId == teamA.getId(), matchSettings);
    }

    private int getServeTeamId(int leftTeamId) {
        Match match = matchSettings.getMatch();
        if (leftTeamChoice == SERVE_ID)
        {
            if (leftTeamId == match.getHostTeam().getId())
                return leftTeamId;
            else
                return match.getGuestTeam().getId();
        } else {
            if (leftTeamId == match.getHostTeam().getId())
                return match.getGuestTeam().getId();
            else
                return leftTeamId;
        }
    }

    private int getLeftTeamId() {
        Match match = matchSettings.getMatch();
        return leftTeam.getSelectedItem().toString().equals(match.getHostTeam()
                .getShortName()) ? match.getHostTeam().getId() : match.getGuestTeam().getId();
    }

    private void changeSize(ImageView toResize, int size){
        ViewGroup.LayoutParams params = toResize.getLayoutParams();
        params.height = size;
        params.width = size;
        toResize.setLayoutParams(params);
    }
}
