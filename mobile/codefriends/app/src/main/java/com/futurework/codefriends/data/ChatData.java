package com.futurework.codefriends.data;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class ChatData {

    public static final int SENDER_TEXT = 7;
    public static final int RECEIVER_TEXT = 27;
    public static final int SENDER_IMAGE = 6;
    public static final int RECEIVER_IMAGE = 28;
    public static final int RECEIVER_AUDIO = 19;
    public static final int SENDER_AUDIO = 22;
    public int type;

    private String id;
    private String senderId;
    private String textMsg;
    private String date;
    private String image;
    private String audio;

    public ChatData(){}

    public ChatData(String senderId, String textMsg, String date, int type){
        if(type == RECEIVER_TEXT || type == SENDER_TEXT){
            this.textMsg = textMsg;
            this.type = type;
            this.date =  date;
            this.senderId = senderId;
        }else if(type == SENDER_IMAGE ){
            this.image = textMsg;
            this.type = type;
            this.date =  date;
            this.senderId = senderId;
        }
    }

    public ChatData(String senderId, String image, int type){
        if(type == RECEIVER_TEXT || type == SENDER_TEXT){
            this.textMsg = image;
            this.type = type;
            this.senderId = senderId;
            @SuppressLint("SimpleDateFormat") SimpleDateFormat time_formatter = new SimpleDateFormat("hh:mm a");
            String current_time_str = time_formatter.format(System.currentTimeMillis());
            this.date =  ""+current_time_str;
        }else if(type == SENDER_IMAGE || type == RECEIVER_IMAGE ){
            this.image = image;
            this.type = type;
            this.senderId = senderId;
            @SuppressLint("SimpleDateFormat") SimpleDateFormat time_formatter = new SimpleDateFormat("hh:mm a");
            String current_time_str = time_formatter.format(System.currentTimeMillis());
            this.date =  ""+current_time_str;
        }
    }

    public String getTextMsg() {
        return textMsg;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getAudio() {
        return audio;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
