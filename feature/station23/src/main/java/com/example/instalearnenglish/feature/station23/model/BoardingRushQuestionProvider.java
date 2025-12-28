package com.example.instalearnenglish.feature.station23.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoardingRushQuestionProvider {

    public static List<BoardingRushQuestion> getQuestions() {
        List<BoardingRushQuestion> allQuestions = new ArrayList<>();

        allQuestions.add(new BoardingRushQuestion("'Gate' có nghĩa là gì?", Arrays.asList("Cổng ra máy bay", "Ghế ngồi", "Hành lý"), 0));
        allQuestions.add(new BoardingRushQuestion("Đâu là 'hành lý xách tay'?", Arrays.asList("Checked baggage", "Carry-on baggage", "Excess baggage"), 1));
        allQuestions.add(new BoardingRushQuestion("Từ nào đồng nghĩa với 'delay'?", Arrays.asList("On time", "Cancelled", "Postponed"), 2));
        allQuestions.add(new BoardingRushQuestion("Bạn cần xuất trình giấy tờ gì tại quầy an ninh?", Arrays.asList("Ticket and Food", "Boarding pass and ID", "Book and phone"), 1));
        allQuestions.add(new BoardingRushQuestion("'Final call' có nghĩa là gì?", Arrays.asList("Chuyến bay bị hủy", "Cuộc gọi cuối cùng", "Chuyến bay đúng giờ"), 1));
        allQuestions.add(new BoardingRushQuestion("Đâu là nơi bạn nhận lại hành lý ký gửi?", Arrays.asList("Duty-free shop", "Baggage claim", "Lounge"), 1));
        allQuestions.add(new BoardingRushQuestion("'Departure' có nghĩa là gì?", Arrays.asList("Điểm đến", "Khởi hành", "Đến nơi"), 1));
        allQuestions.add(new BoardingRushQuestion("'Arrival' có nghĩa là gì?", Arrays.asList("Đến nơi", "Khởi hành", "Quá cảnh"), 0));
        allQuestions.add(new BoardingRushQuestion("Từ nào dùng để chỉ chuyến bay nội địa?", Arrays.asList("International flight", "Connecting flight", "Domestic flight"), 2));
        allQuestions.add(new BoardingRushQuestion("Bạn làm thủ tục cho chuyến bay ở đâu?", Arrays.asList("Check-in counter", "Lost and Found", "Food court"), 0));
        allQuestions.add(new BoardingRushQuestion("'Seat belt' là gì?", Arrays.asList("Áo phao", "Dây an toàn", "Mặt nạ dưỡng khí"), 1));
        allQuestions.add(new BoardingRushQuestion("Khi nào bạn phải tắt điện thoại?", Arrays.asList("During takeoff and landing", "Anytime", "Never"), 0));

        // Shuffle the list and return the first 10 questions to ensure variety
        Collections.shuffle(allQuestions);
        return new ArrayList<>(allQuestions.subList(0, 10));
    }
}
