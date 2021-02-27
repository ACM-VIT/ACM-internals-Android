package com.acmvit.acm_app.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tag")
public class Tag {

    @NonNull
    @ColumnInfo(name = "tag", collate = ColumnInfo.NOCASE)
    @SerializedName("name")
    @PrimaryKey
    private String tag;

    public Tag(@NonNull String tag) {
        this.tag = tag;
    }

    @NonNull
    public String getTag() {
        return tag;
    }

    public void setTag(@NonNull String tag) {
        this.tag = tag;
    }
}
