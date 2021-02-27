package com.acmvit.acm_app.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "user")
public class User implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    @SerializedName("id")
    private String user_id;

    @ColumnInfo(name = "name")
    @SerializedName("full_name")
    private String name;

    @ColumnInfo(name = "profilePic")
    @SerializedName("profilePic")
    private String dp;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String disp;

    @ColumnInfo(name = "email")
    @SerializedName("email")
    private String email;

    @Embedded
    @SerializedName("accounts_connected")
    private Accounts accounts;

    public User(
            @NotNull String user_id,
            String name,
            String email,
            Accounts accounts,
            String dp,
            String disp
    ) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.accounts = accounts;
        this.dp = dp;
        this.disp = disp;
    }

    @Ignore
    public User(@NonNull String user_id, String name, String dp) {
        this.user_id = user_id;
        this.name = name;
        this.dp = dp;
    }

    @NotNull
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(@NotNull String user_id) {
        this.user_id = user_id;
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

    public Accounts getAccounts() {
        return accounts;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getDisp() {
        return disp;
    }

    public void setDisp(String disp) {
        this.disp = disp;
    }

    @NotNull
    @Override
    public String toString() {
        return (
                "User{" +
                        "id='" +
                        user_id +
                        '\'' +
                        ", name='" +
                        name +
                        '\'' +
                        ", email='" +
                        email +
                        '\'' +
                        ", accounts=" +
                        accounts +
                        '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return user_id.equals(user.user_id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(dp, user.dp) &&
                Objects.equals(disp, user.disp) &&
                Objects.equals(email, user.email) &&
                Objects.equals(accounts, user.accounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, name, dp, disp, email, accounts);
    }
}
