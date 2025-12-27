package com.example.instalearnenglish.feature.station4.model;

import java.util.List;

public class VocabCategory {
    private final String title;
    private final List<VocabItem> items;

    public VocabCategory(String title, List<VocabItem> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public List<VocabItem> getItems() {
        return items;
    }
}
