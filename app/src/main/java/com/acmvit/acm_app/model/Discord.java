package com.acmvit.acm_app.model;

import com.google.gson.annotations.SerializedName;

public class Discord {
    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    public Discord(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Discord{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
