package com.example.instalearnenglish.feature.station23.model;

import androidx.annotation.RawRes;

import java.util.List;

public class AnnouncementQuestion {
    @RawRes
    private final int audioFileResId;
    private final String question;
    private final List<String> answers;
    private final int correctAnswerIndex;

    public AnnouncementQuestion(@RawRes int audioFileResId, String question, List<String> answers, int correctAnswerIndex) {
        this.audioFileResId = audioFileResId;
        this.question = question;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public int getAudioFileResId() {
        return audioFileResId;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}
