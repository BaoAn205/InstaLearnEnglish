package com.example.instalearnenglish.feature.station23.model;

import com.example.instalearnenglish.feature.station23.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class St23VocabProvider {

    public static List<St23VocabCategory> getStation2CategorizedVocabulary() {
        List<St23VocabCategory> categoryList = new ArrayList<>();

        // Category 1: At the Airport
        List<St23VocabItem> atAirport = Arrays.asList(
            new St23VocabItem("Airport", "/ˈeərpɔːrt/", R.drawable.ic_airport, "Sân bay", "We need to be at the airport two hours before our flight."),
            new St23VocabItem("Terminal", "/ˈtɜːrmɪnl/", R.drawable.ic_terminal, "Nhà ga", "Our flight departs from Terminal 2."),
            new St23VocabItem("Check-in desk", "/ˈtʃek ɪn desk/", R.drawable.ic_check_in_desk, "Quầy làm thủ tục", "Please go to the check-in desk to get your boarding pass.")
        );
        categoryList.add(new St23VocabCategory("At the Airport", atAirport));

        // Category 2: Security
        List<St23VocabItem> security = Arrays.asList(
            new St23VocabItem("Security check", "/sɪˈkjʊrəti tʃek/", R.drawable.ic_security_check, "Kiểm tra an ninh", "You have to go through the security check."),
            new St23VocabItem("Scanner", "/ˈskænər/", R.drawable.ic_scanner, "Máy soi", "Place your bags in the scanner.")
        );
        categoryList.add(new St23VocabCategory("Security", security));

        return categoryList;
    }

    public static List<St23VocabCategory> getStation3CategorizedVocabulary() {
        List<St23VocabCategory> categoryList = new ArrayList<>();

        // Category 1: Public Transport
        List<St23VocabItem> publicTransport = Arrays.asList(
            new St23VocabItem("Bus", "/bʌs/", R.drawable.ic_bus, "Xe buýt", "The bus is a cheap way to travel."),
            new St23VocabItem("Train", "/treɪn/", R.drawable.ic_train, "Tàu hỏa", "I take the train to work every day."),
            new St23VocabItem("Subway", "/ˈsʌbweɪ/", R.drawable.ic_subway, "Tàu điện ngầm", "The subway is very crowded in the morning.")
        );
        categoryList.add(new St23VocabCategory("Public Transport", publicTransport));

        return categoryList;
    }
}
