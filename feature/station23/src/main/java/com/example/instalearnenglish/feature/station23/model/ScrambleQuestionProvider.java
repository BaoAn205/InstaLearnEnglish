package com.example.instalearnenglish.feature.station23.model;

import java.util.ArrayList;
import java.util.List;

public class ScrambleQuestionProvider {

    public static List<ScrambleQuestion> getQuestions() {
        List<ScrambleQuestion> questions = new ArrayList<>();

        questions.add(new ScrambleQuestion(
                "I would like to buy a single ticket",
                "Tôi muốn mua một vé lẻ."
        ));

        questions.add(new ScrambleQuestion(
                "Which platform does the train leave from",
                "Tàu khởi hành từ sân ga nào?"
        ));

        questions.add(new ScrambleQuestion(
                "Is this seat taken",
                "Ghế này có ai ngồi chưa?"
        ));
        
        questions.add(new ScrambleQuestion(
                "How much is a ticket to the city center",
                "Một vé đến trung tâm thành phố giá bao nhiêu?"
        ));

        return questions;
    }
}
