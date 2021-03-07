package com.acmvit.acm_app.model;

public class BasicProject {
    private String id;
    private String name;
    private String disp;
    private ProjectStatus status;

    public BasicProject(String id, String name, String disp, ProjectStatus status) {
        this.id = id;
        this.name = name;
        this.disp = disp;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisp() {
        return disp;
    }

    public void setDisp(String disp) {
        this.disp = disp;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
