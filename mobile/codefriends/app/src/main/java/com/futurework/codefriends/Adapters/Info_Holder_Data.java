package com.futurework.codefriends.Adapters;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Info_Holder_Data {

    Bitmap image;
    String name;
    String status;
    ArrayList<String> tags;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    Info_Holder_Data(){ }

    Info_Holder_Data(String name, String status, ArrayList<String> tags  ){
        this.name = name;
        this.status = status;
        this.tags = tags;
    }

    Info_Holder_Data(Bitmap image, String name, String status, ArrayList<String> tags  ){
        this.image = image;
        this.name = name.trim();
        this.tags = tags;
        this.status = status.trim();
    }


}
