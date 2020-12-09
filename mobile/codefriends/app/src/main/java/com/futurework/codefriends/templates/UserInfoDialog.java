package com.futurework.codefriends.templates;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import com.futurework.codefriends.R;

public class UserInfoDialog extends AppCompatActivity {

    public static int Init(){
        return R.layout.user_info_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_dialog);
    }

}