package com.futurework.codefriends.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS "+DbContainer.BlankEntry.LOGIN_TABLE_NAME;

    public final static String CREATE_TABLE_USER = "CREATE TABLE "+ DbContainer.BlankEntry.LOGIN_TABLE_NAME+"( " +
            DbContainer.BlankEntry._ID+" INTEGER NOT NULL PRIMARY KEY ," +
            DbContainer.BlankEntry.COLUMNS_USER_IMAGE+" BLOB,"+
            DbContainer.BlankEntry.COLUMNS_USER_NAME+" TEXT NOT NULL,"+
            DbContainer.BlankEntry.COLUMNS_USER_STATUS+" INTEGER NOT NULL, "+
            DbContainer.BlankEntry.COLUMNS_USER_TAG+" TEXT " +
            ");";

    public static final int VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DbContainer.BlankEntry.DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i<i1){
            sqLiteDatabase.execSQL(DROP_TABLE_USER);
            onCreate(sqLiteDatabase);
        }
    }
}
