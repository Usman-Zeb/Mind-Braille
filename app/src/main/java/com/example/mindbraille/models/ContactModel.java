package com.example.mindbraille.models;

import android.widget.ImageView;

import java.io.Serializable;

public class ContactModel implements Serializable {

    private String name;
    private String email;
    private ImageView dp;

    public ContactModel(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ImageView getDp() {
        return dp;
    }

    public void setDp(ImageView dp) {
        this.dp = dp;
    }
}
