package com.acmvit.acm_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AuthToken {
    private static final String TAG = "AuthToken";
    private final double TOKEN_EXPIRY = 1.037e+7;
    public static final String TOKEN_TYPE = "Bearer";

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    private Date expiryDate;

    public String getAccessToken() {
        return accessToken;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    private void calculateExpiry(){
        Calendar calendar = Calendar.getInstance();
        double expiry = calendar.getTimeInMillis() + TOKEN_EXPIRY * 1000D;
        expiryDate = new Date((long) expiry);
    }

    public boolean isNull(){
        return accessToken == null || refreshToken == null;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "TOKEN_EXPIRY=" + TOKEN_EXPIRY +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiryDate=" + expiryDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com.akribase.oauthloginimplementation.model.AuthToken authToken = (com.akribase.oauthloginimplementation.model.AuthToken) o;
        return Objects.equals(accessToken, authToken.accessToken) &&
                Objects.equals(refreshToken, authToken.refreshToken);
    }

}
