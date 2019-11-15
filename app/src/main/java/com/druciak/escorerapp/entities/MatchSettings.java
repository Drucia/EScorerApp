package com.druciak.escorerapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MatchSettings implements Parcelable {
    private String tournamentName;
    private String town;
    private String street;
    private String hall;
    private String refereeFirst;
    private String refereeSnd;
    private String type;
    private boolean isMan;
    private boolean isFin;
    private Match match;
    private String startTime;
    private List<Player> players;
    private List<TeamAdditionalMember> members;
    private List<String> lineReferees;

    public MatchSettings() {
    }

    protected MatchSettings(Parcel in) {
        tournamentName = in.readString();
        town = in.readString();
        street = in.readString();
        hall = in.readString();
        refereeFirst = in.readString();
        refereeSnd = in.readString();
        type = in.readString();
        isMan = in.readByte() != 0;
        isFin = in.readByte() != 0;
        match = in.readParcelable(Match.class.getClassLoader());
        startTime = in.readString();
        players = in.createTypedArrayList(Player.CREATOR);
        members = in.createTypedArrayList(TeamAdditionalMember.CREATOR);
        lineReferees = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tournamentName);
        dest.writeString(town);
        dest.writeString(street);
        dest.writeString(hall);
        dest.writeString(refereeFirst);
        dest.writeString(refereeSnd);
        dest.writeString(type);
        dest.writeByte((byte) (isMan ? 1 : 0));
        dest.writeByte((byte) (isFin ? 1 : 0));
        dest.writeParcelable(match, flags);
        dest.writeString(startTime);
        dest.writeTypedList(players);
        dest.writeTypedList(members);
        dest.writeStringList(lineReferees);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MatchSettings> CREATOR = new Creator<MatchSettings>() {
        @Override
        public MatchSettings createFromParcel(Parcel in) {
            return new MatchSettings(in);
        }

        @Override
        public MatchSettings[] newArray(int size) {
            return new MatchSettings[size];
        }
    };

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public String getTown() {
        return town;
    }

    public String getStreet() {
        return street;
    }

    public String getHall() {
        return hall;
    }

    public String getRefereeFirst() {
        return refereeFirst;
    }

    public String getRefereeSnd() {
        return refereeSnd;
    }

    public String getType() {
        return type;
    }

    public boolean isMan() {
        return isMan;
    }

    public boolean isFin() {
        return isFin;
    }

    public Match getMatch() {
        return match;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<TeamAdditionalMember> getMembers() {
        return members;
    }

    public List<String> getLineReferees() {
        return lineReferees;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public void setRefereeFirst(String refereeFirst) {
        this.refereeFirst = refereeFirst;
    }

    public void setRefereeSnd(String refereeSnd) {
        this.refereeSnd = refereeSnd;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMan(boolean man) {
        isMan = man;
    }

    public void setFin(boolean fin) {
        isFin = fin;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setMembers(List<TeamAdditionalMember> members) {
        this.members = members;
    }

    public void setLineReferees(List<String> lineReferees) {
        this.lineReferees = lineReferees;
    }
}
