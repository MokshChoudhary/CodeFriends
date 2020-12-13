package com.futurework.codefriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.futurework.codefriends.Adapters.ChatAdapter;
import com.futurework.codefriends.data.ChatData;


import java.util.ArrayList;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class ChatActivity extends AppCompatActivity {

    static String TAG = "ChatActivity";
    private TextView name,text;
    private ImageView image;
    private ArrayList<ChatData> mData;
    private RecyclerView recyclerView;
    private PopupMenu menu;
    private TextView bmb;

    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);
        name = findViewById(R.id.chatter_name);
        image = findViewById(R.id.chatter_image);
        text = findViewById(R.id.send_input_text);
        final ImageButton button = findViewById(R.id.button_send_chat_activity);
        button.setOnClickListener(new send_button_add_data_UI());

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


        name.setText("Sanskriti Choudhary");
        Glide.with(getApplicationContext())
                .load("https://storage.googleapis.com/webdesignledger.pub.network/WDL/work-better-with-coders-1.jpg")
                .circleCrop()
                .fitCenter()
                .into(image);

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
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(n);
        recyclerView.scrollToPosition(mData.size() - 1);
    }

    private class send_button_add_data_UI implements View.OnClickListener{
        @Override
        public void onClick(View view){
            /* the recycle view implementation*/
            if(!text.getText().toString().trim().isEmpty()) {
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                ChatAdapter n = new ChatAdapter(ChatActivity.this, mData);
                mData.add(new ChatData(text.getText().toString().trim(), ChatData.SENDER_TEXT));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this, VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.scrollToPosition(mData.size() - 1);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                text.setText("");
                recyclerView.setAdapter(n);
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