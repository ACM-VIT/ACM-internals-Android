package com.acmvit.acm_app.db;

import androidx.room.TypeConverter;
import com.acmvit.acm_app.model.Project;
import com.acmvit.acm_app.model.ProjectStatus;

public class Converters {

    @TypeConverter
    public static String projectStatusToString(ProjectStatus status) {
        return status == null ? null : status.name().toLowerCase();
    }

    @TypeConverter
    public static ProjectStatus StringToProjectStatus(String status) {
        if (status.equals("IMPLEMENTATION")) status = "in_progress";
        return ProjectStatus.valueOf(status.toUpperCase());
    }
}
