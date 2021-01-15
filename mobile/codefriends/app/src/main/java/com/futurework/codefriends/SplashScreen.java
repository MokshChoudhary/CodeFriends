package com.futurework.codefriends;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.futurework.codefriends.Database.UserDb.UserDbProvider;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private final String TAG = "SplashScreen";
    private TextView logo ;
    private TextView logo_string ;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        logo = findViewById(R.id.logo);
        logo_string = findViewById(R.id.app_name);
        mAuth = FirebaseAuth.getInstance();

        /*
         *  animation on logo by fade in of logo duration 1sec
         */
        Animation fadeAnimation = AnimationUtils.loadAnimation(this,R.anim.splash_screen_animation);
        logo_string.startAnimation(fadeAnimation);
        logo.startAnimation(fadeAnimation);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(mAuth.getCurrentUser() == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this,Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    SplashScreen.this.finish();
                }
            },2000);
        }else{
            long count = new UserDbProvider(this).getCount();
            if(count > 0)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    SplashScreen.this.finish();
                }
            },2000);
            else
                new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this,UserForm.class));
                    SplashScreen.this.finish();
                }
            },2000);
        }
        //updateUI(currentUser);
    }
}