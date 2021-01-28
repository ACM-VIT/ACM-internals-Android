package com.acmvit.acm_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserList {
    @SerializedName("allUsers")
    private ArrayList<User> users;

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public UserList(ArrayList<User> user){
        this.users=user;
    }
}
