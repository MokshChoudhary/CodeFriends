package com.futurework.codefriends;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.futurework.codefriends.Database.UserDb.UserDbProvider;
import com.futurework.codefriends.templates.UserInfoDialog;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.futurework.codefriends.Adapters.InfoHolderAdapter;
import com.futurework.codefriends.data.Info_Holder_Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView ListView;
    private FirebaseAuth mAuth;
    private ArrayList<Info_Holder_Data> Data;
    private InfoHolderAdapter adapter;
    private UserDbProvider dbProvider;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView = findViewById(R.id.List);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Log.i(TAG, "No User found!");
        } else Log.i(TAG, "user name : " + mAuth.getCurrentUser().getDisplayName());

        //      Implementing list view in the main screen
        Data = new ArrayList<>();
        adapter = new InfoHolderAdapter(Data, getApplicationContext());

        /**
         * @TODO : Load frame from database
         */

        dbProvider = new UserDbProvider(getApplicationContext());

        Data = dbProvider.getUserData();

        ListView.setAdapter(adapter);
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Info_Holder_Data data = Data.get(position);
                ImageView imageView = view.findViewById(R.id.image);
                ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,imageView, ViewCompat.getTransitionName(imageView));

                startActivity(new Intent(MainActivity.this,ChatActivity.class).putExtra("name",data.getName()),option.toBundle());
            }
        });
        ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Info_Holder_Data data = Data.get(i);
                Dialog dialogBox = new Dialog(MainActivity.this);
                dialogBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogBox.setContentView(UserInfoDialog.Init());
                ImageView image = dialogBox.findViewById(R.id.info_dialog_image);
                TextView name = dialogBox.findViewById(R.id.info_dialog_name);
                TextView status = dialogBox.findViewById(R.id.info_dialog_status);
                name.setText(data.getName());
                status.setText(data.getStatus());
                Glide.with(dialogBox.getContext()).load(data.getImage()).into(image);
                dialogBox.show();
                /*Snackbar.make(view, "long click : "+data.getName() + "\n" + data.getStatus() + " tags: " + data.getTags(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();*/
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "option menu");
        if (item.getItemId() == R.id.action_search) {
            Log.d(TAG, "Search option" + R.id.action_search);
            return true;
        } else if (item.getItemId() == R.id.action_setting) {
            Log.d(TAG, "Search option" + R.id.action_setting);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.d(TAG, "no user");
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
    }

    public void fclick(View view) {
//        startActivity(new Intent(MainActivity.this, Contact.class));
//        overridePendingTransition(R.anim.bottom_up,R.anim.nothing);
        ArrayList<String> a = new ArrayList<>();
        a.add("C++");
        a.add("Java");
        a.add("Android");
        a.add("Android");
        a.add("QT");
        dbProvider.setUserData("https://storage.googleapis.com/webdesignledger.pub.network/WDL/work-better-with-coders-1.jpg", "Sanskriti Choudhary", "This is a time pass",a.size(), a);
    }
}