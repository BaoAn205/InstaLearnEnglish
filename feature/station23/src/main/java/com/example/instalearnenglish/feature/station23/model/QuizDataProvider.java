package com.example.instalearnenglish.feature.station23.model;

import java.util.ArrayList;
import java.util.List;

public class QuizDataProvider {

    public static List<QuizQuestion> getStation3QuizQuestions() {
        List<QuizQuestion> questions = new ArrayList<>();

        questions.add(new QuizQuestion("Bạn cần đến đâu để lên tàu?", "Ticket machine", "Platform", true));
        questions.add(new QuizQuestion("Từ nào đồng nghĩa với 'tàu điện ngầm'?", "Subway", "Map", false));
        questions.add(new QuizQuestion("Cái gì dùng để mua vé tự động?", "Map", "Ticket machine", true));
        questions.add(new QuizQuestion("Bạn sẽ tìm thông tin chuyến tàu trên...?", "Bản đồ (Map)", "Bảng thông báo (Board)", true));
        questions.add(new QuizQuestion("Nếu bị lạc, bạn nên tìm cái gì đầu tiên?", "Bản đồ (Map)", "Sân ga (Platform)", false));

        return questions;
    }
}
