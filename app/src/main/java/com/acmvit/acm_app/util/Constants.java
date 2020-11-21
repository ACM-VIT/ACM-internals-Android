package com.acmvit.acm_app.util;

public class Constants {
    public static class ProjectNotification{
        public static final String MSG_TITLE = "title";
        public static final String MSG_BODY = "body";
        public static final String CHANNEL_ID = "PROJECTS_CHANNEL";
        public static final String CHANNEL_NAME = "projects";
        public static final String CHANNEL_DISP = "You get notified when your teammates create new projects";
    }

    public static class Backend {
        public static String BASE_URL = "https://us-central1-acminternal.cloudfunctions.net";
        public static String SUCCESS_STATUS = "10000";
        public static int TOKEN_RETRY_COUNT = 2;
    }

}
