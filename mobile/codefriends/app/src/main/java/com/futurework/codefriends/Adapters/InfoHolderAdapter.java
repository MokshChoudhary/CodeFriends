package com.futurework.codefriends.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.futurework.codefriends.R;
import com.futurework.codefriends.data.Info_Holder_Data;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class InfoHolderAdapter extends ArrayAdapter<Info_Holder_Data> implements View.OnClickListener, View.OnLongClickListener {

    public static final String THIS_IS_A_LONG_CLICK_ = "This is a long click ";
    private ArrayList<Info_Holder_Data> dataSet;
    private Context context;

    @Override
    public boolean onLongClick(View view) {
        Snackbar.make(view, THIS_IS_A_LONG_CLICK_, Snackbar.LENGTH_LONG)
                .setAction("No action", null).show();
        int position=(Integer) view.getTag();
        Info_Holder_Data dataModel= getItem(position);
        return false;
    }

    private static class ViewHolder{
        TextView name,status,text;
        ListView listView;
        ImageView image;
    }

    public InfoHolderAdapter(ArrayList<Info_Holder_Data> data, @NonNull Context context) {
        super(context, R.layout.activity_info_holder,data);
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Info_Holder_Data dataModel= getItem(position);

    }
    private final int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Info_Holder_Data dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_info_holder, parent, false);
            viewHolder.name = convertView.findViewById( R.id.name);
            viewHolder.status = convertView.findViewById(R.id.status);
            viewHolder.image =  convertView.findViewById(R.id.image);
            viewHolder.text = convertView.findViewById(R.id.textView3);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.nav_default_pop_enter_anim : R.anim.fragment_open_exit);
        result.startAnimation(animation);

        viewHolder.name.setText(dataModel.getName());
        viewHolder.status.setText(dataModel.getStatus());
        viewHolder.name.setTextColor(Color.WHITE);
        viewHolder.status.setTextColor(Color.WHITE);
        viewHolder.text.setTextColor(Color.WHITE);
        Glide.with(context).
                load(dataModel.getImage())
                .placeholder(R.drawable.ic_baseline_person_24)
                .fitCenter()
                .into(viewHolder.image);
        // Return the completed view to render on screen
        return convertView;
    }
}
