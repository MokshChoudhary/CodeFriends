 package com.futurework.codefriends.Database.MessageDb;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.futurework.codefriends.Database.DbContainer;
import com.futurework.codefriends.data.ChatData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageDbProvider {
    private final SQLiteDatabase sqLiteDatabase;
    public MessageDbProvider(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public long setMessage(int type, String message, String date, String senderId){
        ContentValues values = new ContentValues();
        values.put(DbContainer.MessageEntry.COLUMNS_MESSAGE_TYPE,type);
        values.put(DbContainer.MessageEntry.COLUMNS_MESSAGE_SENDER_ID,senderId);
        values.put(DbContainer.MessageEntry.COLUMNS_MESSAGE_Text,message.trim());
        values.put(DbContainer.MessageEntry.COLUMNS_MESSAGE_TIMESTAMP,date.trim());
        return sqLiteDatabase.insert(DbContainer.MessageEntry.MESSAGE_TABLE_NAME,null,values);
    }

    @SuppressLint("Recycle")
    public ArrayList<ChatData> getMessage(String id, int numberOfMessage){
        final ArrayList<ChatData> list = new ArrayList<>();

        Cursor dec = sqLiteDatabase.query(DbContainer.MessageEntry.MESSAGE_TABLE_NAME, null, DbContainer.MessageEntry.COLUMNS_MESSAGE_SENDER_ID + " =? ", new String[]{id}, DbContainer.MessageEntry.COLUMNS_MESSAGE_TIMESTAMP, null, DbContainer.MessageEntry.COLUMNS_MESSAGE_TIMESTAMP, numberOfMessage + "");

        while (dec.moveToNext()){
            ChatData msg = new ChatData();
            msg.setSenderId(dec.getString(dec.getColumnIndex(DbContainer.MessageEntry.COLUMNS_MESSAGE_SENDER_ID)));
            msg.setTextMsg(dec.getString(dec.getColumnIndex(DbContainer.MessageEntry.COLUMNS_MESSAGE_Text)));
            msg.setDate(dec.getString(dec.getColumnIndex(DbContainer.MessageEntry.COLUMNS_MESSAGE_TIMESTAMP)));
            msg.setType(dec.getInt(dec.getColumnIndex(DbContainer.MessageEntry.COLUMNS_MESSAGE_TYPE)));
            list.add(msg);
        }

        return list;
    }
}
