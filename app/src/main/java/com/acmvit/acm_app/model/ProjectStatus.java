package com.acmvit.acm_app.model;

import com.google.gson.annotations.SerializedName;

public enum ProjectStatus {
    @SerializedName("ideation") IDEATION,
    @SerializedName("in_progress") IN_PROGRESS,
    @SerializedName("completed") COMPLETED

}
