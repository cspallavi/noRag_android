package com.example.pallavi.norag;

/**
 * Created by Pallavi on 29/06/2017.
 */

public class Get_set_pchat {

    private String username,message,image;int senderid;

    public Get_set_pchat() {
    }

    public Get_set_pchat(String username, String message,int senderid,String image) {
        this.username=username;
        this.message=message;
        this.senderid=senderid;
        this.image=image;

    }

    public String getusername() {
        return username;
    }

    public void setusername(String name) {
        this.username = name;
    }

    public String getmessage() {
        return message;
    }

    public void setsenderid(int id) {
        this.senderid=senderid;
    }

    public int getsenderid() {
        return senderid;
    }

    public void setmessage(String message) {
        this.message = message;
    }

    public void setImage(String image){this.image=image;}
    public String getImage(){return image;}

}
