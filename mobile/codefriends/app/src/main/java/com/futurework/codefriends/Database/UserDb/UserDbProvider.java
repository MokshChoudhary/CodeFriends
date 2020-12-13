package com.futurework.codefriends.Database.UserDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.futurework.codefriends.Database.DbContainer;
import com.futurework.codefriends.Database.DbHelper;
import com.futurework.codefriends.MainActivity;
import com.futurework.codefriends.data.Info_Holder_Data;

import java.io.ByteArrayOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

public class UserDbProvider {
    private static final String TAG = "UserDataDbProvider";
    private SQLiteDatabase read;
    private SQLiteDatabase write;
    private final Context context;

    public UserDbProvider(Context context){
        read = new DbHelper(context).getReadableDatabase();
        write = new DbHelper(context).getWritableDatabase();
        this.context = context;
    }

    public void setUserData(byte[] image, String name, String status, int no_of_tag, ArrayList<String> tag_list){
        write = new DbHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContainer.BlankEntry.COLUMNS_USER_NAME,name);
        StringBuilder tags = new StringBuilder();
        for(int i = 0; i < no_of_tag; i++){
            tags.append(tag_list.get(i)).append(";");
        }
        values.put(DbContainer.BlankEntry.COLUMNS_USER_TAG, tags.toString());
        values.put(DbContainer.BlankEntry.COLUMNS_USER_STATUS,status);
        values.put(DbContainer.BlankEntry.COLUMNS_USER_IMAGE,image);
        write.insert(DbContainer.BlankEntry.DATABASE_NAME,null,values);
    }
    public void setUserData(String image, String name, String status, int no_of_tag, ArrayList<String> tag_list){
        write = new DbHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContainer.BlankEntry.COLUMNS_USER_NAME,name);

        StringBuilder tags = new StringBuilder();
        for(int i = 0; i < no_of_tag; i++){
            tags.append(tag_list.get(i)).append(";");
        }

        values.put(DbContainer.BlankEntry.COLUMNS_USER_TAG, tags.toString());
        values.put(DbContainer.BlankEntry.COLUMNS_USER_STATUS,status);

        FutureTarget<Bitmap> futureTarget = Glide.with(context)
                .asBitmap()
                .load(image)
                .submit();
        Bitmap map = null;
        try {
            map = futureTarget.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        values.put(DbContainer.BlankEntry.COLUMNS_USER_IMAGE,bitmapToByteArray(map));
        write.insert(DbContainer.BlankEntry.DATABASE_NAME,null,values);
    }

    public byte[] bitmapToByteArray(Bitmap image){
        ByteBuffer buffer = ByteBuffer.allocate(image.getRowBytes()*image.getByteCount());
        image.copyPixelsToBuffer(buffer);
        return buffer.array();
    }

    public Bitmap byteToBitmap(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public ArrayList<Info_Holder_Data> getUserData(){
        try(Cursor cursor = read.query(DbContainer.BlankEntry.LOGIN_TABLE_NAME,null,null,null,null,null,null) ){
            Info_Holder_Data info = new Info_Holder_Data();
            ArrayList<Info_Holder_Data> list = new ArrayList();
            cursor.moveToFirst();
            while(cursor.isAfterLast()){
                info.setImageInBytes(cursor.getBlob(1));
                info.setName(cursor.getString(2));
                info.setStatus(cursor.getString(3));
                String tags = cursor.getString(4).trim();
                String[] tag_list = tags.split(";");
                info.setTags(tag_list);
                list.add(info);
                cursor.moveToNext();
            }
            return list;
        }
    }

    public int getCount(){
        try (Cursor cursor = read.query(DbContainer.BlankEntry.LOGIN_TABLE_NAME, null, null, null, null, null, null)) {
            int count = cursor.getCount();
            cursor.close();
            return count;
        }
    }
}
