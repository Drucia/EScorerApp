package com.druciak.escorerapp.model.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Team {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("fullName")
    @Expose
    public String fullName;

    @SerializedName("shortName")
    @Expose
    public String shortName;

    public Team(int id, String fullName, String shortName) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
    }
}
