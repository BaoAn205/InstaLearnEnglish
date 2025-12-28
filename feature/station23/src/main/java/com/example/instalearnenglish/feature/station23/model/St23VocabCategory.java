package com.example.instalearnenglish.feature.station23.model;

import java.util.List;

public class St23VocabCategory {
    private final String title;
    private final List<St23VocabItem> items;

    public St23VocabCategory(String title, List<St23VocabItem> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public List<St23VocabItem> getItems() {
        return items;
    }
}
