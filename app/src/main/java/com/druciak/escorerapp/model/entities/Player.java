package com.druciak.escorerapp.model.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("fullName")
    @Expose
    public String fullName;

    @SerializedName("sex")
    @Expose
    public char sex;

    @SerializedName("team")
    @Expose
    public Team team;
}
