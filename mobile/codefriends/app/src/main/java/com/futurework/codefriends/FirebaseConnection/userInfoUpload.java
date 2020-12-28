package com.futurework.codefriends.FirebaseConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.futurework.codefriends.UserForm;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

class userInfoUpload {
    FirebaseDatabase mRef;
    DatabaseReference mDatabase;
    FirebaseStorage mStorageRef;
    StorageReference mStorage;
    FirebaseFirestore db;
    Context context;
    public userInfoUpload(Context mContext){
        mRef = FirebaseDatabase.getInstance();
        mDatabase = mRef.getReference("User/");
        mStorageRef = FirebaseStorage.getInstance();
        mStorage = mStorageRef.getReference("UserImages/");
        db = FirebaseFirestore.getInstance();
        context = mContext;
    }

    public Uri uploadImage(Bitmap image,String imageName){
        mStorage = mStorage.child(imageName);
        ByteArrayOutputStream bitOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bitOutputStream);
        byte[] data = bitOutputStream.toByteArray();
        UploadTask task = mStorage.putBytes(data);
        task.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                UploadTask.TaskSnapshot result = task.getResult();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        Task<Uri> UrlTask = task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return mStorage.getDownloadUrl();
            }
        })/*.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        })*/;

        if(UrlTask.isComplete()) {
            return UrlTask.getResult();
        }
        else {
            try {
                wait(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return UrlTask.getResult();
    }

    public Uri uploadImage(UserForm.OnDataListener listener, Bitmap image, String imageName){
        mStorage = mStorage.child(imageName);
        ByteArrayOutputStream bitOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bitOutputStream);
        byte[] data = bitOutputStream.toByteArray();
        final ProgressDialog bar = new ProgressDialog(context);
        bar.setMessage("Uploading..");
        bar.show();
        UploadTask task = mStorage.putBytes(data);
        task.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                UploadTask.TaskSnapshot result = task.getResult();
                bar.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                bar.dismiss();
            }
        });

        Task<Uri> UrlTask = task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return mStorage.getDownloadUrl();
            }
        })/*.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        })*/;

        return UrlTask.getResult();
    }

}
