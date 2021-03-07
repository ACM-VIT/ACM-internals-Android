package com.acmvit.acm_app.model;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectUpdateBody {
    private String name;
    private String disp;
    private String icon;
    private List<String> tags;

    public ProjectUpdateBody(String name, String disp, String icon, List<String> tags) {
        this.name = name;
        this.disp = disp;
        this.icon = icon;
        this.tags = tags;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
