package com.acmvit.acm_app.model;

import androidx.paging.DataSource;
import androidx.room.ColumnInfo;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Discord {

    @ColumnInfo(name = "username")
    @SerializedName("username")
    private String username;

    @ColumnInfo(name = "email")
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
        return (
            "Discord{" +
            "username='" +
            username +
            '\'' +
            ", email='" +
            email +
            '\'' +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discord discord = (Discord) o;
        return (
            Objects.equals(username, discord.username) &&
            Objects.equals(email, discord.email)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}
