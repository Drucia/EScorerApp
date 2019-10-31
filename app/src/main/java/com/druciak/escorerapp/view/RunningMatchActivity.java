package com.druciak.escorerapp.view;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IRunningMatchMVP;
import com.druciak.escorerapp.model.entities.Match;
import com.druciak.escorerapp.model.entities.MatchInfo;
import com.druciak.escorerapp.model.entities.MatchPlayer;
import com.druciak.escorerapp.model.entities.MatchSettings;
import com.druciak.escorerapp.model.entities.Player;
import com.druciak.escorerapp.model.entities.Team;
import com.druciak.escorerapp.model.entities.TeamAdditionalMember;
import com.druciak.escorerapp.presenter.RunningMatchPresenter;
import com.druciak.escorerapp.view.matchSettings.PlayersAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunningMatchActivity extends AppCompatActivity implements IRunningMatchMVP.IView{
    private static final int RIGHT_LINE_UP_ID = 1;
    private static final int LEFT_LINE_UP_ID = 2;

    private static final HashMap<Integer, Integer> positionChange = new HashMap<Integer, Integer>(){{
        put(1, 3);
        put(3, 5);
        put(5, 4);
        put(4, 2);
        put(2, 0);
        put(0, 1);
    }};

    private IRunningMatchMVP.IPresenter presenter;
    private PlayersAdapter adapterLeft;
    private PlayersAdapter adapterRight;
    private RecyclerView recyclerViewLeft;
    private RecyclerView recyclerViewRight;

    private ArrayList<MatchPlayer> playersLeft;
    private ArrayList<MatchPlayer> playersRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_match);

