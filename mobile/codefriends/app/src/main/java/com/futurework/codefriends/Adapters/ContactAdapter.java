package com.futurework.codefriends.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.futurework.codefriends.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ContactAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private static final String TAG = "ContactAdapter";
    private String name[];
    private String number[];
    private String imageUri[];
    private LayoutInflater inflater;

    public ContactAdapter(Context context, ArrayList<String> name, ArrayList<String> number, ArrayList<String> imageUri) {
        this.name = new String[name.size()];
        this.number = new String[number.size()];
        this.imageUri = new String[imageUri.size()];
        this.name = name.toArray(this.name);
        this.number = number.toArray(this.number);
        this.imageUri = imageUri.toArray(this.imageUri);
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
                into(image);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG,name[i]);
        //Todo Check that user in the cloud database or not
    }
}
