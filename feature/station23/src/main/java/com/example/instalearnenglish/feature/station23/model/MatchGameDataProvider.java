package com.example.instalearnenglish.feature.station23.model;

import java.util.ArrayList;
import java.util.List;

public class MatchGameDataProvider {

    public static List<St23VocabItem> getStation3MatchData() {
        List<St23VocabItem> vocabList = new ArrayList<>();
        
        vocabList.add(new St23VocabItem("Subway", "Tàu điện ngầm"));
        vocabList.add(new St23VocabItem("Ticket machine", "Máy bán vé"));
        vocabList.add(new St23VocabItem("Platform", "Sân ga"));
        vocabList.add(new St23VocabItem("Map", "Bản đồ"));
        vocabList.add(new St23VocabItem("One-way ticket", "Vé một chiều"));
        vocabList.add(new St23VocabItem("Round trip", "Vé khứ hồi"));

        return vocabList;
    }
}
