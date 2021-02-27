package com.acmvit.acm_app.db.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.acmvit.acm_app.model.ProjectStatus;
import com.acmvit.acm_app.model.User;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Entity(tableName = "project")
public class ProjectDb {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "project_id")
    private String project_id;

    @ColumnInfo(name = "status")
    private ProjectStatus projectStatus;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "desc")
    private String description;

    @ColumnInfo(name = "icon")
    private String icon;

    @ColumnInfo(name = "last_updated")
    private long timestamp;

    private String founder_id;

    public ProjectDb(@NonNull String project_id, ProjectStatus projectStatus, String name, String description, String icon, String founder_id, long timestamp) {
        this.project_id = project_id;
        this.projectStatus = projectStatus;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.founder_id = founder_id;
        this.timestamp = timestamp;
    }

    @NotNull
    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(@NotNull String project_id) {
        this.project_id = project_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getFounder_id() {
        return founder_id;
    }

    public void setFounder_id(String founder_id) {
        this.founder_id = founder_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectDb project = (ProjectDb) o;
        return founder_id.equals(project.founder_id) &&
                Objects.equals(project_id, project.project_id) &&
                projectStatus == project.projectStatus &&
                Objects.equals(name, project.name) &&
                Objects.equals(description, project.description) &&
                Objects.equals(icon, project.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project_id, projectStatus, name, description, icon, founder_id);
    }

    @Override
    public String toString() {
        return "Project{" +
                "user_id='" + project_id + '\'' +
                ", status=" + projectStatus +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", founder_id='" + founder_id + '\'' +
                '}';
    }
}
