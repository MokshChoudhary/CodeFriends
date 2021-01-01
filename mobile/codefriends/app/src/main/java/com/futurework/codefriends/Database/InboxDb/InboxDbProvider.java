package com.futurework.codefriends.Database.InboxDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.futurework.codefriends.Database.DbContainer;
import com.futurework.codefriends.Database.DbHelper;
import com.futurework.codefriends.data.UserInfoData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class InboxDbProvider {
    private static final String TAG = "UserDataDbProvider";
    private SQLiteDatabase read;
    private SQLiteDatabase write;
    private final Context context;

    public InboxDbProvider(Context context){
        read = new DbHelper(context).getReadableDatabase();
        write = new DbHelper(context).getWritableDatabase();
        this.context = context;
    }

    public long setInboxData(final Bitmap image ,final String name,String number , final int where_box, final String status ,final ArrayList<String> tag_list){
        write = new DbHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_NAME,name);
        if(image != null){
            byte[] img = bitmapToByteArray(image);
            values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_IMAGE,img);
        }
        StringBuilder tags = new StringBuilder();
        if(tag_list != null){
            for(int i = 0; i < tag_list.size(); i++){
                tags.append(tag_list.get(i)).append(";");
            }
            values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_TAG, tags.toString());
        }else{
            values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_TAG, ";");
        }
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_NUMBER, number);
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_STATUS,status);
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_WHERE,where_box);
        return write.insert(DbContainer.BlankEntry.INBOX_TABLE_NAME,null,values);
    }

    public long setInboxData(final String name,String number , final int where_box, final String status , final ArrayList<String> tag_list){
        write = new DbHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_NAME,name);
        StringBuilder tags = new StringBuilder();
        if(tag_list != null){
            for(int i = 0; i < tag_list.size(); i++){
                tags.append(tag_list.get(i)).append(";");
            }
            values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_TAG, tags.toString());
        }else{
            values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_TAG, ";");
        }
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_NUMBER, number);
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_STATUS,status);
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_WHERE,where_box);
        return write.insert(DbContainer.BlankEntry.INBOX_TABLE_NAME,null,values);
    }

    public byte[] bitmapToByteArray(Bitmap image){
        int row = image.getRowBytes() , col = image.getByteCount();
        ByteBuffer buffer;
        if(image.getRowBytes()*image.getByteCount()>3705710){
            buffer = ByteBuffer.allocate(3705710);
        }else {
            buffer = ByteBuffer.allocate(row * col);
        }
        image.copyPixelsToBuffer(buffer);
        return buffer.array();
    }

    public Bitmap byteToBitmap(byte[] image){
        if(image == null) return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public ArrayList<UserInfoData> getUserData(){
        try(Cursor cursor = read.query(DbContainer.BlankEntry.INBOX_TABLE_NAME,null,null,null,null,null,null) ){
            ArrayList<UserInfoData> list = new ArrayList<>();
            UserInfoData info = new UserInfoData();
            while(cursor.moveToNext()){
                info.setId(cursor.getLong(cursor.getColumnIndex(DbContainer.BlankEntry._ID)));
                info.setImage(cursor.getString(cursor.getColumnIndex("image")));
                info.setName(cursor.getString(cursor.getColumnIndex("name")));
                info.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                String tags = cursor.getString(cursor.getColumnIndex("tag"));
                String[] tag_list = tags.split(";");
                info.setTags(tag_list);
                list.add(info);
            }
            return list;
        }
    }

    public ArrayList<UserInfoData> getUserData(long id){
        Log.d(TAG,"Id is : "+id);
        try(Cursor cursor = read.query(DbContainer.BlankEntry.INBOX_TABLE_NAME,null,DbContainer.BlankEntry._ID+" =? ", new String[]{String.valueOf(id)},null,null,null) ){
            ArrayList<UserInfoData> list = new ArrayList<>();
            UserInfoData info = new UserInfoData();
            while(cursor.moveToNext()){
                info.setImageByte(cursor.getBlob(cursor.getColumnIndex("image")));
                info.setName(cursor.getString(cursor.getColumnIndex("name")));
                info.setNumber(cursor.getString(cursor.getColumnIndex("number")));
                info.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                String tags = cursor.getString(cursor.getColumnIndex("tag")).trim();
                String[] tag_list = tags.split(";");
                info.setTags(tag_list);
                list.add(info);
            }
            return list;
        }
    }

    public ArrayList<UserInfoData> getUserData(int where_box){
        try(Cursor cursor = read.query(DbContainer.BlankEntry.INBOX_TABLE_NAME,null,DbContainer.BlankEntry.COLUMNS_INBOX_USER_WHERE+" = ",new String[]{where_box+""},null,null,null) ){
            ArrayList<UserInfoData> list = new ArrayList<>();
            UserInfoData info = new UserInfoData();
            while(cursor.moveToNext()){
                info.setImage(cursor.getString(cursor.getColumnIndex("image")));
                info.setName(cursor.getString(cursor.getColumnIndex("name")));
                info.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                info.setNumber(cursor.getString(cursor.getColumnIndex("number")));
                String tags = cursor.getString(cursor.getColumnIndex("tag")).trim();
                String[] tag_list = tags.split(";");
                info.setTags(tag_list);
                list.add(info);
            }
            return list;
        }
    }

    public int getCount(){
        try (Cursor cursor = read.query(DbContainer.BlankEntry.INBOX_TABLE_NAME, null, null, null, null, null, null)) {
            int count = cursor.getCount();
            cursor.close();
            return count;
        }
    }
}
