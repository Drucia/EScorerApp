package com.druciak.escorerapp.entities;

public class GameType {
    private int id;
    private String name;
    private int imageId;
    private boolean isAvailable;

    public GameType(int id, String name, int imageId, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.imageId = imageId;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
