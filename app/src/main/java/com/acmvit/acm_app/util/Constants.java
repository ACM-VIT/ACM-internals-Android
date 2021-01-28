package com.acmvit.acm_app.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final int MIN_PASSWORD_LENGTH = 6;

    public static class ProjectNotification {

        public static final String TOPIC = "projects";
        public static final String MSG_TITLE = "title";
        public static final String MSG_BODY = "desc";
        public static final String CHANNEL_ID = "PROJECTS_CHANNEL";
        public static final String CHANNEL_NAME = "projects";
        public static final String CHANNEL_DISP =
            "You get notified when your teammates create new projects";
    }

    public static class Backend {

        public static final String BASE_URL =
            "https://us-central1-acminternal.cloudfunctions.net";
        public static final String SUCCESS_STATUS = "10000";
        public static final int TOKEN_RETRY_COUNT = 2;
        public static final String DP_STORAGE_LOC = "profileDP";
    }

    public static class Discord {

        public static final String REDIRECT_URL = "oli://auth";
        public static final String AUTH_URL =
            "https://discord.com/api/oauth2/authorize";
        public static final String TOKEN_URL =
            "https://discord.com/api/oauth2/token";
        public static final String[] SCOPES = { "identify", "email" };
        public static final String PROMPT = "consent";
    }
}
