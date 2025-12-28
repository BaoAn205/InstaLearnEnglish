package com.example.instalearnenglish.feature.station23.model;

import java.util.ArrayList;
import java.util.List;

public class MatchGameDataProvider {

    public static List<St23VocabItem> getStation3MatchData() {
        List<St23VocabItem> vocabList = new ArrayList<>();
        
        // Updated constructor to match the new St23VocabItem signature
        vocabList.add(new St23VocabItem("Subway", "", 0, "Tàu điện ngầm", ""));
        vocabList.add(new St23VocabItem("Ticket machine", "", 0, "Máy bán vé", ""));
        vocabList.add(new St23VocabItem("Platform", "", 0, "Sân ga", ""));
        vocabList.add(new St23VocabItem("Map", "", 0, "Bản đồ", ""));
        vocabList.add(new St23VocabItem("One-way ticket", "", 0, "Vé một chiều", ""));
        vocabList.add(new St23VocabItem("Round trip", "", 0, "Vé khứ hồi", ""));

        return vocabList;
    }
}
