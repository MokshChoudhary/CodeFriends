package com.futurework.codefriends.data;

import android.graphics.Bitmap;

import java.sql.Timestamp;

public class ImageMessageData extends TextMessageData {
    private Bitmap bitmap;

    public ImageMessageData() {
    }

    public ImageMessageData(String text, Timestamp timestamp, Bitmap bitmap) {
        super(text, timestamp);
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
