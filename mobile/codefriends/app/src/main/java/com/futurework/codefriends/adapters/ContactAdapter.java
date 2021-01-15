package com.futurework.codefriends.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.futurework.codefriends.Database.DbContainer;
import com.futurework.codefriends.Database.InboxDb.InboxDbProvider;
import com.futurework.codefriends.R;
import com.futurework.codefriends.Service.Service;
import com.futurework.codefriends.templates.CustomProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ContactAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private static final String TAG = "ContactAdapter";
    private String[] name;
    private String[] number;
    private String[] imageUri;
    private final LayoutInflater inflater;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Context context;
    private final addDataAddListener listener;

    public interface addDataAddListener{
        void onIsDataSet(boolean isDataSet);
    }

    public ContactAdapter(Context context, ArrayList<String> name, ArrayList<String> number, ArrayList<String> imageUri,addDataAddListener listener) {
        this.name = new String[name.size()];
        this.number = new String[number.size()];
        this.imageUri = new String[imageUri.size()];
        this.name = name.toArray(this.name);
        this.number = number.toArray(this.number);
        this.imageUri = imageUri.toArray(this.imageUri);
        this.listener = listener;
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
        final CustomProgressBar dialog = new CustomProgressBar(context);
        dialog.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag[0] == 1){
                    dialog.dismiss();
                }
            }
        });
        dialog.setTitle("Loading Contact");
        dialog.setMessage("finding contact on cloud ");
        dialog.show();

        number[i] = Service.rawNumber(number[i]);
        Log.d(TAG,number[i]);
        db.collection("Users")
                .whereEqualTo("number",number[i])
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //Flag 1 : data is successfully found
                        flag[0]=1;
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                            Log.d(TAG,"contact Found");
                            if(document != null) {
                                Map<String, Object> data = document.getData();
                                final String id = (String) data.get("id");
                                final String name = (String) data.get("name");
                                final String email = (String) data.get("email");
                                final String number = (String) data.get("number");
                                final String imageUri = (String) data.get("image");
                                final String status = (String) data.get("status");
                                final List<String> tags;
                                if (!("-1".equals(data.get("tags"))))
                                    tags = (List<String>) data.get("tags");
                                else
                                    tags = null;
                                dialog.setMessage("downloading image!");
                                Log.d(TAG, imageUri);
                                try {
                                    assert email != null;
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(FirebaseStorage.getInstance().getReference().child("UserImage/"+new Service().removeExtraFromString(email)).getDownloadUrl())
                                            .listener(new RequestListener<Bitmap>() {
                                                @Override
                                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                                    dialog.setMessage("Adding data into database");
                                                    InboxDbProvider dbProvider = new InboxDbProvider(context);
                                                    assert tags != null;
                                                    long i = dbProvider.setInboxData(resource , id, name, number, DbContainer.BlankEntry.INBOX, status, new ArrayList<>(tags));
                                                    if (i == -1) {
                                                        //@TODO Create Dialog to Send request to none CodeFriends user a link
                                                        Log.d(TAG, "Insertion failed");
                                                        dialog.setMessage("Insertion failed");
                                                    } else {
                                                        //flag 3 : data is the database
                                                        flag[0] = 3;
                                                        /*Intent intent = new Intent(context, ChatActivity.class);
                                                        intent.putExtra("id", i);
                                                        context.startActivity(intent)*/
                                                        listener.onIsDataSet(true);
                                                    }
                                                    dialog.dismiss();
                                                    return false;
                                                }

                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                                    Log.d(TAG, e != null ? e.getMessage()+" :   onLoadFailed" : "null");
                                                    try {
                                                        ((TextView) (dialog.getWindow().findViewById(R.id.progress_msg))).setText("Adding data into database no image found");
                                                        dialog.setMessage("Adding data into database no image found");
                                                    } catch (Exception exp) {
                                                        Log.d(TAG, exp.getMessage());
                                                    }
                                                    InboxDbProvider dbProvider = new InboxDbProvider(context);
                                                    ArrayList<String> tag = null;
                                                    if (tags != null) tag = new ArrayList<>(tags);
                                                    long i = dbProvider.setInboxData(id,name, number, DbContainer.BlankEntry.INBOX, status, tag);
                                                    dialog.dismiss();
                                                    if (i == -1) {
                                                        //@TODO Create Dialog to Send request to none CodeFriends user a link
                                                        dialog.setMessage("Insertion failed");
                                                        Log.d(TAG, "Insertion failed");
                                                    } else {
                                                        //flag 3 : data in the database
                                                        flag[0] = 3;
                                                        /*Intent intent = new Intent(context, ChatActivity.class);
                                                        intent.putExtra("id", i);
                                                        context.startActivity(intent);*/
                                                        listener.onIsDataSet(true);
                                                    }
                                                    return false;
                                                }
                                            }).submit();
                                }catch (Exception e){
                                    e.printStackTrace();
                                    Log.d(TAG,e.getMessage());
                                    dialog.dismiss();
                                }
                            }else{
                                //flag 2 : no data found
                                flag[0] = 2;
                                Log.d(TAG,"No document found in the cloud");
                                dialog.setMessage("No document found in the cloud");
                                dialog.dismiss();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //flag 2 : no data found
                        //@TODO Send message to the contact to install codefriends
                        Log.d(TAG,"No match found");
                        Log.d(TAG,e.getMessage());
                        dialog.setMessage("No match found");
                        dialog.dismiss();
                        Toast.makeText(context,"The no match found",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, Objects.requireNonNull(task.getResult()).getDocuments().toString());
            }
        });
    }
}
