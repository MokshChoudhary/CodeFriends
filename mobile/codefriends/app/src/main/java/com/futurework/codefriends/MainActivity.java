package com.futurework.codefriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.futurework.codefriends.Database.InboxDb.InboxDbProvider;
import com.futurework.codefriends.Database.UserDb.UserDbProvider;
import com.futurework.codefriends.data.UserInfoData;
import com.futurework.codefriends.templates.UserInfoDialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.futurework.codefriends.Adapters.InfoHolderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView ListView;
    private FirebaseAuth mAuth;
    private ArrayList<UserInfoData> Data;
    private InfoHolderAdapter adapter;
    private InboxDbProvider dbProvider;
    //private ImageView buttonSetting = findViewById(R.id.s);
    private ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Log.d(TAG, "Search option" + R.id.search_button);
            }
        });

        /*buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Search option" + R.id.search_button);
                startActivity(new Intent(MainActivity.this, Contact.class));
            }
        });*/

        //Checking if the user is registered
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Log.i(TAG, "No User found!");
            Toast.makeText(getApplicationContext()
                    ,"Can not able to fetch your information!",Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this,Login.class));
            this.finish();
        } else {
            long count = new UserDbProvider(this).getCount();
            Log.d(TAG,"user database count : "+count);
            if(count <= 0){
                Log.d(TAG,"test");
                startActivity(new Intent(MainActivity.this,UserForm.class));
                this.finish();
            }
            Log.d(TAG, "user name : " + mAuth.getUid());

        }


        ListView = findViewById(R.id.List);
        //Implementing list view in the main screen
        Data = new ArrayList<>();
        dbProvider = new InboxDbProvider(this);

        if(dbProvider.getUserData().size() != 0)
            Data = dbProvider.getUserData();

        adapter = new InfoHolderAdapter(Data, getApplicationContext());
        ListView.setAdapter(adapter);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfoData data = Data.get(position);
                ImageView imageView = view.findViewById(R.id.image);
                Log.i(TAG,"Click on "+data.getName());
                ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,imageView, ViewCompat.getTransitionName(imageView));

                startActivity(new Intent(MainActivity.this,ChatActivity.class).putExtra("name",data.getName()),option.toBundle());
            }
        });
        ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserInfoData data = Data.get(i);
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
                return true;
            }
        });

        if(new InboxDbProvider(getApplicationContext()).getCount() <=0 ) {
            ArrayList<String> a = new ArrayList<>();
            a.add("C++");
            a.add("Java");
            a.add("Android");
            a.add("QT");
            if (-1 == new InboxDbProvider(getApplicationContext()).setUserData("https://storage.googleapis.com/webdesignledger.pub.network/WDL/work-better-with-coders-1.jpg", "Sanskriti Choudhary", "This is a time pass", a)) {
                Toast.makeText(getApplicationContext(), "Not Inserted", Toast.LENGTH_LONG).show();
                if (dbProvider.getUserData().size() != 0) {
                    Data = dbProvider.getUserData();
                    adapter = new InfoHolderAdapter(Data, getApplicationContext());
                    ListView.setAdapter(adapter);
                }
            }
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
            MainActivity.this.finish();
        }
    }


    public void fclick(View view) {
        startActivity(new Intent(MainActivity.this, Contact.class));
    }
}