package com.druciak.escorerapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import static com.druciak.escorerapp.entities.MatchInfo.NO_CARD_ID;
import static com.druciak.escorerapp.entities.MatchInfo.YELLOW_AND_RED_CARD_SEPARATELY_ID;
import static com.druciak.escorerapp.entities.MatchInfo.YELLOW_AND_RED_CARD_TOGETHER_ID;

public class MatchPlayer extends Player implements Parcelable {
    public static final int STATUS_PLAYER_ON_COURT = 1;
    public static final int STATUS_PLAYER_ON_DESK = 2;
    public static final int STATUS_PLAYER_SHIFTED = 3;
    public static final int STATUS_PLAYER_NOT_TO_SHIFT = 4;
    public static final int NO_SHIFT = -1;

    private int statusId;
    private int shiftNumber;
    private boolean canPlay;
    private int cardId;
    private boolean isOnCourt;

    public MatchPlayer(Player player) {
        super(player);
        statusId = STATUS_PLAYER_ON_DESK;
        shiftNumber = NO_SHIFT;
        canPlay = true;
        isOnCourt = false;
        cardId = NO_CARD_ID;
    }

    protected MatchPlayer(Parcel in) {
        super(in);
        statusId = in.readInt();
        shiftNumber = in.readInt();
        canPlay = in.readByte() != 0;
        cardId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeInt((int) sex);
        dest.writeParcelable(team, flags);
        dest.writeByte((byte) (isLibero ? 1 : 0));
        dest.writeByte((byte) (isCaptain ? 1 : 0));
        dest.writeInt(number);

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

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(int shiftNumber) {
        this.shiftNumber = shiftNumber;
    }

    public boolean isCanPlay() {
        return canPlay;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
        if (cardId == YELLOW_AND_RED_CARD_TOGETHER_ID || cardId == YELLOW_AND_RED_CARD_SEPARATELY_ID)
            this.canPlay = false;
    }

    public boolean isOnCourt() {
        return isOnCourt;
    }

    public void setOnCourt(boolean onCourt) {
        isOnCourt = onCourt;
    }
}
