package com.acmvit.acm_app.db.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
    primaryKeys = { "project_id", "tag" },
    indices = { @Index("project_id"), @Index("tag") }
)
public class ProjectTagCrossRef {

    @NonNull
    @ColumnInfo(name = "project_id")
    private String projectId;

    @NonNull
    @ColumnInfo(name = "tag")
    private String tag;

    public ProjectTagCrossRef(@NonNull String projectId, @NonNull String tag) {
        this.projectId = projectId;
        this.tag = tag;
    }

    @NonNull
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(@NonNull String projectId) {
        this.projectId = projectId;
    }

    @NonNull
    public String getTag() {
        return tag;
    }

    public void setTag(@NonNull String tag) {
        this.tag = tag;
    }
}
