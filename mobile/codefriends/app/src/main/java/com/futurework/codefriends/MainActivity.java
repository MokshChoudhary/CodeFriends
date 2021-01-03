package com.futurework.codefriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.futurework.codefriends.Database.DbHelper;
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
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView ListView;
    private FirebaseAuth mAuth;
    private ArrayList<UserInfoData> Data;
    private InfoHolderAdapter adapter;
    private InboxDbProvider dbProvider;
    private ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        
        if(mAuth.getCurrentUser() == null){
            Log.d(TAG,"null object ");
        }
        search = findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                new DbHelper(MainActivity.this).dropTableUser();
                Log.d(TAG, "Search option" + R.id.search_button);
            }
        });

        ListView = findViewById(R.id.List);
        //Implementing list view in the main screen
        Data = new ArrayList<>();
        dbProvider = new InboxDbProvider(this);

        if (dbProvider.getUserData().size() != 0)
            Data = dbProvider.getUserData();

        adapter = new InfoHolderAdapter(Data, getApplicationContext());
        ListView.setAdapter(adapter);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfoData data = Data.get(position);
                ImageView imageView = view.findViewById(R.id.image);
                Log.i(TAG, "Click on " + data.getId());

                ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imageView, Objects.requireNonNull(ViewCompat.getTransitionName(imageView)));
                startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra("id", data.getId()), option.toBundle());
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbProvider.getUserData().size() != 0) {
            Data = dbProvider.getUserData();
            adapter = new InfoHolderAdapter(Data, getApplicationContext());
            ListView.setAdapter(adapter);
        }
    }

    public void fclick(View view) {
        startActivity(new Intent(MainActivity.this, Contact.class));
    }
}