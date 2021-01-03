package com.acmvit.acm_app.model;

import com.google.gson.annotations.SerializedName;

public class Accounts {

    @SerializedName("google")
    private Object google;

    @SerializedName("discord")
    private Discord discord;

    public Accounts(Object google, Discord discord) {
        this.google = google;
        this.discord = discord;
    }

    public Object getGoogle() {
        return google;
    }

    public void setGoogle(Object google) {
        this.google = google;
    }

    public Discord getDiscord() {
        return discord;
    }

    public void setDiscord(Discord discord) {
        this.discord = discord;
    }

    @Override
    public String toString() {
        return (
            "Accounts{" +
            "google='" +
            google +
            '\'' +
            ", discord='" +
            discord +
            '\'' +
            '}'
        );
    }
}
