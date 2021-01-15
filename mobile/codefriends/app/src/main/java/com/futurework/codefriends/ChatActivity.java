package com.futurework.codefriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.futurework.codefriends.adapters.ChatAdapter;
import com.futurework.codefriends.Database.DbHelper;
import com.futurework.codefriends.Database.InboxDb.InboxDbProvider;
import com.futurework.codefriends.Database.MessageDb.MessageDbProvider;
import com.futurework.codefriends.Database.UserDb.UserDbProvider;
import com.futurework.codefriends.data.ChatData;
import com.futurework.codefriends.data.UserInfoData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class ChatActivity extends AppCompatActivity {

    static String TAG = "ChatActivity";
    private TextView name;
    private ImageView image,imageSet,bmb;
    private ArrayList<ChatData> mData;
    private RecyclerView recyclerView;
    private PopupMenu menu;
    private ChatAdapter n;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private EditText input;
    private MessageDbProvider mRead;
    private MessageDbProvider mWrite;
    private boolean flag = true;
    /**
        uniqueId is use to store the data of user id
        senderId is use to store the date of sender id
     */
    private String uniqueId,senderId;

    public interface addDataOnSetListener{
        void onDataSet(ChatData data);
    }

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
        recyclerView = findViewById(R.id.recyclerView);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Chatting/");

        mRead = new MessageDbProvider(new DbHelper(ChatActivity.this).getReadableDatabase());
        mWrite = new MessageDbProvider(new DbHelper(ChatActivity.this).getWritableDatabase());

        uniqueId = getIntent().getStringExtra("id");
        Log.d(TAG,"uniqueId is : "+ uniqueId);

        final FloatingActionButton button = findViewById(R.id.button_send_chat_activity);
        final InboxDbProvider dbProvider = new InboxDbProvider(this);

        //user information
        for(UserInfoData data : dbProvider.getUserData(uniqueId)){
            name.setText(data.getName());
            image.setImageBitmap(dbProvider.byteToBitmap(data.getImageByte()));
        }

        UserInfoData data = new UserDbProvider(ChatActivity.this).getUser();
        senderId = data.getId();

        Log.d(TAG,"senderId is : "+ senderId);

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

        /*final TextView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> ChatActivity.this.finish());*/

        bmb = findViewById(R.id.add_files);

        bmb.setOnClickListener(new add_file_class());

        /*
         * Stopping the fade of all other element while image transformation
         * */

        Fade fade = new Fade();
        View cView = getWindow().getDecorView();
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
        //mData.add(new ChatData("https://storage.googleapis.com/webdesignledger.pub.network/WDL/work-better-with-coders-1.jpg",ChatData.SENDER_IMAGE));
        //mData.add(new ChatData("https://storage.googleapis.com/webdesignledger.pub.network/WDL/work-better-with-coders-1.jpg",ChatData.RECEIVER_IMAGE));
        n = new ChatAdapter(this,mData);
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
                @SuppressLint("SimpleDateFormat") SimpleDateFormat time_formatter = new SimpleDateFormat("hh:mm a");
                String current_time_str = time_formatter.format(System.currentTimeMillis());
                ChatData msgData = new ChatData(senderId, input.getText().toString().trim(),current_time_str, ChatData.SENDER_TEXT);
                mRef.child(uniqueId+"/").push().setValue(msgData);

                mWrite.setMessage(ChatData.RECEIVER_TEXT,input.getText().toString().trim(),current_time_str.trim(),senderId);
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                mData.add(new ChatData(senderId,input.getText().toString().trim(), ChatData.SENDER_TEXT));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this, VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.scrollToPosition(mData.size() - 1);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                n.notifyItemChanged(mData.size() - 1);
                n.notifyDataSetChanged();
                input.setText("");
            }else{
                //@TODO send files ,Document,Images,audio
                PopupMenu menu = new PopupMenu(ChatActivity.this, view);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check for database
        int s = mData.size();
        mData.addAll(mRead.getMessage(senderId,25));
        if(mData.size() > s){
            n.notifyItemChanged(mData.size());
        }else{
            Log.d(TAG,"Data didn't change !");
        }
    }

    private class add_file_class implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Context wrapper = new ContextThemeWrapper(ChatActivity.this,R.style.ProgressDialog);
            menu = new PopupMenu(wrapper,bmb);
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