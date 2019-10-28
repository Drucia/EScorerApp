package com.druciak.escorerapp.view;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.druciak.escorerapp.R;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;

public class DrawActivity extends AppCompatActivity {
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

        Spinner leftTeam = findViewById(R.id.leftTeamSpinner);
        Spinner rightTeam = findViewById(R.id.rightTeamSpinner);
        startMatchButton = findViewById(R.id.startMatch);
        startMatchButton.setOnClickListener(view -> {
//            Intent intent = new Intent();
        });
//        Intent intent = getIntent();
//        MatchSettings ms = intent.getParcelableExtra(MatchSettingsActivity.MACH_SETTINGS_ID);
//        Match match = ms.getMatch();
//        List<String> teamsNames = Arrays.asList(match.getHostTeam().getShortName(), match.getGuestTeam().getShortName());
        List<String> teamsNames = Arrays.asList("Chełmiec", "Polonia"); // todo remove hardcode
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
