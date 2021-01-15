package com.futurework.codefriends.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.futurework.codefriends.R;
import com.futurework.codefriends.data.ChatData;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private final ArrayList<ChatData> mData;
    private final LayoutInflater mInflater;
    protected String TAG = "ChatAdapter";

    public ChatAdapter(Context context,ArrayList<ChatData> data){
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v ;
        switch (viewType) {
            case ChatData.SENDER_TEXT:
                v = mInflater.inflate(R.layout.chat_massage_send_layout, parent, false);
                return new ViewHolder(v, R.layout.chat_massage_send_layout);

            case ChatData.RECEIVER_TEXT:
                v = mInflater.inflate(R.layout.chat_massage_recive_layout, parent, false);
                return new ViewHolder(v, R.layout.chat_massage_recive_layout);

            case ChatData.SENDER_IMAGE:
                v = mInflater.inflate(R.layout.chat_image_send_layout,parent,false);
                return new ViewHolder(v, R.layout.chat_image_send_layout);

            case ChatData.RECEIVER_IMAGE:
                v = mInflater.inflate(R.layout.chat_image_receiver_layout,parent,false);
                return new ViewHolder(v,R.layout.chat_image_receiver_layout);

            default:
                return null;

        }
    }

    @Override
    public int getItemViewType(int position){
        switch (mData.get(position).type) {
            case ChatData.SENDER_TEXT:
                return R.layout.chat_massage_send_layout;
            case ChatData.RECEIVER_TEXT:
                return R.layout.chat_massage_recive_layout;
            case ChatData.SENDER_IMAGE:
                return R.layout.chat_image_send_layout;
            case ChatData.RECEIVER_IMAGE:
                return R.layout.chat_image_receiver_layout;
            default:
                return -1;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
            String date  = mData.get(position).getDate();
            switch (mData.get(position).type){
                case ChatData.SENDER_TEXT:
                    String msg = mData.get(position).getTextMsg();
                    holder.v.setText(msg);
                    holder.d.setText(date);
                    break;
                case ChatData.RECEIVER_TEXT:
                    holder.v.setText(mData.get(position).getTextMsg());
                    holder.d.setText(date);
                    break;
                case ChatData.SENDER_IMAGE:
                    Glide.with(holder.itemView)
                            .load(mData.get(position).getImage())
                            .into(holder.image);
                    holder.d.setText(date);
                    break;
                case ChatData.RECEIVER_IMAGE:
                    Glide.with(holder.itemView)
                            .load(mData.get(position).getImage())
                            .into(holder.image);
                    holder.d.setText(date);
                    holder.d.setTextColor(Color.BLACK);
                    break;
            }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    static public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView v,d;
        ImageView image;
        int viewType;
        public ViewHolder(@NonNull View itemView,int viewType) {
            super(itemView);
            this.viewType = viewType;
            switch (viewType){
                case ChatData.SENDER_TEXT:
                    v = itemView.findViewById(R.id.chat_massage_send_text);
                    d = itemView.findViewById(R.id.chat_massage_send_date);
                    itemView.setOnClickListener(this);
                    itemView.setOnLongClickListener(this);
                    break;

                case ChatData.RECEIVER_TEXT:
                    v = itemView.findViewById(R.id.chat_massage_receive_text);
                    d = itemView.findViewById(R.id.chat_massage_receive_date);
                    itemView.setOnClickListener(this);
                    itemView.setOnLongClickListener(this);
                    break;

                case ChatData.SENDER_IMAGE:
                    image = itemView.findViewById(R.id.send_image_layout);
                    d = itemView.findViewById(R.id.send_data_layout);
                    itemView.setOnClickListener(this);
                    itemView.setOnLongClickListener(this);
                    break;

                case ChatData.RECEIVER_IMAGE:
                    image = itemView.findViewById(R.id.receive_image_layout);
                    d = itemView.findViewById(R.id.receive_data_layout);
                    itemView.setOnClickListener(this);
                    itemView.setOnLongClickListener(this);
                    break;
            }
        }

        @Override
        public void onClick(View view) {
            switch (viewType){
                case ChatData.SENDER_TEXT :
                    Snackbar.make(view,"Sender Text",Snackbar.LENGTH_SHORT).show();
                    break;
                case ChatData.RECEIVER_TEXT :
                    Snackbar.make(view,"Receive Text",Snackbar.LENGTH_SHORT).show();
                    break;
                case ChatData.SENDER_IMAGE:
                    Snackbar.make(view,"Send Image",Snackbar.LENGTH_SHORT).show();
                    break;
                case ChatData.RECEIVER_IMAGE:
                    Snackbar.make(view,"Receive Image",Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            String[] options = {"Remove from Host", "Remove from server"};
            AlertDialog dialog;
            switch (viewType){
                case ChatData.SENDER_TEXT :
                    builder.setTitle("Select One");
                    //Pass the array list in Alert dialog
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            switch (which) {
                                case 0: // Select option task
                                    break;
                                case 1: // Config it as you need here
                                    break;
                            }
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                    Snackbar.make(view,"Long Sender Text",Snackbar.LENGTH_SHORT).show();
                    return true;
                case ChatData.RECEIVER_TEXT :
                    Snackbar.make(view,"Long Receive Text",Snackbar.LENGTH_SHORT).show();
                    return true;
                case ChatData.SENDER_IMAGE:
                    builder.setTitle("Select One");
                    //Pass the array list in Alert dialog
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog12, int which) {
                            switch (which) {
                                case 0: // Select option task
                                    break;
                                case 1: // Config it as you need here
                                    break;
                            }
                        }
                    });
                    // create and show the alert dialog
                    dialog = builder.create();
                    dialog.show();
                    Snackbar.make(view,"Long Send Image",Snackbar.LENGTH_SHORT).show();
                    return true;
                case ChatData.RECEIVER_IMAGE:
                    Snackbar.make(view,"Long Receiver Image",Snackbar.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        }

    }
}
