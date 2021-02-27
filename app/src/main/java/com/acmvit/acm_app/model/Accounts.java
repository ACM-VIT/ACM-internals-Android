package com.acmvit.acm_app.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Accounts {
    @SerializedName("discord")
    @Embedded(prefix = "discord_")
    private Discord discord;

    public Accounts(Discord discord) {
        this.discord = discord;
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
            ", discord='" +
            discord +
            '\'' +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accounts accounts = (Accounts) o;
        return Objects.equals(discord, accounts.discord);
    }
}
