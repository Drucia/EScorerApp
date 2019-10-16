package com.druciak.escorerapp.model.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewUser {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("surname")
    @Expose
    private String surname;

    @SerializedName("isReferee")
    @Expose
    private boolean isReferee;

    @SerializedName("certificate")
    @Expose
    private String certificate;

    @SerializedName("refereeClass")
    @Expose
    private String refereeClass;

    public NewUser(String id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.isReferee = false;
        this.certificate = null;
        this.refereeClass = null;
    }

    public NewUser(String id, String name, String surname,
                   String certificate, String refereClass) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.isReferee = true;
        this.certificate = certificate;
        this.refereeClass = refereClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean getIsReferee() {
        return isReferee;
    }

    public void setIsReferee(boolean referee) {
        isReferee = referee;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getRefereeClass() {
        return refereeClass;
    }

    public void setRefereeClass(String refereClass) {
        this.refereeClass = refereClass;
    }

    public String getFullName() {
        return name + " " + surname;
    }
}
