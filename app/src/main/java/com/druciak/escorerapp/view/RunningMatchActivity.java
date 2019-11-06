package com.druciak.escorerapp.view;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.druciak.escorerapp.R;
import com.druciak.escorerapp.interfaces.IRunningMatchMVP;
import com.druciak.escorerapp.model.entities.Match;
import com.druciak.escorerapp.model.entities.MatchInfo;
import com.druciak.escorerapp.model.entities.MatchPlayer;
import com.druciak.escorerapp.model.entities.MatchSettings;
import com.druciak.escorerapp.model.entities.MatchTeam;
import com.druciak.escorerapp.model.entities.Player;
import com.druciak.escorerapp.model.entities.Team;
import com.druciak.escorerapp.model.entities.TeamAdditionalMember;
import com.druciak.escorerapp.presenter.RunningMatchPresenter;
import com.druciak.escorerapp.view.matchSettings.PlayersAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.druciak.escorerapp.model.entities.MatchInfo.RED_CARD_ID;
import static com.druciak.escorerapp.model.entities.MatchInfo.TIME_LENGTH;
import static com.druciak.escorerapp.model.entities.MatchInfo.WARNING_ID;
import static com.druciak.escorerapp.model.entities.MatchInfo.YELLOW_CARD_ID;
import static com.druciak.escorerapp.model.entities.TeamAdditionalMember.COACH_MEMBER_ID;
import static com.druciak.escorerapp.model.entities.TeamAdditionalMember.MASSEUR_MEMBER_ID;
import static com.druciak.escorerapp.model.entities.TeamAdditionalMember.MEDICINE_MEMBER_ID;

public class RunningMatchActivity extends AppCompatActivity implements IRunningMatchMVP.IView{
    public static final int RIGHT_TEAM_ID = 1;
    public static final int LEFT_TEAM_ID = 2;

    private IRunningMatchMVP.IPresenter presenter;
    private PlayersAdapter adapterLeft;
    private PlayersAdapter adapterRight;
    private RecyclerView recyclerViewLeft;
    private RecyclerView recyclerViewRight;
    private TextView leftTeamName;
    private TextView rightTeamName;
    private TextView bigScore;
    private TextView leftSets;
    private TextView rightSets;
    private ImageView leftServe;
    private ImageView rightServe;
    private ImageView leftTime;
    private ImageView rightTime;
    private ImageView leftCards;
    private ImageView rightCards;
    private SpeedDialView speedDial;
    private View leftFirstTime;
    private View rightFirstTime;
    private View leftSndTime;
    private View rightSndTime;
    private LinearLayout cardsLayout;

    private ArrayList<MatchPlayer> playersLeft;
    private ArrayList<MatchPlayer> playersRight;

    // conversion from adapter position to area number
    public static final Map<Integer, Pair<Integer, String>> leftLineUpConversion =
            new HashMap<Integer, Pair<Integer, String>>(){{
               put(0, new Pair<>(5, "V"));
               put(1, new Pair<>(4, "IV"));
               put(2, new Pair<>(6, "VI"));
               put(3, new Pair<>(3, "III"));
               put(4, new Pair<>(1, "I"));
               put(5, new Pair<>(2, "II"));
    }};

    public static final Map<Integer, Pair<Integer, String>> rightLineUpConversion =
            new HashMap<Integer, Pair<Integer, String>>(){{
                put(0, new Pair<>(2, "II"));
                put(1, new Pair<>(1, "I"));
                put(2, new Pair<>(3, "III"));
                put(3, new Pair<>(6, "VI"));
                put(4, new Pair<>(4, "IV"));
                put(5, new Pair<>(5, "V"));
            }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_match);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//        Intent intent = getIntent();
//        MatchInfo info = intent.getParcelableExtra(MATCH_INFO_ID);

        // mocked match info
        MatchInfo matchInfo = mockMatchInfo();

