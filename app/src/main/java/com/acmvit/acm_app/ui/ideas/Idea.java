package com.acmvit.acm_app.ui.ideas;

import android.os.Parcel;
import android.os.Parcelable;

public class Idea implements Parcelable {

    private Integer imgResource;
    private String name;
    private String date;
    private boolean liked;
    private Integer likes_count;
    private String topicName;
    private String description;

    public Idea(
        Integer imgResource,
        String name,
        String date,
        boolean liked,
        Integer likes_count,
        String topicName,
        String description
    ) {
        this.imgResource = imgResource;
        this.name = name;
        this.date = date;
        this.liked = liked;
        this.likes_count = likes_count;
        this.topicName = topicName;
        this.description = description;
    }

    protected Idea(Parcel in) {
        if (in.readByte() == 0) {
            imgResource = null;
        } else {
            imgResource = in.readInt();
        }
        name = in.readString();
        date = in.readString();
        liked = in.readByte() != 0;
        if (in.readByte() == 0) {
            likes_count = null;
        } else {
            likes_count = in.readInt();
        }
        topicName = in.readString();
        description = in.readString();
    }

    public static final Creator<Idea> CREATOR = new Creator<Idea>() {
        @Override
        public Idea createFromParcel(Parcel in) {
            return new Idea(in);
        }

        @Override
        public Idea[] newArray(int size) {
            return new Idea[size];
        }
    };

    public Integer getImgResource() {
        return imgResource;
    }

    public void setImgResource(Integer imgResource) {
        this.imgResource = imgResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public Integer getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(Integer likes_count) {
        this.likes_count = likes_count;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (imgResource == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(imgResource);
        }
        parcel.writeString(name);
        parcel.writeString(date);
        parcel.writeByte((byte) (liked ? 1 : 0));
        if (likes_count == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(likes_count);
        }
        parcel.writeString(topicName);
        parcel.writeString(description);
    }
}
