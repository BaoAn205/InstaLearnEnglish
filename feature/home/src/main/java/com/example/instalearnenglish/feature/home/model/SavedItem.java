package com.example.instalearnenglish.feature.home.model;

public class SavedItem {
    private String id;
    private String title;
    private String type; // "TIP" or "VOCAB"
    private int stationId;
    private int position;

    public SavedItem() {}

    public SavedItem(String id, String title, String type, int stationId, int position) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.stationId = stationId;
        this.position = position;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public int getStationId() { return stationId; }
    public int getPosition() { return position; }
}
