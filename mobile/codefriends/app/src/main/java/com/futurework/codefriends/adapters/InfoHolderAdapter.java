package com.futurework.codefriends.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.futurework.codefriends.R;
import com.futurework.codefriends.Service.Service;
import com.futurework.codefriends.data.UserInfoData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class InfoHolderAdapter extends ArrayAdapter<UserInfoData> implements View.OnClickListener, View.OnLongClickListener {

    public static final String THIS_IS_A_LONG_CLICK_ = "This is a long click ";
    private ArrayList<UserInfoData> dataSet = new ArrayList<>();
    public Context context;
    private String TAG = "InfoHolderAdapter";

    @Override
    public boolean onLongClick(View view) {
        Snackbar.make(view, THIS_IS_A_LONG_CLICK_, Snackbar.LENGTH_LONG)
                .setAction("No action", null).show();
        //int position=(Integer) view.getTag();
        //Info_Holder_Data dataModel= getItem(position);
        return false;
    }

    private static class ViewHolder{
        TextView name,status,text,tagText;
        ListView listView;
        ImageView image;
        LinearLayout layout;
        ArrayAdapter<String> adapter ;
    }

    public InfoHolderAdapter(ArrayList<UserInfoData> data, @NonNull Context context) {
        super(context, R.layout.activity_info_holder,data);
        this.dataSet = data;
        this.context = context;
        Log.d(TAG,"in infoHolderAdapter");
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        UserInfoData dataModel= getItem(position);
        Log.i(TAG,"onclick "+dataModel.getName());

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final UserInfoData dataModel = getItem(position);
        Log.d(TAG,"in getView");
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_info_holder, parent, false);
            viewHolder.name = convertView.findViewById( R.id.name);
            viewHolder.status = convertView.findViewById(R.id.status);
            viewHolder.image =  convertView.findViewById(R.id.image);
            viewHolder.text = convertView.findViewById(R.id.textView3);
            viewHolder.layout = convertView.findViewById(R.id.tags_list);
            //viewHolder.listView = convertView.findViewById(R.id.tag_list);
            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
        }

        int lastPosition = -1;
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.nav_default_pop_enter_anim : R.anim.fragment_open_exit);
        result.startAnimation(animation);

        final CircularProgressDrawable progressDrawable = new CircularProgressDrawable(viewHolder.image.getContext());
        String email = null;
        if(dataModel.getEmail() != null)
            email = new Service().removeExtraFromString(dataModel.getEmail());

        for(String tag : dataModel.getTags()){
            TextView text  = new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            text.setText(tag);
            text.setLayoutParams(params);
            Log.d(TAG, tag);
            viewHolder.layout.addView(text);
        }

        viewHolder.name.setText(dataModel.getName());
        viewHolder.status.setText(dataModel.getStatus());
        viewHolder.name.setTextColor(Color.WHITE);
        viewHolder.status.setTextColor(Color.WHITE);
        viewHolder.text.setTextColor(Color.WHITE);
        Log.d(TAG,dataModel.getEmail()+"");
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("UserImages/"+email+"").getDownloadUrl()
            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                        .load(uri)
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .fitCenter()
                        .into(viewHolder.image).onLoadStarted(progressDrawable);
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Glide.with(context)
                        .load(R.drawable.ic_baseline_person_24)
                        .fitCenter()
                        .into(viewHolder.image).onLoadStarted(progressDrawable);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
