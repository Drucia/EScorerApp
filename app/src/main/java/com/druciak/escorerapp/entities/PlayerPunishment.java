package com.druciak.escorerapp.entities;

import android.os.Parcel;

public final class PlayerPunishment extends TeamPunishment {
    public static final String COACH_ID = "C";

    private int memberId;

    public PlayerPunishment(int cardId, MatchTeam team, TeamAdditionalMember member,
                            MatchTeam sndTeam, int set) {
        super(cardId, team, set, sndTeam);
        this.memberId = member.getMemberTypeId();
    }

    public PlayerPunishment(int cardId, MatchTeam team, MatchPlayer player,
                            MatchTeam sndTeam, int set) {
        super(cardId, team, set, sndTeam);
        this.memberId = player.getNumber();
        player.setCardId(cardId);
    }

    protected PlayerPunishment(Parcel in) {
        super(in);
        memberId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(memberId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlayerPunishment> CREATOR = new Creator<PlayerPunishment>() {
        @Override
        public PlayerPunishment createFromParcel(Parcel in) {
            return new PlayerPunishment(in);
        }

        @Override
        public PlayerPunishment[] newArray(int size) {
            return new PlayerPunishment[size];
        }
    };

    @Override
    public String toString() {
        return "PlayerPunishment{" +
                "memberId=" + memberId +
                ", teamPun=" + super.toString() +
                '}';
    }
}
