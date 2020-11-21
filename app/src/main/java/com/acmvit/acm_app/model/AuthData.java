package com.acmvit.acm_app.model;

import com.google.gson.annotations.SerializedName;

public class AuthData {
    @SerializedName("user")
    private User user;

    @SerializedName("tokens")
    private AuthToken token;

    public AuthData(User user, AuthToken token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AuthToken getToken() {
        return token;
    }

    public void setToken(AuthToken token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AuthData{" +
                "user=" + user +
                ", token=" + token +
                '}';
    }
}
