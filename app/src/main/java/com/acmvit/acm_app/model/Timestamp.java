package com.acmvit.acm_app.model;

import com.google.gson.annotations.SerializedName;

public class Timestamp {

    @SerializedName("_seconds")
    private long seconds;

    @SerializedName("_nanoseconds")
    private long nanoseconds;

    public Timestamp(long seconds, long nanoseconds) {
        this.seconds = seconds;
        this.nanoseconds = nanoseconds;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public long getNanoseconds() {
        return nanoseconds;
    }

    public void setNanoseconds(long nanoseconds) {
        this.nanoseconds = nanoseconds;
    }
}
