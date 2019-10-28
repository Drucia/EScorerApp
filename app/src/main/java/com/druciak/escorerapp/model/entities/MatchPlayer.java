package com.druciak.escorerapp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import static com.druciak.escorerapp.model.entities.MatchInfo.NO_CARD_ID;

public class MatchPlayer implements Parcelable {
    public static final int STATUS_PLAYER_ON_COURT = 1;
    public static final int STATUS_PLAYER_ON_DESK = 2;
    public static final int NO_SHIFT = -1;

    private Player player;
    private int statusId;
    private int shiftNumber;
    private boolean canPlay;
    private int cardId;

    public MatchPlayer(Player player) {
        this.player = player;
        statusId = STATUS_PLAYER_ON_DESK;
        shiftNumber = NO_SHIFT;
        canPlay = true;
        cardId = NO_CARD_ID;
    }

    protected MatchPlayer(Parcel in) {
        player = in.readParcelable(Player.class.getClassLoader());
        statusId = in.readInt();
        shiftNumber = in.readInt();
        canPlay = in.readByte() != 0;
        cardId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(player, flags);
        dest.writeInt(statusId);
        dest.writeInt(shiftNumber);
        dest.writeByte((byte) (canPlay ? 1 : 0));
        dest.writeInt(cardId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MatchPlayer> CREATOR = new Creator<MatchPlayer>() {
        @Override
        public MatchPlayer createFromParcel(Parcel in) {
            return new MatchPlayer(in);
        }

        @Override
        public MatchPlayer[] newArray(int size) {
            return new MatchPlayer[size];
        }
    };
}
