package com.example.instalearnenglish.feature.station23.model;

import com.example.instalearnenglish.feature.station23.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnouncementQuestionProvider {

    public static List<AnnouncementQuestion> getQuestions() {
        List<AnnouncementQuestion> questions = new ArrayList<>();

        // THE FIX: Point to the actual MP3 files you've added.

        // Question 1
        questions.add(new AnnouncementQuestion(
                R.raw.announcement_1,
                "Chuyến bay này đi đâu?",
                Arrays.asList("Singapore", "Paris", "New York"),
                0 // Singapore is the correct answer
        ));

        // Question 2
        questions.add(new AnnouncementQuestion(
                R.raw.announcement_2,
                "Cổng ra máy bay là số mấy?",
                Arrays.asList("Gate 10", "Gate 12", "Gate 20"),
                1 // Gate 12 is the correct answer
        ));

        // Question 3
        questions.add(new AnnouncementQuestion(
                R.raw.announcement_3,
                "Hãng hàng không nào đang gọi hành khách?",
                Arrays.asList("Vietnam Airlines", "British Airways", "Emirates"),
                1 // British Airways is the correct answer
        ));

        return questions;
    }
}
