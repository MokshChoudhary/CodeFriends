package com.futurework.codefriends.data;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Info_Holder_Data {

    private String image;
    private String name;
    private String status;
    private ArrayList<String> tags;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

    public Info_Holder_Data(){ }

    public Info_Holder_Data(String name, String status, ArrayList<String> tags  ){
        this.name = name;
        this.status = status;
        this.tags = tags;
    }

    public Info_Holder_Data(String image, String name, String status, ArrayList<String> tags  ){
        this.image = image;
        this.name = name.trim();
        this.tags = tags;
        this.status = status.trim();
    }

    public Info_Holder_Data(String image, String name, String status){
        this.image = image;
        this.name = name.trim();
        this.tags = null;
        this.status = status.trim();
    }


}
