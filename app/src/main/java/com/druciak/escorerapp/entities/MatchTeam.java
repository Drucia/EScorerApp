package com.druciak.escorerapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.druciak.escorerapp.entities.MatchInfo.MAX_SHIFTS_AMOUNT;
import static com.druciak.escorerapp.entities.MatchInfo.MAX_TIMES_AMOUNT;
import static com.druciak.escorerapp.entities.MatchInfo.TEAM_A_ID;
import static com.druciak.escorerapp.entities.MatchInfo.YELLOW_AND_RED_CARD_SEPARATELY_ID;
import static com.druciak.escorerapp.view.runningMatch.RunningMatchActivity.LEFT_TEAM_ID;
import static com.druciak.escorerapp.view.runningMatch.RunningMatchActivity.RIGHT_TEAM_ID;

public class MatchTeam extends Team implements Parcelable {
    public static final int PLAYERS_ON_COURT = 6;

    private List<MatchPlayer> players;
    private List<TeamAdditionalMember> members;
    private Map<Integer, MatchPlayer> lineUp;
    private int points;
    private int sets;
    private int teamId; // team A or team B
    private int teamSideId; // left or right
    private int timesCounter;
    private int shiftCounter;
    private boolean isLineUpSet;
    private int cardId;

    public MatchTeam(Team team, List<Player> players, int teamId, List<TeamAdditionalMember> members) {
        super(team);
        this.players = players.stream().map(MatchPlayer::new).collect(Collectors.toList());
        lineUp = new HashMap<>();
        points = 0;
        sets = 0;
        this.teamId = teamId;
        this.teamSideId = teamId == TEAM_A_ID ? LEFT_TEAM_ID : RIGHT_TEAM_ID;
        this.isLineUpSet = false;
        this.members = members.stream().filter(member -> member.getTeamId() == team.getId()).
                collect(Collectors.toList());
    }

    protected MatchTeam(Parcel in) {
        super(in);
        players = in.createTypedArrayList(MatchPlayer.CREATOR);
        members = in.createTypedArrayList(TeamAdditionalMember.CREATOR);
        int size = in.readInt();
        lineUp = new HashMap<>();
        for (int i = 0; i < size; i++)
        {
            int key = in.readInt();
            lineUp.put(key, in.readParcelable(MatchPlayer.class.getClassLoader()));
        }
        points = in.readInt();
        sets = in.readInt();
        teamId = in.readInt();
        teamSideId = in.readInt();
        timesCounter = in.readInt();
        shiftCounter = in.readInt();
        isLineUpSet = in.readByte() != 0;
        cardId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fullName);
        dest.writeString(shortName);

        dest.writeTypedList(players);
        dest.writeTypedList(members);
        dest.writeInt(lineUp.size());
        for (Integer key : lineUp.keySet())
        {
            dest.writeInt(key);
            dest.writeParcelable(lineUp.get(key), flags);
        }
        dest.writeInt(points);
        dest.writeInt(sets);
        dest.writeInt(teamId);
        dest.writeInt(teamSideId);
        dest.writeInt(timesCounter);
        dest.writeInt(shiftCounter);
        dest.writeByte((byte) (isLineUpSet ? 1 : 0));
        dest.writeInt(cardId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MatchTeam> CREATOR = new Creator<MatchTeam>() {
        @Override
        public MatchTeam createFromParcel(Parcel in) {
            return new MatchTeam(in);
        }

        @Override
        public MatchTeam[] newArray(int size) {
            return new MatchTeam[size];
        }
    };

    public List<TeamAdditionalMember> getMembers() {
        return members;
    }

    public boolean getIsLineUpSet() {
        return isLineUpSet;
    }

    public int getTeamSideId() {
        return teamSideId;
    }

    public void setTeamSideId(int teamSideId) {
        this.teamSideId = teamSideId;
    }

    public void addTime(){timesCounter += 1;}

    public void addShift(){shiftCounter += 1;}

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public boolean canGetTime() {return timesCounter < MAX_TIMES_AMOUNT;}
    public boolean canGetShift() {return shiftCounter < MAX_SHIFTS_AMOUNT;}

    public int getTimesCounter() {
        return timesCounter;
    }

    public void addPoint(){points += 1;}

    public int getPoints() {
        return points;
    }

    public int getShiftCounter() {
        return shiftCounter;
    }

    public int getSets() {
        return sets;
    }

    public void addSet() {
        sets += 1;
    }

    public int getTeamId() {
        return teamId;
    }

    public List<MatchPlayer> getPlayers() {
        return players;
    }

    public Map<Integer, MatchPlayer> getLineUp() {
        return lineUp;
    }

    public void setLineUp(Map<Integer, MatchPlayer> lineUp) {
        this.lineUp = lineUp;
    }

    public MatchPlayer getPlayerByNumber(int number){
        Optional<MatchPlayer> player = players.stream()
                .filter(matchPlayer -> matchPlayer.getNumber() == number).findAny();

        return player.orElse(null);
    }

    public void nextSet() {
        this.points = 0;
        this.timesCounter = 0;
        this.shiftCounter = 0;
        this.isLineUpSet = false;
        for (MatchPlayer player : players) {
            player.setStatusId(MatchPlayer.STATUS_PLAYER_ON_DESK);
            if (player.getCardId() != YELLOW_AND_RED_CARD_SEPARATELY_ID && !player.isCanPlay())
                player.setCanPlay(true);
            player.setOnCourt(false);
        }
        this.lineUp = new HashMap<>();
    }

    public void setLineUp(int areaNb, MatchPlayer player) {
        if (player == null){
            lineUp.remove(areaNb);
        } else {
            lineUp.put(areaNb, player);
        }
        isLineUpSet = lineUp.size() == PLAYERS_ON_COURT;
    }

    public void setIsLineUpSet(boolean isSetLineUp) {
        this.isLineUpSet = isSetLineUp;
    }

    public void makeShift() {
        List<MatchPlayer> players = new ArrayList<>(lineUp.values());
        MatchPlayer p = players.remove(0);
        players.add(p);

        for (int i = 0; i < players.size(); i++)
            lineUp.put(i +1 , players.get(i));
    }

    public void makeRevertShift() {
        List<MatchPlayer> players = new ArrayList<>(lineUp.values());
        MatchPlayer p = players.remove(players.size()-1);
        players.add(0, p);

        for (int i = 0; i < players.size(); i++)
            lineUp.put(i +1 , players.get(i));
    }
}
