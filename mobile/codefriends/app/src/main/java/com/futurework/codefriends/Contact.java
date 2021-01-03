package com.futurework.codefriends;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.futurework.codefriends.Adapters.ContactAdapter;

import java.util.ArrayList;

public class Contact extends AppCompatActivity {
    private static final String TAG = "Contact";
    private TextView addTeam, addContact;
    private ArrayList<String> name,number,imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect);
        addTeam = findViewById(R.id.add_team);
        addContact = findViewById(R.id.add_contact);
        final ProgressBar bar = findViewById(R.id.progressBar);
        final ListView list = findViewById(R.id.contactListView);
        name = new ArrayList<>();
        number = new ArrayList<>();
        imageUri = new ArrayList<>();
        bar.setVisibility(ProgressBar.INVISIBLE);

        if (ContextCompat.checkSelfPermission(Contact.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            final ContentResolver cr = getContentResolver();

            @SuppressLint("Recycle") final Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

            if ((cur != null ? cur.getCount() : 0) > 0) {
                bar.setVisibility(View.VISIBLE);
                while (cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String imageUri = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Contact.this.name.add(name);
                            Contact.this.imageUri.add(imageUri);
                            Contact.this.number.add(phoneNo);
                        }
                        pCur.close();
                    }
                }
            }
            final ContactAdapter adapter = new ContactAdapter(Contact.this,name,number,imageUri);

            list.setAdapter(adapter);
            bar.setVisibility(View.INVISIBLE);

            list.setOnItemClickListener(adapter);

            addContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            addTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        } else {
            ActivityCompat
                    .requestPermissions(
                            Contact.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            10);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == 10) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.recreate();
                // Showing the toast message
                Toast.makeText(Contact.this,
                        "Application use this Permission to show you the contact of your on the CodeFriends!",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(), "This application required a Contact read permission. Without this activity have no longer useful! ", Toast.LENGTH_LONG).show();
                Contact.this.finish();
            }
        }else{
            this.recreate();
        }

    }
}