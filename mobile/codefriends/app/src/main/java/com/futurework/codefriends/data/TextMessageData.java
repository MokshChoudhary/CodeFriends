package com.futurework.codefriends.data;

import java.sql.Timestamp;

public class TextMessageData {
    private String TextMessage;
    private Timestamp timestamp;

    public TextMessageData() {
    }

    public String getTextMessage() {
        return TextMessage;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public TextMessageData(String text, Timestamp timestamp) {
        TextMessage = text;
        this.timestamp = timestamp;
    }


}
