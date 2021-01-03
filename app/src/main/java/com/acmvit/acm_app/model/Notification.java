package com.acmvit.acm_app.model;

public class Notification {

    private String icon;
    private String text;

    public Notification(String icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
