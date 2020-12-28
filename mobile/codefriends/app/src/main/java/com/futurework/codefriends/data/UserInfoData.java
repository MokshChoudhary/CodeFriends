package com.futurework.codefriends.data;

public class UserInfoData {

    private String name,image,email,status;
    private String[] tags;
    private int number;

    public UserInfoData(){}

    public UserInfoData(final String name, String image, String email, String status, String[] tags, int number) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.status = status;
        this.tags = tags;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getTags() {
        return tags;
    }

    public String getTagsAsString(){
        StringBuilder h = new StringBuilder();
        if(tags == null) return "";
        for(String l : tags){
            h.append(l).append(";");
        }
        return h.toString();
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
