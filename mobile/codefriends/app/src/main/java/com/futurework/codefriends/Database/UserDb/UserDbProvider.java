package com.futurework.codefriends.Database.UserDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.service.autofill.UserData;

import com.futurework.codefriends.Database.DbContainer;
import com.futurework.codefriends.Database.DbHelper;
import com.futurework.codefriends.data.UserInfoData;

public class UserDbProvider {

    private SQLiteDatabase read;
    private SQLiteDatabase write;

    public UserDbProvider(Context context){
        read = new DbHelper(context).getReadableDatabase();
        write = new DbHelper(context).getReadableDatabase();
    }

    public long insertUserData(UserInfoData data){
        ContentValues values = new ContentValues();
        values.put(DbContainer.UserEntry._ID,getCount());
        values.put(DbContainer.UserEntry.COLUMNS_USER_NAME,data.getName());
        values.put(DbContainer.UserEntry.COLUMNS_USER_IMAGE,data.getImage());
        values.put(DbContainer.UserEntry.COLUMNS_USER_EMAIL,data.getEmail());
        values.put(DbContainer.UserEntry.COLUMNS_USER_STATUS,data.getStatus());
        values.put(DbContainer.UserEntry.COLUMNS_USER_TAG,data.getTagsAsString());
        values.put(DbContainer.UserEntry.COLUMNS_USER_NUMBER,data.getNumber());
        return write.insert(DbContainer.UserEntry.USER_TABLE_NAME,null,values);
    }

    public int getCount(){
        try (Cursor cursor = read.query(DbContainer.UserEntry.USER_TABLE_NAME, null, null, null, null, null, null)) {
            int count = cursor.getCount();
            cursor.close();
            return count;
        }
    }
}
