package com.futurework.codefriends.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.futurework.codefriends.ChatActivity;
import com.futurework.codefriends.Contact;
import com.futurework.codefriends.Database.DbContainer;
import com.futurework.codefriends.Database.InboxDb.InboxDbProvider;
import com.futurework.codefriends.R;
import com.futurework.codefriends.Service.Service;
import com.futurework.codefriends.data.UserInfoData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class ContactAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private static final String TAG = "ContactAdapter";
    private String name[];
    private String number[];
    private String imageUri[];
    private LayoutInflater inflater;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;

    public ContactAdapter(Context context, ArrayList<String> name, ArrayList<String> number, ArrayList<String> imageUri) {
        this.name = new String[name.size()];
        this.number = new String[number.size()];
        this.imageUri = new String[imageUri.size()];
        this.name = name.toArray(this.name);
        this.number = number.toArray(this.number);
        this.imageUri = imageUri.toArray(this.imageUri);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.contact_holder, null);
        TextView name = view.findViewById(R.id.contact_name);
        TextView phone = view.findViewById(R.id.contact_phone);
        ImageView image = view.findViewById(R.id.contact_image);
        name.setText(this.name[i]);
        phone.setText(this.number[i]);
        Glide.with(view).
                load(imageUri[i]).
                apply(new RequestOptions()).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                into(image);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG,name[i]);
        final int[] flag = {0};
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_progress);
        ((Button)(dialog.getWindow().findViewById(R.id.button2))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag[0] == 1){
                    dialog.dismiss();
                }
            }
        });
        dialog.setCancelable(false);
        final TextView title = dialog.getWindow().findViewById(R.id.progress_title);
        final TextView msg = dialog.getWindow().findViewById(R.id.progress_msg);
        title.setText("Loading Contact");
        msg.setText("finding contact on cloud ");
        dialog.show();
        //Todo Check that user in the cloud database or not
        number[i] = Service.rawNumber(number[i]);
        Log.d(TAG,number[i]);
        db.collection("Users").
                orderBy("name").
                whereEqualTo("number",number[i]).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //Flag 1 : data is successfully found
                        flag[0]=1;
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                            Log.d(TAG,"contact Found");
                            if(document != null){
                                Map<String,Object> data = document.getData();
                                final String name = (String)data.get("name");
                                final String number = (String)data.get("number");
                                final String imageUri = (String)data.get("image");
                                final String status = (String)data.get("status");
                                final List<String> tags;
                                if(!("-1".equals(data.get("tags"))))
                                    tags = (List<String>) data.get("tags");
                                else
                                    tags = null;
                                msg.setText("downloading image!");
                                Glide.with(context)
                                        .asBitmap()
                                        .load(imageUri)
                                        .listener(new RequestListener<Bitmap>() {
                                            @Override
                                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                                msg.setText("Adding data into database");
                                                InboxDbProvider dbProvider = new InboxDbProvider(context);
                                                long i = dbProvider.setInboxData(resource,name,number,DbContainer.BlankEntry.INBOX,status, new ArrayList<>(tags));
                                                if(i == -1){
                                                    //@TODO Create Dialog to Send request to none CodeFriends user a link
                                                    Log.d(TAG,"Insertion failed");
                                                    msg.setText("Insertion failed");
                                                }else{
                                                    //flag 3 : data is the database
                                                    flag[0] = 3;
                                                    Intent intent = new Intent(context, ChatActivity.class);
                                                    intent.putExtra("id",i);
                                                    context.startActivity(intent);
                                                }
                                                dialog.dismiss();
                                                return false;
                                            }

                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                                Log.d(TAG, e != null ? e.getMessage() : "null");
                                                try{
                                                    ((TextView)(dialog.getWindow().findViewById(R.id.progress_msg))).setText("Adding data into database no image found");
                                                    msg.setText("Adding data into database no image found");
                                                }catch(Exception exp){
                                                    Log.d(TAG,exp.getMessage());
                                                }
                                                InboxDbProvider dbProvider = new InboxDbProvider(context);
                                                ArrayList<String> tag = null;
                                                if(tags != null) tag = new ArrayList<>(tags);
                                                long i = dbProvider.setInboxData(name,number,DbContainer.BlankEntry.INBOX,status,tag);
                                                dialog.dismiss();
                                                if(i == -1){
                                                    //@TODO Create Dialog to Send request to none CodeFriends user a link
                                                    msg.setText("Insertion failed");
                                                    Log.d(TAG,"Insertion failed");
                                                }else{
                                                    //flag 3 : data in the database
                                                    flag[0] = 3;
                                                    Intent intent = new Intent(context, ChatActivity.class);
                                                    intent.putExtra("id",i);
                                                    context.startActivity(intent);
                                                }
                                                return false;
                                            }
                                        }).submit();
                            }else{
                                //flag 2 : no data found
                                flag[0] = 2;
                                Log.d(TAG,"No document found in the cloud");
                                msg.setText("No document found in the cloud");
                                dialog.dismiss();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //flag 2 : no data found
                        //@TODO Send message to the contact to install codefriends
                        Log.d(TAG,"No match found");
                        Log.d(TAG,e.getMessage());
                        msg.setText("No match found");
                        dialog.dismiss();
                        Toast.makeText(context,"The no match found",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
