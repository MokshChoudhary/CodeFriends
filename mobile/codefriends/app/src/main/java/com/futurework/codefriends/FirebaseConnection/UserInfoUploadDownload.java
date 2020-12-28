package com.futurework.codefriends.FirebaseConnection;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.futurework.codefriends.UserForm;
import com.futurework.codefriends.data.UserInfoData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserInfoUploadDownload extends userInfoUpload{

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private final String TAG = "UserInfoUploadDownload";
    public UserInfoUploadDownload(Context context){
        super(context);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User/");
    }

    public void UploadUserData(final UserInfoData data, final UserForm.OnDataListener listener){
        if(data != null){
            Map<String,Object> info = new HashMap<>();
            info.put("name",data.getName());
            info.put("image", data.getImage());
            info.put("email",data.getEmail());
            info.put("status", data.getStatus());
            info.put("number", data.getNumber());
            info.put("tags", data.getNumber());
            db.collection("Users")
                    .document(data.getEmail())
                    .set(info)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void mVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            listener.onBooleanChange(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onBooleanChange(false);
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }


    }


    public ArrayList<UserInfoData> DownloadUserData(){
        ArrayList<UserInfoData> userInfo= new ArrayList<>();

        return userInfo;
    }
}
