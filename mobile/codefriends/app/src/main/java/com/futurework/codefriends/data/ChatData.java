package com.futurework.codefriends.data;

import com.futurework.codefriends.R;

import java.text.SimpleDateFormat;

public class ChatData {

    public static final int SENDER_TEXT = R.layout.chat_massage_send_layout;
    public static final int RECEIVER_TEXT = R.layout.chat_massage_recive_layout;
    public static final int SENDER_IMAGE = R.layout.chat_image_send_layout;
    public static final int RECEIVER_IMAGE = R.layout.chat_image_receiver_layout;
    public int type;

    private String text;
    private String date;
    private String image;
    private String audio;


    public ChatData(String text, String date, int type){
        if(type == RECEIVER_TEXT || type == SENDER_TEXT){
            this.text = text;
            this.type = type;
            this.date =  date;
        }else if(type == SENDER_IMAGE ){
            this.image = text;
            this.type = type;
            this.date =  date;
        }
    }

    public ChatData(String image, int type){
        if(type == RECEIVER_TEXT || type == SENDER_TEXT){
            this.text = image;
            this.type = type;
            SimpleDateFormat time_formatter = new SimpleDateFormat("hh:mm a");
            String current_time_str = time_formatter.format(System.currentTimeMillis());
            this.date =  ""+current_time_str;
        }else if(type == SENDER_IMAGE || type == RECEIVER_IMAGE ){
            this.image = image;
            this.type = type;
            SimpleDateFormat time_formatter = new SimpleDateFormat("hh:mm a");
            String current_time_str = time_formatter.format(System.currentTimeMillis());
            this.date =  ""+current_time_str;
        }
    }

    public String getText() {
        return text;
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
}
