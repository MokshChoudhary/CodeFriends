package com.futurework.codefriends.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.futurework.codefriends.R;
import com.futurework.codefriends.data.ChatData;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private ArrayList<ChatData> mData;
    private LayoutInflater mInflater;
    protected String TAG = "ChatAdapter";

    public ChatAdapter(Context context,ArrayList<ChatData> data){
        mData = new ArrayList<>();
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
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
                return ChatData.SENDER_TEXT;
            case ChatData.RECEIVER_TEXT:
                return ChatData.RECEIVER_TEXT;
            case ChatData.SENDER_IMAGE:
                return ChatData.SENDER_IMAGE;
            case ChatData.RECEIVER_IMAGE:
                return ChatData.RECEIVER_IMAGE;
            default:
                return -1;
        }
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
            String date  = mData.get(position).getDate();
            switch (mData.get(position).type){
                case ChatData.SENDER_TEXT:
                    String msg = mData.get(position).getText();
                    holder.v.setText(msg);
                    holder.d.setText(date);
                    break;
                case ChatData.RECEIVER_TEXT:
                    holder.v.setText(mData.get(position).getText());
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

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView v,d;
        ImageView image;
        int viewType;
        public ViewHolder(@NonNull View itemView,int viewType) {
            super(itemView);
            this.viewType = viewType;
            switch (viewType){
                case ChatData.SENDER_TEXT:
                    v = itemView.findViewById(R.id.chat_massage_send_text);
                    d = itemView.findViewById(R.id.chat_massage_send_data);
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
                        public void onClick(DialogInterface dialog, int which) {
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
                        public void onClick(DialogInterface dialog, int which) {
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
