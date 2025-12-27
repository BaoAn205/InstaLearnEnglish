package com.example.instalearnenglish.feature.station5.model;

import java.util.List;

public class VocabCategory {
    private String title;
    private List<VocabItem> items;

    public VocabCategory() {}

    public VocabCategory(String title, List<VocabItem> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() { return title; }
    public List<VocabItem> getItems() { return items; }
}
