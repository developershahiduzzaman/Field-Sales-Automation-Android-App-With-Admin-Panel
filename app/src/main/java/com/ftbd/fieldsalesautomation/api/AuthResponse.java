package com.ftbd.fieldsalesautomation.api;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("user_id")
    private int userId;
    @SerializedName("token")
    private String token;

    @SerializedName("message")
    private String message;

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }
    public int getUserId() {
        return userId;
    }

}