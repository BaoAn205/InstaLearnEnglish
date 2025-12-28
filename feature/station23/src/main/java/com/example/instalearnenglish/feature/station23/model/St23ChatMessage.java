package com.example.instalearnenglish.feature.station23.model;

public class St23ChatMessage {
    public enum Sender {
        BOT, USER
    }

    private String message;
    private Sender sender;

    public St23ChatMessage(String message, Sender sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public Sender getSender() {
        return sender;
    }
}
