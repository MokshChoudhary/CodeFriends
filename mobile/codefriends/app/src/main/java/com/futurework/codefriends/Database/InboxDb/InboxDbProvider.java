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

    private void saveFile(Bitmap bitmap,String imageName) throws FileNotFoundException {
        FileOutputStream directory = new FileOutputStream("CodeFriends/image/");
        File file = new File(directory + imageName);
        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setUserData(final byte[] image ,final String name ,final String status ,final int no_of_tag ,final ArrayList<String> tag_list){
        write = new DbHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_NAME,name);
        StringBuilder tags = new StringBuilder();
        for(int i = 0; i < no_of_tag; i++){
            tags.append(tag_list.get(i)).append(";");
        }
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_TAG, tags.toString());
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_STATUS,status);

        Bitmap map = byteToBitmap(image);

        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_IMAGE,image);
        write.insert(DbContainer.BlankEntry.INBOX_TABLE_NAME,null,values);
    }

    public void setInboxData(final Bitmap image ,final String name,final String status ,final int no_of_tag ,final ArrayList<String> tag_list){
        write = new DbHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_NAME,name);
        StringBuilder tags = new StringBuilder();
        for(int i = 0; i < no_of_tag; i++){
            tags.append(tag_list.get(i)).append(";");
        }
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_TAG, tags.toString());
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_STATUS,status);
        byte[] img = bitmapToByteArray(image);
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_IMAGE,img);
        write.insert(DbContainer.BlankEntry.INBOX_TABLE_NAME,null,values);
    }

    /**
     * @see  <p> This method is not in work. </p>
     * @param image
     * @param name
     * @param status
     * @param tag_list
     */
    public long setUserData(final String image ,final String name ,final String status ,final ArrayList<String> tag_list){

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        long i = -1;
        SimpleDateFormat time_formatter = new SimpleDateFormat("d h,m");
        String current_time_str = time_formatter.format(System.currentTimeMillis());
        final String text = name+" "+current_time_str+".jpg";
        Glide.with(context)
                .asBitmap()
                .load(image)
                .into(new CustomTarget<Bitmap>(){
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            saveFile(resource,text);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.e("File Excption",text+" image not able to save image!");
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                })
                .onLoadStarted(circularProgressDrawable);

        write = new DbHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_NAME,name);
        StringBuilder tags = new StringBuilder();
        for(int f = 0; f < tag_list.size(); f++){
            tags.append(tag_list.get(f)).append(";");
        }

        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_TAG, tags.toString());
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_STATUS,status);
        values.put(DbContainer.BlankEntry.COLUMNS_INBOX_USER_IMAGE,text);

        values.put(DbContainer.UserEntry._ID,getCount());

        i = write.insert(DbContainer.BlankEntry.INBOX_TABLE_NAME,null,values);


        return i;
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
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public ArrayList<UserInfoData> getUserData(){
        try(Cursor cursor = read.query(DbContainer.BlankEntry.INBOX_TABLE_NAME,null,null,null,null,null,null) ){
            ArrayList<UserInfoData> list = new ArrayList<>();
            UserInfoData info = new UserInfoData();
            while(cursor.moveToNext()){
                info.setImage(cursor.getString(cursor.getColumnIndex("image")));
                info.setName(cursor.getString(cursor.getColumnIndex("name")));
                info.setStatus(cursor.getString(cursor.getColumnIndex("status")));
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
