package com.druciak.escorerapp.entities;

import android.os.Parcel;

import java.util.Optional;

import static com.druciak.escorerapp.entities.MatchInfo.RED_CARD_ID;
import static com.druciak.escorerapp.entities.MatchInfo.TEAM_A_ID;
import static com.druciak.escorerapp.entities.MatchInfo.TEAM_B_ID;

public class TeamPunishment extends Action {
    private int cardId;
    private int set;

    public TeamPunishment(int cardId, MatchTeam team, int set, MatchTeam sndTeam){
        this.cardId = cardId;
        this.set = set;
        teamMadeActionId = team.getTeamId();
        teamMadeActionPoints = team.getPoints();
        sndTeamPoints = sndTeam.getPoints();
        team.setCardId(cardId);
        if (cardId == RED_CARD_ID)
            sndTeam.addPoint();
    }

    protected TeamPunishment(Parcel in) {
        teamMadeActionId = in.readInt();
        teamMadeActionPoints = in.readInt();
        sndTeamPoints = in.readInt();

        cardId = in.readInt();
        set = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(teamMadeActionId);
        dest.writeInt(teamMadeActionPoints);
        dest.writeInt(sndTeamPoints);

        dest.writeInt(cardId);
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
            return Optional.of(teamMadeActionId == TEAM_A_ID ? TEAM_B_ID : TEAM_A_ID);
        else
            return Optional.empty();
    }

    @Override
    public String toString() {
        return "TeamPunishment{" +
                "cardId=" + cardId +
                ", set=" + set +
                ", score=" + super.toString() +
                '}';
    }
}
