package com.ftbd.fieldsalesautomation;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    // @SerializedName ensures the field matches the key in your Laravel API JSON
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    // Constructor to initialize data when calling from LoginActivity
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters for Retrofit to access the data
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}