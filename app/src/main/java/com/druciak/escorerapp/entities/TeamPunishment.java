package com.druciak.escorerapp.entities;

import android.os.Parcel;

import java.util.Optional;

import static com.druciak.escorerapp.entities.MatchInfo.RED_CARD_ID;
import static com.druciak.escorerapp.entities.MatchInfo.TEAM_A_ID;
import static com.druciak.escorerapp.entities.MatchInfo.TEAM_B_ID;

public class TeamPunishment extends Action {
    private int cardId;
    private int teamId;
    private int set;

    public TeamPunishment(int cardId, MatchTeam team, int set, MatchTeam sndTeam){
        this.cardId = cardId;
        this.teamId = team.getTeamId();
        this.set = set;
        teamMadeActionPoints = team.getPoints();
        sndTeamPoints = sndTeam.getPoints();
        team.setCardId(cardId);
        if (cardId == RED_CARD_ID)
            sndTeam.addPoint();
    }

    protected TeamPunishment(Parcel in) {
        cardId = in.readInt();
        teamId = in.readInt();
        set = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cardId);
        dest.writeInt(teamId);
        dest.writeInt(set);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeamPunishment> CREATOR = new Creator<TeamPunishment>() {
        @Override
        public TeamPunishment createFromParcel(Parcel in) {
            return new TeamPunishment(in);
        }

        @Override
        public TeamPunishment[] newArray(int size) {
            return new TeamPunishment[size];
        }
    };

        @Override
    public Optional<Integer> returnTeamIdIfIsPoint() {
        if (cardId == RED_CARD_ID)
            return Optional.of(teamId == TEAM_A_ID ? TEAM_B_ID : TEAM_A_ID);
        else
            return Optional.empty();
    }

    @Override
    public String toString() {
        return "TeamPunishment{" +
                "cardId=" + cardId +
                ", teamId=" + teamId +
                ", set=" + set +
                ", score=" + super.toString() +
                '}';
    }
}
