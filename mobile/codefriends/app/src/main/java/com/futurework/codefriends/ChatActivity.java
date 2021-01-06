package com.futurework.codefriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.futurework.codefriends.Adapters.ChatAdapter;
import com.futurework.codefriends.Database.DbHelper;
import com.futurework.codefriends.Database.InboxDb.InboxDbProvider;
import com.futurework.codefriends.Database.MessageDb.MessageDbProvider;
import com.futurework.codefriends.data.ChatData;
import com.futurework.codefriends.data.UserInfoData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class ChatActivity extends AppCompatActivity {

    static String TAG = "ChatActivity";
    private TextView name;
    private ImageView image,imageSet;
    private ArrayList<ChatData> mData;
    private RecyclerView recyclerView;
    private PopupMenu menu;
    private FirebaseDatabase mDatabase;
    private EditText input;
    private TextView bmb;
    private MessageDbProvider mDbProvider = new MessageDbProvider(new DbHelper(ChatActivity.this).getWritableDatabase());
    private boolean flag = true;
    private String uniqueId;

    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);
        name = findViewById(R.id.chatter_name);
        image = findViewById(R.id.chatter_image);
        input = findViewById(R.id.send_input_text);
        imageSet = findViewById(R.id.image_set);
        imageSet.setImageResource(R.drawable.blank);
        uniqueId = getIntent().getLongExtra("id",-1)+"";
        final FloatingActionButton button = findViewById(R.id.button_send_chat_activity);
        InboxDbProvider dbProvider = new InboxDbProvider(this);
        Log.d(TAG,"Id is : "+ uniqueId);
        for(UserInfoData data : dbProvider.getUserData(Long.parseLong(uniqueId))){
            name.setText(data.getName());
            image.setImageBitmap(dbProvider.byteToBitmap(data.getImageByte()));
        }

        button.setOnClickListener(new send_button_add_data_UI());

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG,editable.toString().trim().length()+"");
                if(editable.toString().trim().length() > 0){
                    if(flag)
                    {
                        button.setImageResource(R.drawable.ic_baseline_send_24);
                        imageSet.setImageResource(R.drawable.ic_baseline_attachment_24);
                        flag = false;
                    }
                }else{
                    button.setImageResource(R.drawable.ic_baseline_attachment_24);
                    imageSet.setImageResource(R.drawable.blank);
                    flag = true;
                }
            }
        });

        final TextView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatActivity.this.finish();
            }
        });

        bmb = findViewById(R.id.add_files);

        bmb.setOnClickListener(new add_file_class());

        /*
         * Stopping the fade of all other element while image transformation
         * */

        Fade fade = new Fade();
        View cView = getWindow().getDecorView();
        fade.excludeTarget(cView.findViewById(R.id.linearLayout2),true);
        fade.excludeTarget(cView.findViewById(R.id.recyclerView),true);
        fade.excludeTarget(cView.findViewById(R.id.send_input_text),true);
        fade.excludeTarget(cView.findViewById(R.id.button_send_chat_activity),true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        /* the recycle view implementation*/
        mData = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        //mData.add(new ChatData("https://storage.googleapis.com/webdesignledger.pub.network/WDL/work-better-with-coders-1.jpg",ChatData.SENDER_IMAGE));
        //mData.add(new ChatData("https://storage.googleapis.com/webdesignledger.pub.network/WDL/work-better-with-coders-1.jpg",ChatData.RECEIVER_IMAGE));
        ChatAdapter n = new ChatAdapter(this,mData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(n);
        recyclerView.scrollToPosition(mData.size() - 1);
    }

    private class send_button_add_data_UI implements View.OnClickListener{
        @Override
        public void onClick(View view){
            /* the recycle view implementation*/
            if(!input.getText().toString().trim().isEmpty()) {
                //@TODO Update in firebase
                mDatabase.getReference("Chatting/").child(uniqueId+"/").push();

                mDbProvider.setMessage(uniqueId,input.getText().toString().trim());
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                ChatAdapter n = new ChatAdapter(ChatActivity.this, mData);
                mData.add(new ChatData(input.getText().toString().trim(), ChatData.SENDER_TEXT));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this, VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.scrollToPosition(mData.size() - 1);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                input.setText("");
                recyclerView.setAdapter(n);
            }else{
                //@TODO send files ,Document,Images,audio
                PopupMenu menu = new PopupMenu(ChatActivity.this, view);
            }
        }
    }

    private class add_file_class implements View.OnClickListener{
        @Override
        public void onClick(View view){
            menu = new PopupMenu(ChatActivity.this,bmb);
            menu.getMenuInflater().inflate(R.menu.add_file,menu.getMenu());
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    return false;
                }
            });
            menu.show();
        }
    }

    @Override
    public void onBackPressed() {
        ChatActivity.this.finish();
    }
}