        recyclerViewLeft = findViewById(R.id.leftPlayersRecyclerView);
        recyclerViewRight = findViewById(R.id.rightPlayersRecyclerView);
        leftTeamName = findViewById(R.id.leftTeamName);
        rightTeamName = findViewById(R.id.rightTeamName);
        bigScore = findViewById(R.id.score);
        leftSets = findViewById(R.id.leftSets);
        rightSets = findViewById(R.id.rightSets);
        leftServe = findViewById(R.id.leftServe);
        rightServe = findViewById(R.id.rightServe);
        leftTime = findViewById(R.id.hourglassLeft);
        rightTime = findViewById(R.id.hourglassRight);
        leftCards = findViewById(R.id.cardsLeft);
        rightCards = findViewById(R.id.cardsRight);
        speedDial = findViewById(R.id.speedDialRunningMatch);
        leftFirstTime = findViewById(R.id.timeCounterFirstLeft);
        rightFirstTime = findViewById(R.id.timeCounterFirstRight);
        leftSndTime = findViewById(R.id.timeCounterSndLeft);
        rightSndTime = findViewById(R.id.timeCounterSndRight);

        playersLeft = new ArrayList<>(Arrays.asList(null, null, null, null, null, null));
        playersRight = new ArrayList<>(Arrays.asList(null, null, null, null, null, null));
        adapterLeft = new PlayersAdapter(this, playersLeft);
        adapterRight = new PlayersAdapter(this, playersRight);
        adapterLeft.setTeamSideId(LEFT_TEAM_ID);
        adapterRight.setTeamSideId(RIGHT_TEAM_ID);
        recyclerViewLeft.setAdapter(adapterLeft);
        recyclerViewRight.setAdapter(adapterRight);
        recyclerViewLeft.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewRight.setLayoutManager(new GridLayoutManager(this, 2));