//        Intent intent = getIntent();
//        MatchInfo info = intent.getParcelableExtra(MATCH_INFO_ID);

        // mocked match info
        MatchInfo matchInfo = mockMatchInfo();

        recyclerViewLeft = findViewById(R.id.leftPlayersRecyclerView);
        recyclerViewRight = findViewById(R.id.rightPlayersRecyclerView);
        MaterialButton rightPoint = findViewById(R.id.pointRight);
        MaterialButton leftPoint = findViewById(R.id.pointLeft);
        rightPoint.setOnClickListener(view -> makeShift(RIGHT_LINE_UP_ID));
        leftPoint.setOnClickListener(view -> makeShift(LEFT_LINE_UP_ID));

        presenter = new RunningMatchPresenter(this, matchInfo);
        presenter.onActivityCreated();
    }

    private void makeShift(int lineUpId) {
        PlayersAdapter adapter = lineUpId == RIGHT_LINE_UP_ID ? adapterRight : adapterLeft;
        ArrayList<MatchPlayer> players = lineUpId == RIGHT_LINE_UP_ID ? playersRight : playersLeft;
        MatchPlayer p = players.remove(2);
        players.add(0, p);
        adapter.notifyItemMoved(2, 0);
        p = players.remove(4);
        players.add(2, p);
        adapter.notifyItemMoved(4, 2);
        p = players.remove(5);
        players.add(4, p);
        adapter.notifyItemMoved(5, 4);
    }

    @Override
    public void showPopUpForLineUp(String teamName, List<MatchPlayer> players, boolean isFirst) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.pop_up_line_up);
        builder.setTitle(teamName);
        builder.setCancelable(false);
        builder.setPositiveButton("Zatwierdź", (dialogInterface, i) -> {
            // set players todo
            dialogInterface.dismiss();
            if (isFirst)
                presenter.onFirstLineUpSet(); // todo
            else
                presenter.onSecondLineUpSet(); // todo
        });
        builder.create().show();
    }

    private MatchInfo mockMatchInfo() {
        Team polonia = new Team(1, "Polonia", "MKS Polonia Świdnica");
        Team chelmiec = new Team(2, "Chełmiec", "MKS Chełmiec Wodociągi Wałbrzych");

        // prepare players
        ArrayList<Player> poloniaPlayers = new ArrayList<>();
        poloniaPlayers.add(new Player( "Aleksandra", "Druciak", polonia, false, true, 1));
        poloniaPlayers.add(new Player( "Aleksandra", "Nowak", polonia, false, false, 2));
        poloniaPlayers.add(new Player( "Aleksandra", "Kowalska", polonia, false, false, 3));
        poloniaPlayers.add(new Player( "Aleksandra", "Drucia", polonia, false, false, 4));
        poloniaPlayers.add(new Player( "Paulina", "Druciak", polonia, false, false, 5));
        poloniaPlayers.add(new Player( "Karolina", "Druciak", polonia, false, false, 7));

        // prepare players
        ArrayList<Player> chelmiecPlayers = new ArrayList<>();
        chelmiecPlayers.add(new Player( "Aleksandra", "Druciak", chelmiec, false,false, 10));
        chelmiecPlayers.add(new Player( "Aleksandra", "Nowak", chelmiec, false, false, 12));
        chelmiecPlayers.add(new Player( "Aleksandra", "Kowalska", chelmiec, false, false, 13));
        chelmiecPlayers.add(new Player( "Aleksandra", "Drucia", chelmiec, false, false, 14));
        chelmiecPlayers.add(new Player( "Paulina", "Druciak", chelmiec, false, false, 15));
        chelmiecPlayers.add(new Player( "Karolina", "Druciak", chelmiec, false, false, 16));
        chelmiecPlayers.add(new Player( "Kar", "Druci", chelmiec, true, false, 19));
        chelmiecPlayers.add(new Player( "Karo", "Druciak", chelmiec, false, true, 99));

        MatchSettings matchSettings = new MatchSettings();
        matchSettings.setTournamentName("Liga Kadetów Grupa B");
        matchSettings.setTown("Wałbrzych");
        matchSettings.setStreet("Paderewskiego 36");
        matchSettings.setHall("SP 3");
        matchSettings.setRefereeFirst("Aleksandra Całka");
        matchSettings.setRefereeFirst("Mateusz Iwańczak");
        matchSettings.setType("Kadeci");
        matchSettings.setMan(true);
        matchSettings.setZas(true);
        matchSettings.setMatch(new Match(polonia, chelmiec, "5YAYc3t7jWea1SnDXYyQEH3gQ1p1"));
        List<Player> players = new ArrayList<>();
        players.addAll(poloniaPlayers);
        players.addAll(chelmiecPlayers);
        matchSettings.setPlayers(players);
        matchSettings.setMembers(new ArrayList<>(Arrays.asList(
                new TeamAdditionalMember("Marek Olczyk", 2, TeamAdditionalMember.COACH_MEMBER_ID),
                new TeamAdditionalMember("Marcin Dąbrowski", 2, TeamAdditionalMember.MEDICINE_MEMBER_ID),
                new TeamAdditionalMember("Magda Sadowska", 1, TeamAdditionalMember.COACH_MEMBER_ID))));
        matchSettings.setLineReferees(new ArrayList<>());

        MatchInfo matchInfo = new MatchInfo(polonia, poloniaPlayers, chelmiec, chelmiecPlayers, false, matchSettings);
        Map<Integer, MatchPlayer> lineUpA = new HashMap<>();
        lineUpA.put(1, matchInfo.getTeamA().getPlayerByNumber(1));
        lineUpA.put(2, matchInfo.getTeamA().getPlayerByNumber(2));
        lineUpA.put(3, matchInfo.getTeamA().getPlayerByNumber(3));
        lineUpA.put(4, matchInfo.getTeamA().getPlayerByNumber(4));
        lineUpA.put(5, matchInfo.getTeamA().getPlayerByNumber(5));
        lineUpA.put(6, matchInfo.getTeamA().getPlayerByNumber(7));
        matchInfo.setTeamLineUp(MatchInfo.TEAM_A_ID, lineUpA);
        Map<Integer, MatchPlayer> lineUpB = new HashMap<>();
        lineUpB.put(1, matchInfo.getTeamB().getPlayerByNumber(10));
        lineUpB.put(2, matchInfo.getTeamB().getPlayerByNumber(12));
        lineUpB.put(3, matchInfo.getTeamB().getPlayerByNumber(13));
        lineUpB.put(4, matchInfo.getTeamB().getPlayerByNumber(15));
        lineUpB.put(5, matchInfo.getTeamB().getPlayerByNumber(16));
        lineUpB.put(6, matchInfo.getTeamB().getPlayerByNumber(99));
        matchInfo.setTeamLineUp(MatchInfo.TEAM_B_ID, lineUpB);
        return matchInfo;
    }

    @Override
    public void setAdapterWithPlayersLineUp(Map<Integer, MatchPlayer> lineUpLeft, Map<Integer, MatchPlayer> lineUpRight) {
        playersLeft = new ArrayList<>(lineUpLeft.values());
        playersRight = new ArrayList<>(lineUpRight.values());
        adapterLeft = new PlayersAdapter(this, playersLeft);
        adapterRight = new PlayersAdapter(this, playersRight);
        recyclerViewLeft.setAdapter(adapterLeft);
        recyclerViewRight.setAdapter(adapterRight);
        recyclerViewLeft.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewRight.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    public void onPlayerClicked(Object player, int adapterPosition) {
    }
}
