package com.futurework.codefriends;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.futurework.codefriends.adapters.InfoHolderAdapter;
import com.futurework.codefriends.Database.DbContainer;
import com.futurework.codefriends.Database.DbHelper;
import com.futurework.codefriends.Database.InboxDb.InboxDbProvider;
import com.futurework.codefriends.Database.MessageDb.MessageDbProvider;
import com.futurework.codefriends.Database.UserDb.UserDbProvider;
import com.futurework.codefriends.Service.Service;
import com.futurework.codefriends.data.ChatData;
import com.futurework.codefriends.data.UserInfoData;
import com.futurework.codefriends.templates.UserInfoDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private android.widget.ListView ListView;
    private FirebaseAuth mAuth;
    private ArrayList<UserInfoData> Data;
    private InfoHolderAdapter adapter;
    private InboxDbProvider dbProvider;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mRef;
    private final FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            Log.d(TAG,"null object ");
        }
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Chatting");
        ImageView search = findViewById(R.id.search_button);
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
                startActivity(new Intent(MainActivity.this, ChatActivity2.class).putExtra("id", data.getId()), option.toBundle());
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
    protected void onStart() {
        super.onStart();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(MainActivity.this, SplashScreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            MainActivity.this.finish();
                        }
                    }, 2000);
                }
            }
        };

        final UserInfoData info = new UserDbProvider(MainActivity.this).getUser();
        mRef.child(info.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                final ChatData data = snapshot.getValue(ChatData.class);
                if(data==null)
                    return;
                DbHelper provider = new DbHelper(MainActivity.this);
                @SuppressLint("Recycle")
                Cursor cursor = provider.getReadableDatabase().query(DbContainer.BlankEntry.INBOX_TABLE_NAME,null,DbContainer.BlankEntry._ID+" =? ",new String[]{data.getSenderId()},null,null,null);
                if(cursor != null )
                {
                    Log.d(TAG, data.type + " " + data.getSenderId() + " " + data.getDate()  + " " + data.getTextMsg());
                    MessageDbProvider msg = new MessageDbProvider(new DbHelper(MainActivity.this).getWritableDatabase());
                    msg.setMessage(ChatData.RECEIVER_TEXT, data.getTextMsg().trim(),data.getDate(), data.getSenderId());
                }else{
                    mStore.collection("Users")
                            .whereEqualTo("id",info.getId())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                        Map<String, Object> data1 = documentSnapshot.getData();
                                        assert data1 != null;
                                        final String id = (String) data1.get("id");
                                        final String name = (String) data1.get("name");
                                        final String email = (String) data1.get("email");
                                        final String number = (String) data1.get("number");
                                        final String imageUri = (String) data1.get("image");
                                        final String status = (String) data1.get("status");
                                        final String[] tags;
                                        String[] tags1;

                                        if (!("-1".equals(data1.get("tags"))))
                                            try{
                                                tags1 = (String[]) data1.get("tags");
                                            }catch (Exception ignored){
                                                tags1 = ArrayListToString((ArrayList<String>) data1.get("tags"));
                                            }
                                        else
                                            tags1 = null;

                                        tags = tags1;
                                        final UserInfoData infoData = new UserInfoData(id, name, imageUri, email, status, tags, number);

                                        try {
                                            Glide.with(MainActivity.this)
                                                    .asBitmap()
                                                    .load(FirebaseStorage.getInstance().getReference().child("UserImage/" + new Service().removeExtraFromString(email)).getDownloadUrl())
                                                    .listener(new RequestListener<Bitmap>() {
                                                        @Override
                                                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                                            InboxDbProvider dbProvider = new InboxDbProvider(MainActivity.this);
                                                            long i = dbProvider.setInboxData(resource, id, name, number, DbContainer.BlankEntry.INBOX, status, new ArrayList<>(Arrays.asList(tags)));
                                                            if (i == -1) {
                                                                Log.d(TAG, "Insertion failed");
                                                            } else {
                                                                MessageDbProvider msg = new MessageDbProvider(new DbHelper(MainActivity.this).getWritableDatabase());
                                                                if(msg.setMessage(ChatData.RECEIVER_TEXT, data.getTextMsg().trim(),data.getDate(), data.getSenderId()) == -1)
                                                                    Log.d(TAG,"Unable to insert data ");
                                                                else
                                                                    Log.d(TAG,"Data is inserted");
                                                                adapter.add(infoData);
                                                            }
                                                            return false;
                                                        }

                                                        @Override
                                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                                            Log.d(TAG, e != null ? e.getMessage() + " :   onLoadFailed" : "null");
                                                            InboxDbProvider dbProvider = new InboxDbProvider(MainActivity.this);
                                                            ArrayList<String> tag = null;
                                                            if (tags != null)
                                                                tag = new ArrayList<>(Arrays.asList(tags));
                                                            long i = dbProvider.setInboxData(id, name, number, DbContainer.BlankEntry.INBOX, status, tag);

                                                            if (i == -1) {
                                                                Log.d(TAG, "Insertion failed");
                                                            } else {
                                                                MessageDbProvider msg = new MessageDbProvider(new DbHelper(MainActivity.this).getWritableDatabase());
                                                                if(msg.setMessage(ChatData.RECEIVER_TEXT, data.getTextMsg().trim(),data.getDate(), data.getSenderId()) == -1)
                                                                    Log.d(TAG,"Unable to insert data ");
                                                                else
                                                                    Log.d(TAG,"Data is inserted");
                                                                adapter.add(infoData);
                                                            }
                                                            return true;
                                                        }
                                                    }).submit();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.d(TAG, e.getMessage());
                                        }

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG,e.getMessage());
                        }
                    });
                }
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
        if (dbProvider.getUserData().size() != 0) {
            Data = dbProvider.getUserData();
            adapter = new InfoHolderAdapter(Data, getApplicationContext());
            ListView.setAdapter(adapter);
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    private String[] ArrayListToString(ArrayList<String> list){
        final String[] l = new String[list.size()];
        for(int i = 0 ; i<list.size() ; i++){
            l[i] = list.get(i);
        }
        return l;
    }

    public void fclick(View view) {
        startActivity(new Intent(MainActivity.this, Contact.class));
    }

}