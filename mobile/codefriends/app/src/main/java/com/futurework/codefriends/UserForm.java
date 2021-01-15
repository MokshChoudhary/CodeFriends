package com.futurework.codefriends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.futurework.codefriends.Database.UserDb.UserDbProvider;
import com.futurework.codefriends.Service.Service;
import com.futurework.codefriends.data.UserInfoData;
import com.futurework.codefriends.templates.CustomProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class UserForm extends AppCompatActivity {

    private Button button ;
    private androidx.appcompat.widget.AppCompatEditText name,email,number,status;
    private ImageView image;
    private CustomProgressBar progressDialog;
    private String TAG = "UserForm";
    private Spinner spinner;
    private boolean flag = true;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public interface OnDataListener{
        void onStringChange(String uri);
        void onBooleanChange(Boolean flag);
        void onBitmapChange(Bitmap bitmap);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        final Animation flick = AnimationUtils.loadAnimation(this, R.anim.button_flick);
        button = findViewById(R.id.form_button);
        name = findViewById(R.id.form_name);
        email = findViewById(R.id.form_email);
        spinner = findViewById(R.id.spinner);
        number = findViewById(R.id.form_no);
        status = findViewById(R.id.form_status);
        image = findViewById(R.id.form_image);
        String[] a = Service.getCountry();
        String[] b = Service.getCode();
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, b);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        final CustomProgressBar progressBar = new CustomProgressBar(UserForm.this);
        progressBar.setTitle("Pleas wait");
        progressBar.setMessage("Working on your information");
        progressBar.show();
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            if (!user.getDisplayName().isEmpty())
                                name.setText(user.getDisplayName().trim());
                            if (!user.getEmail().isEmpty()) {
                                email.setText(user.getEmail().trim());
                                email.setEnabled(false);
                            }
                            if (user.getPhoneNumber() != null)
                                if (!user.getPhoneNumber().isEmpty()) {
                                    number.setText(user.getPhoneNumber());
                                    number.setEnabled(false);
                                }
                            if (user.getPhotoUrl() != null)
                                Glide.with(getApplicationContext()).
                                        asBitmap().
                                        load(user.getPhotoUrl()).
                                        into(image);
                        }
                        if (flag)
                            progressBar.dismiss();
                    }
                }, 5000);

        status.setText("I am Available");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(flick);

                if (!name.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !number.getText().toString().isEmpty()) {
                    progressDialog = new CustomProgressBar(UserForm.this);

                    progressDialog.setTitle("Saving your information on");
                    progressDialog.setMessage("Cloud");
                    progressDialog.show();
                    //Uploading the image
                    ByteArrayOutputStream bitOutputStream = new ByteArrayOutputStream();
                    ((BitmapDrawable) image.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bitOutputStream);
                    byte[] data = bitOutputStream.toByteArray();
                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    UploadTask task = storage.getReference("UserImages/")
                            .child(new Service().removeExtraFromString(email.getText().toString().trim())).putBytes(data);
                    task.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Log.d(TAG, "Uploading result of Image is " + task.getResult());
                            //uploading data to cloud

                            String id = UUID.randomUUID().toString().replace("-", "");
                            final UserInfoData data = new UserInfoData();
                            data.setId(id);
                            data.setName(name.getText().toString().trim());
                            data.setImage(Objects.requireNonNull(task.getResult().getUploadSessionUri()).toString().trim());
                            data.setEmail(email.getText().toString().trim());
                            data.setNumber(number.getText().toString().trim());
                            data.setStatus(Objects.requireNonNull(status.getText()).toString().trim());
                            final List list = Arrays.asList("C++", "Java", "Android");

                            Map<String, Object> info = new HashMap<>();
                            info.put("id", id);
                            info.put("name", data.getName());
                            info.put("image", data.getImage());
                            info.put("email", data.getEmail());
                            info.put("status", data.getStatus());
                            info.put("number", data.getNumber());
                            info.put("tags", list);

                            //@Todo Check if email or number exist in database

                            FirebaseFirestore.getInstance().collection("Users")
                                    .document(new Service().removeExtraFromString(data.getEmail()))
                                    .set(info)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void mVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                            progressDialog.setMessage("Device");
                                            //Saving data on user device
                                            UserDbProvider dbProvider = new UserDbProvider(getApplicationContext());
                                            if (dbProvider.insertUserData(data) != -1) {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(UserForm.this, MainActivity.class));
                                                UserForm.this.finish();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Not able to Save data in your device! Pls restart your device.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            recreate();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}