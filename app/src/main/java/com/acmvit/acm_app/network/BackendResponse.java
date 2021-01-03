package com.acmvit.acm_app.network;

import com.google.gson.annotations.SerializedName;

public class BackendResponse<T> {

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data;

    public BackendResponse(String statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return (
            "AuthResponse{" +
            "statusCode='" +
            statusCode +
            '\'' +
            ", message='" +
            message +
            '\'' +
            ", data=" +
            data +
            '}'
        );
    }
}