        speedDial.addActionItem(
                new SpeedDialActionItem.Builder(R.id.attentions, R.drawable.attention)
                        .setLabel("Uwagi")
                        .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.black_overlay, getTheme()))
                        .create());
        speedDial.addActionItem(
                new SpeedDialActionItem.Builder(R.id.returnAction, R.drawable.back)
                        .setLabel("Cofinj Akcję")
                        .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.black_overlay, getTheme()))
                        .create());
        speedDial.addActionItem(
                new SpeedDialActionItem.Builder(R.id.teamMembers, R.drawable.person)
                        .setLabel("Informacje")
                        .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.black_overlay, getTheme()))
                        .create());
        speedDial.addActionItem(
                new SpeedDialActionItem.Builder(R.id.finishMatch, R.drawable.end)
                        .setLabel("Zakończ")
                        .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.black_overlay, getTheme()))
                        .create());
        speedDial.setOnActionSelectedListener(actionItem -> {
            switch (actionItem.getId()){
                case R.id.attentions:
                    presenter.onAttentionsClicked();
                    break;
                case R.id.returnAction:
                    presenter.onReturnActionClicked();
                    break;
                case R.id.teamMembers:
                    presenter.onTeamsInfoClicked();
                    break;
                case R.id.finishMatch:
                    presenter.onFinishMatchClicked();
                    break;
            }
            return false;
        });

        MaterialButton rightPoint = findViewById(R.id.pointRight);
        MaterialButton leftPoint = findViewById(R.id.pointLeft);
        rightPoint.setOnClickListener(view -> presenter.onAddPointClicked(RIGHT_TEAM_ID));
        leftPoint.setOnClickListener(view -> presenter.onAddPointClicked(LEFT_TEAM_ID));
        rightTime.setOnClickListener(view -> presenter.onTimeClicked(RIGHT_TEAM_ID));
        leftTime.setOnClickListener(view -> presenter.onTimeClicked(LEFT_TEAM_ID));
        View.OnClickListener listener = view -> {
            cardsLayout = view.getId() == R.id.cardsLeft ? findViewById(R.id.leftLayoutCards)
                    : findViewById(R.id.rightLayoutCards);
            if (cardsLayout.getVisibility() == View.GONE) {
                int teamId = view.getId() == R.id.cardsLeft ? LEFT_TEAM_ID : RIGHT_TEAM_ID;
                cardsLayout.setVisibility(View.VISIBLE);
                ImageView delay = findViewById(view.getId() == R.id.cardsLeft ? R.id.leftDelay : R.id.rightDelay);
                ImageView behavior = findViewById(view.getId() == R.id.cardsLeft ? R.id.leftBehavior : R.id.rightBehavior);
                delay.setOnClickListener(view1 -> {presenter.onCardClicked(teamId, true);
                    cardsLayout.setVisibility(View.GONE);});
                behavior.setOnClickListener(view1 -> {presenter.onCardClicked(teamId, false);
                    cardsLayout.setVisibility(View.GONE);});
            } else
                cardsLayout.setVisibility(View.GONE);
        };
        leftCards.setOnClickListener(listener);
        rightCards.setOnClickListener(listener);

        findViewById(R.id.relativeLayout).setOnClickListener(view -> {
            if (cardsLayout != null && cardsLayout.getVisibility() == View.VISIBLE)
                cardsLayout.setVisibility(View.GONE);
        });

        presenter = new RunningMatchPresenter(this, matchInfo);
        presenter.onActivityCreated();
    }

    public void showPopUpWithConfirmTime(int teamId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Czas");
        builder.setMessage("Czy na pewno przyznać przerwę?");
        builder.setPositiveButton("TAK", (dialogInterface, i) -> {
            presenter.onTimeConfirmClicked(teamId);
            dialogInterface.dismiss();
        });
        builder.setNegativeButton("NIE", (dialogInterface, i) -> dialogInterface.cancel());
        builder.create().show();
    }

    @Override
    public void showPopUpWithTeamPunish(int teamSideId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kara");
        View root = getLayoutInflater().inflate(R.layout.pop_up_delay, null);
        ChipGroup group = root.findViewById(R.id.cardsChipGroup);
        builder.setView(root);
        // todo if time allow - filter cards
        builder.setPositiveButton("Zatwierdź", (dialogInterface, i) -> {
            int chipId = group.getCheckedChipId();
            if (chipId != -1)
            {
                switch (chipId)
                {
                    case R.id.chipWarning:
                        presenter.onPunishmentClicked(teamSideId, WARNING_ID);
                        break;
                    case R.id.chipYellow:
                        presenter.onPunishmentClicked(teamSideId, YELLOW_CARD_ID);
                        break;
                    case R.id.chipRed:
                        presenter.onPunishmentClicked(teamSideId, RED_CARD_ID);
                        break;
                }
                dialogInterface.dismiss();
            } else
            {
                Toast.makeText(RunningMatchActivity.this, "Wybierz karę",
                        Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Anuluj", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    @Override
    public void showPopUpWithMemberPunish(MatchTeam team)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Niewłaściwe zachowanie");
        View root = getLayoutInflater().inflate(R.layout.pop_up_behavior, null);
        ChipGroup memberGroup = root.findViewById(R.id.memberGroupMem);
        for (MatchPlayer player : team.getPlayers().stream()
                .sorted(Comparator.comparing(MatchPlayer::getNumber))
                .collect(Collectors.toList()))
        {
            Chip newChip = (Chip) getLayoutInflater().inflate(R.layout.default_chip, memberGroup, false);
            int number = player.getNumber();
            newChip.setId(number);
            newChip.setText(number < 10 ? " " + number + " " : String.valueOf(number));
            memberGroup.addView(newChip);
        }

        List<TeamAdditionalMember> members = team.getMembers().stream()
                .sorted(Comparator.comparingInt(TeamAdditionalMember::getMemberId))
                .collect(Collectors.toList());
        int coachCounter = 0;
        int medicineCounter = 0;
        int masseurCounter = 0;
        for (int i = 1; i <= members.size(); i++)
        {
            Chip newChip = (Chip) getLayoutInflater().inflate(R.layout.default_chip, memberGroup, false);
            newChip.setId(100+i);
            TeamAdditionalMember m = members.get(i-1);
            switch (m.getMemberId()) {
                case COACH_MEMBER_ID:
                    newChip.setText("C"+ (++coachCounter));
                    break;
                case MEDICINE_MEMBER_ID:
                    newChip.setText("M"+ (++medicineCounter));
                    break;
                case MASSEUR_MEMBER_ID:
                    newChip.setText("T"+ (++masseurCounter));
                    break;
            }
            memberGroup.addView(newChip);
        }

        ChipGroup group = root.findViewById(R.id.cardsChipGroupMem);
        builder.setView(root);
        // todo if time allow - filter cards
        builder.setPositiveButton("Zatwierdź", (dialogInterface, i) -> {
            int chipId = group.getCheckedChipId();
            if (chipId != -1)
            {
                dialogInterface.dismiss();
            } else
            {
                Toast.makeText(RunningMatchActivity.this, "Wybierz karę",
                        Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Anuluj", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    @Override
    public void resetAdapters() {
        for (int i = 0; i < MatchTeam.PLAYERS_ON_COURT; i++){
            playersLeft.set(i, null);
            playersRight.set(i, null);
            adapterLeft.notifyDataSetChanged();
            adapterRight.notifyDataSetChanged();
        }
    }

    @Override
    public void resetTimes() {
        leftFirstTime.setBackgroundResource(R.drawable.ring);
        leftSndTime.setBackgroundResource(R.drawable.ring);
        rightFirstTime.setBackgroundResource(R.drawable.ring);
        rightSndTime.setBackgroundResource(R.drawable.ring);
    }

    @Override
    public void showPopUpWithShift(List<MatchPlayer> players, int adapterPosition, int teamSideId,
                                   boolean isOnMatch) {
        final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.pop_up_functions, null);
        dialogBuilder.setView(view);
        dialogBuilder.setTitle("Wybierz zawodnika");
        ChipGroup group = view.findViewById(R.id.functionsChipGroup);
        group.setSingleSelection(true);

        for (Integer number : players.stream().map(Player::getNumber).collect(Collectors.toList()))
        {
            Chip newChip = (Chip) getLayoutInflater().inflate(R.layout.default_chip, group,
                    false);
            newChip.setId(number);
            newChip.setText(number < 10 ? " " + number + " " : String.valueOf(number));
            group.addView(newChip);
        }

        dialogBuilder.setPositiveButton(isOnMatch ? "Zmień" : "Ustaw", (dialogInterface, i) -> {
            int number = group.getCheckedChipId();
            Optional<MatchPlayer> playerToShift = players.stream()
                    .filter(player -> player.getNumber() == number).findAny();
            if (isOnMatch && playerToShift.isPresent()){
                presenter.chosenPlayerToShift(playerToShift.get(),
                        teamSideId == LEFT_TEAM_ID ? playersLeft.get(adapterPosition)
                                : playersRight.get(adapterPosition), teamSideId);
                dialogInterface.dismiss();
            } else if (!isOnMatch){
                Map<Integer, Pair<Integer, String>> conversion = teamSideId == LEFT_TEAM_ID ?
                        leftLineUpConversion : rightLineUpConversion;
                presenter.chosenPlayerToLineUp(playerToShift, teamSideId == LEFT_TEAM_ID ?
                                playersLeft.get(adapterPosition) : playersRight.get(adapterPosition),
                        conversion.get(adapterPosition).first, teamSideId);
                dialogInterface.dismiss();
            }
        });

        if (!isOnMatch){
            dialogBuilder.setNegativeButton("Anuluj", (dialogInterface, i) -> {
                presenter.cancelChoosePlayerToLineUp(teamSideId);
                dialogInterface.dismiss();
            });
        }

        dialogBuilder.create().show();
    }

    @Override
    public void makeChangeInAdapter(MatchPlayer playerOut, MatchPlayer playerIn, int teamSideId) {
        PlayersAdapter adapter = teamSideId == LEFT_TEAM_ID ? adapterLeft : adapterRight;
        ArrayList<MatchPlayer> players = teamSideId == LEFT_TEAM_ID ? playersLeft : playersRight;
        int idx = players.indexOf(playerOut);
        players.set(idx, playerIn);
        adapter.notifyItemChanged(idx);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPopUpWithConfirmLineUp(MatchTeam team) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ustawienie początkowe");
        builder.setMessage("Czy to jest ostateczne ustawienie początkowe zespołu " +
                team.getShortName() + "?");
        builder.setCancelable(false);
        builder.setPositiveButton("TAK", (dialogInterface, i) -> {
            presenter.onConfirmLineUp(true, team);
            dialogInterface.dismiss();
        });
        builder.setNegativeButton("NIE", (dialogInterface, i) -> {
            presenter.onConfirmLineUp(false, team);
            dialogInterface.dismiss();
        });
        builder.create().show();
    }

    @Override
    public void makeShiftInLineUp(int teamSideId) {
        PlayersAdapter adapter = teamSideId == RIGHT_TEAM_ID ? adapterRight : adapterLeft;
        ArrayList<MatchPlayer> players = teamSideId == RIGHT_TEAM_ID ? playersRight : playersLeft;
        MatchPlayer p = players.remove(2);
        players.add(0, p);
        adapter.notifyItemMoved(2, 0);
        p = players.remove(4);
        players.add(2, p);
        adapter.notifyItemMoved(4, 2);
        p = players.remove(5);
        players.add(4, p);
        adapter.notifyItemMoved(5, 4);
        changeServeTeam(teamSideId);
    }

    private void changeServeTeam(int teamWhichServe) {
        if (teamWhichServe == RIGHT_TEAM_ID) {
            rightServe.setVisibility(View.VISIBLE);
            leftServe.setVisibility(View.INVISIBLE);
        } else {
            rightServe.setVisibility(View.INVISIBLE);
            leftServe.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setScore(String score){
        bigScore.setText(score);
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
                new TeamAdditionalMember("Marek Olczyk", 2, COACH_MEMBER_ID),
                new TeamAdditionalMember("Marcin Dąbrowski", 2, MEDICINE_MEMBER_ID),
                new TeamAdditionalMember("Magda Sadowska", 1, COACH_MEMBER_ID))));
        matchSettings.setLineReferees(new ArrayList<>());

        return new MatchInfo(polonia, poloniaPlayers, chelmiec, chelmiecPlayers, false, matchSettings);
    }

    @Override
    public void updateAdapterWithPlayersLineUp(MatchPlayer player, int areaNb, int teamSideId){
        Map<Integer, Pair<Integer, String>> conversion = teamSideId == LEFT_TEAM_ID ?
                leftLineUpConversion : rightLineUpConversion;
        int adapterPosition = conversion.entrySet().stream().filter(entry ->
                entry.getValue().first == areaNb).map(Map.Entry::getKey).findAny().get();
        PlayersAdapter adapter = teamSideId == LEFT_TEAM_ID ? adapterLeft : adapterRight;
        ArrayList<MatchPlayer> players = teamSideId == LEFT_TEAM_ID ? playersLeft : playersRight;
        players.set(adapterPosition, player);
        adapter.notifyItemChanged(adapterPosition);
    }

    @Override
    public void onPlayerClicked(Object player, int adapterPosition, PlayersAdapter adapter) {
        MatchPlayer mPlayer = (MatchPlayer) player;
        presenter.onPlayerClicked(mPlayer, adapterPosition, adapter.getTeamSideId());
    }

    @Override
    public void showPopUpWithEndOf(String winner, String title, boolean isEnd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage((isEnd ? "Mecz" : "Set") + " wygrała drużyna: " + winner);
        builder.setCancelable(false);
        builder.setPositiveButton(isEnd ? "Podsumowanie" : "Kolejny set", (dialogInterface, i) -> {
            presenter.onNextSetClicked();
            dialogInterface.dismiss();});
        builder.create().show();
    }

    @Override
    public void setFields(String fullNameLeft, String fullNameRight, int teamId) {
        leftTeamName.setText(fullNameLeft);
        rightTeamName.setText(fullNameRight);
        changeServeTeam(teamId);
    }

    @Override
    public void setSets(int setsLeft, int setsRight) {
        leftSets.setText(String.valueOf(setsLeft));
        rightSets.setText(String.valueOf(setsRight));
    }

    @Override
    public void showPopUpWithInfo(String title, String msg, String positiveButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveButton, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    @Override
    public void addTimeFor(int teamId, int timeCount) {
        if (teamId == LEFT_TEAM_ID){
            if (timeCount == 1)
                leftFirstTime.setBackgroundResource(R.drawable.circle_with_ring);
            else
                leftSndTime.setBackgroundResource(R.drawable.circle_with_ring);
        } else {
            if (timeCount == 1)
                rightFirstTime.setBackgroundResource(R.drawable.circle_with_ring);
            else
                rightSndTime.setBackgroundResource(R.drawable.circle_with_ring);
        }
    }

    @Override
    public void showTimeCountDown(String teamName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Czas dla " + teamName);
        View root = getLayoutInflater().inflate(R.layout.pop_up_msg, null);
        TextView msg = root.findViewById(R.id.msg);
        msg.setTextSize(16);
        builder.setView(root);
        builder.setCancelable(false);
        Dialog dialog = builder.create();
        new CountDownTimer(TIME_LENGTH * 1000, 1000){

            @Override
            public void onTick(long l) {
                msg.setText("Pozostało: " + (l / 1000) + "s.");
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        }.start();
        dialog.show();
    }
}
