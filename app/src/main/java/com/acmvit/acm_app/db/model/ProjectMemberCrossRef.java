package com.acmvit.acm_app.db.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.acmvit.acm_app.model.User;

import org.jetbrains.annotations.NotNull;

@Entity(primaryKeys = {"user_id", "project_id"}, indices = {@Index("project_id")})
public class ProjectMemberCrossRef {

    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId;

    @NonNull
    @ColumnInfo(name = "project_id")
    private String projectId;

    public ProjectMemberCrossRef(@NotNull String userId, @NotNull String projectId) {
        this.userId = userId;
        this.projectId = projectId;
    }

    @NotNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NotNull String userId) {
        this.userId = userId;
    }

    @NotNull
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(@NotNull String projectId) {
        this.projectId = projectId;
    }
}
