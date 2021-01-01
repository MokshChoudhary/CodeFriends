package com.futurework.codefriends.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS "+DbContainer.UserEntry.USER_TABLE_NAME;
    public static final String DROP_TABLE_INBOX = "DROP TABLE IF EXISTS "+DbContainer.BlankEntry.INBOX_TABLE_NAME;

    public final static String CREATE_TABLE_USER = "CREATE TABLE "+ DbContainer.UserEntry.USER_TABLE_NAME+"( " +
            DbContainer.UserEntry._ID+" TEXT NOT NULL PRIMARY KEY ," +
            DbContainer.UserEntry.COLUMNS_USER_IMAGE+" BLOB,"+
            DbContainer.UserEntry.COLUMNS_USER_NAME+" TEXT NOT NULL,"+
            DbContainer.UserEntry.COLUMNS_USER_EMAIL+" TEXT NOT NULL,"+
            DbContainer.UserEntry.COLUMNS_USER_NUMBER+" TEXT Unique,"+
            DbContainer.UserEntry.COLUMNS_USER_STATUS+" INTEGER NOT NULL, "+
            DbContainer.UserEntry.COLUMNS_USER_TAG+" TEXT " +
            ");";

  public final static String CREATE_TABLE_INBOX = "CREATE TABLE "+ DbContainer.BlankEntry.INBOX_TABLE_NAME+"( " +
            DbContainer.BlankEntry._ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            DbContainer.BlankEntry.COLUMNS_INBOX_USER_IMAGE+" BLOB,"+
            DbContainer.BlankEntry.COLUMNS_INBOX_USER_NAME+" TEXT NOT NULL,"+
            DbContainer.BlankEntry.COLUMNS_INBOX_USER_NUMBER+" TEXT UNIQUE NOT NULL,"+
            DbContainer.BlankEntry.COLUMNS_INBOX_USER_STATUS+" INTEGER NOT NULL , "+
            DbContainer.BlankEntry.COLUMNS_INBOX_USER_WHERE+" INTEGER NOT NULL , " +
            DbContainer.BlankEntry.COLUMNS_INBOX_USER_TAG+" TEXT " +
            ");";

    public static final int VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DbContainer.BlankEntry.DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_INBOX);
    }

    public void dropTableUser(){
        this.getWritableDatabase().execSQL(DROP_TABLE_USER);
        onCreate(this.getWritableDatabase());
    }

    public void dropTableInbox(){
        this.getWritableDatabase().execSQL(DROP_TABLE_INBOX);
        onCreate(this.getWritableDatabase());
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i<i1){
            sqLiteDatabase.execSQL(DROP_TABLE_USER);
            sqLiteDatabase.execSQL(DROP_TABLE_INBOX);
            onCreate(sqLiteDatabase);
        }
    }
}
