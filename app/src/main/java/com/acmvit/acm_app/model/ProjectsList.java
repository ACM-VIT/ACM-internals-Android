package com.acmvit.acm_app.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProjectsList {

    @SerializedName("allProjects")
    private List<Project> allProjects;

    public ProjectsList(List<Project> allProjects) {
        this.allProjects = allProjects;
    }

    public List<Project> getAllProjects() {
        return allProjects;
    }

    public void setAllProjects(List<Project> allProjects) {
        this.allProjects = allProjects;
    }
}
