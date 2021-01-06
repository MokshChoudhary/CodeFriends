package com.futurework.codefriends.Database.MessageDb;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.futurework.codefriends.Database.DbContainer;
import com.futurework.codefriends.data.TextMessageData;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageDbProvider {
    private SQLiteDatabase sqLiteDatabase;
    public MessageDbProvider(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public long setMessage(String id, String message){
        ContentValues values = new ContentValues();
        values.put(DbContainer.MessageEntry._ID,id);
        values.put(DbContainer.MessageEntry.COLUMNS_MESSAGE_Text,message);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        values.put(DbContainer.MessageEntry.COLUMNS_MESSAGE_TIMESTAMP,formatter.format(date));
        return sqLiteDatabase.insert(DbContainer.MessageEntry.MESSAGE_TABLE_NAME,null,values);
    }

    @SuppressLint("Recycle")
    public ArrayList<TextMessageData> getMessage(String id, int numberOfMessage){
        final ArrayList<TextMessageData> list = new ArrayList<>();

        Cursor dec = sqLiteDatabase.query(DbContainer.MessageEntry.MESSAGE_TABLE_NAME, null, DbContainer.MessageEntry._ID + " =? ", new String[]{id}, DbContainer.MessageEntry.COLUMNS_MESSAGE_TIMESTAMP, null, "DEC", numberOfMessage + "");

        while (dec.moveToNext()){
            TextMessageData msg = new TextMessageData(dec.getString(dec.getColumnIndex(DbContainer.MessageEntry.COLUMNS_MESSAGE_Text)), Timestamp.valueOf(dec.getString(dec.getColumnIndex(DbContainer.MessageEntry.COLUMNS_MESSAGE_TIMESTAMP))));
            list.add(msg);
        }

        return list;
    }
}
