package com.acmvit.acm_app.model;

public class Request {

    private String icon;
    private String name;
    private String projectName;

    public Request(String icon, String name, String projectName) {
        this.icon = icon;
        this.name = name;
        this.projectName = projectName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
