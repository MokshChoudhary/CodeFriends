package com.futurework.codefriends.Database.UserDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.service.autofill.UserData;
import android.util.Log;

import com.futurework.codefriends.Database.DbContainer;
import com.futurework.codefriends.Database.DbHelper;
import com.futurework.codefriends.data.UserInfoData;

public class UserDbProvider {

    private static final String TAG = "UserDbProvider" ;
    private SQLiteDatabase read;
    private SQLiteDatabase write;

    public UserDbProvider(Context context){
        read = new DbHelper(context).getReadableDatabase();
        write = new DbHelper(context).getReadableDatabase();
    }

    public long insertUserData(UserInfoData data){
        ContentValues values = new ContentValues();
        values.put(DbContainer.UserEntry._ID,data.getId());
        values.put(DbContainer.UserEntry.COLUMNS_USER_NAME,data.getName());
        values.put(DbContainer.UserEntry.COLUMNS_USER_IMAGE,data.getImage());
        values.put(DbContainer.UserEntry.COLUMNS_USER_EMAIL,data.getEmail());
        values.put(DbContainer.UserEntry.COLUMNS_USER_STATUS,data.getStatus());
        values.put(DbContainer.UserEntry.COLUMNS_USER_TAG,data.getTagsAsString());
        values.put(DbContainer.UserEntry.COLUMNS_USER_NUMBER,data.getNumber());
        return write.insert(DbContainer.UserEntry.USER_TABLE_NAME,null,values);
    }

    public UserInfoData getUser(){
        try (Cursor cursor = read.query(DbContainer.UserEntry.USER_TABLE_NAME, null, null, null, null, null, null)) {
            UserInfoData data = new UserInfoData();
            while(cursor.moveToNext()) {
                data.setId(cursor.getString(cursor.getColumnIndex(DbContainer.UserEntry._ID)));
                data.setName(cursor.getString(cursor.getColumnIndex(DbContainer.UserEntry.COLUMNS_USER_NAME)));
                data.setEmail(cursor.getString(cursor.getColumnIndex(DbContainer.UserEntry.COLUMNS_USER_EMAIL)));
                if (cursor.getBlob(cursor.getColumnIndex(DbContainer.UserEntry.COLUMNS_USER_IMAGE)) != null)
                    data.setImageByte(cursor.getBlob(cursor.getColumnIndex(DbContainer.UserEntry.COLUMNS_USER_IMAGE)));
                data.setNumber(cursor.getString(cursor.getColumnIndex(DbContainer.UserEntry.COLUMNS_USER_NUMBER)));
                data.setStatus(cursor.getString(cursor.getColumnIndex(DbContainer.UserEntry.COLUMNS_USER_STATUS)));
                cursor.close();
                Log.d(TAG, data.getId() + " " + data.getName() + " " + data.getNumber());
            }
            return data;
        }
    }

    public int getCount(){
        try (Cursor cursor = read.query(DbContainer.UserEntry.USER_TABLE_NAME, null, null, null, null, null, null)) {
            int count = cursor.getCount();
            cursor.close();
            return count;
        }
    }
}
