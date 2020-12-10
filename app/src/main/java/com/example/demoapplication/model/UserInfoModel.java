package com.example.demoapplication.model;

import java.io.Serializable;

public class UserInfoModel implements Serializable {

    private final String name;
    private final String email;
    private final String photoUrl;

    public UserInfoModel(String name, String email, String photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
