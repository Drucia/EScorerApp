package com.druciak.escorerapp.view.mainPanel;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.entities.MatchSummary;
import com.druciak.escorerapp.view.mainPanel.matches.MatchFragment;

public class MatchDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_details);
        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        MatchSummary matchSummary = extras.getParcelable(MatchFragment.EXTRA_MATCH_SUMMARY_ITEM);
        LinearLayout matchMainInfo = findViewById(R.id.matchMainInfo);

        ((TextView) findViewById(R.id.hostTeam)).setText(matchSummary.getMatch().getHostTeam().getFullName());
        ((TextView) findViewById(R.id.guestTeam)).setText(matchSummary.getMatch().getGuestTeam().getFullName());
        ((TextView) findViewById(R.id.score)).setText(matchSummary.getHostSets() + " : " + matchSummary.getGuestSets());
        ((TextView) findViewById(R.id.date)).setText(matchSummary.getDate());
        ((TextView) findViewById(R.id.matchNb)).setText(extras.getString(MatchFragment.EXTRA_MATCH_POSITION));

        matchMainInfo.setTransitionName(extras.getString(MatchFragment.EXTRA_TRANSITION_NAME));
    }
}
