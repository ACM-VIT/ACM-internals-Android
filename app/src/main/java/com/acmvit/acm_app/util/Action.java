package com.acmvit.acm_app.util;

import android.app.Activity;

import java.util.Map;

public class Action {
    public enum MainEvent {
        TOAST,
        SNACKBAR,
        HIDE_KEYBOARD,
        CALLBACK
    }

    private final MainEvent type;
    private String message;
    private Map<String, String> data;
    private Callback callback;

    public Action(MainEvent type, String message, Map<String, String> data, Callback callback) {
        this.type = type;
        this.message = message;
        this.data = data;
        this.callback = callback;
    }

    public Action(MainEvent type, String message) {
        this.type = type;
        this.message = message;
    }

    public Action(MainEvent type, String message, Map<String, String> data) {
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public Action(MainEvent type) {
        this.type = type;
    }

    public MainEvent getType() {
        return type;
    }

    public Map<String, String> getData() {
        return data;
    }

    public interface Callback{
        void run(Activity activity);
    }

    public Callback getCallback() {
        return callback;
    }

    public String getMessage() {
        return message;
    }
}
