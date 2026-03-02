package com.ftbd.fieldsalesautomation.models;

import com.google.gson.annotations.SerializedName;

public class DueResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("total_due")
    private double totalDue;

    // গেটার মেথড
    public String getStatus() {
        return status;
    }

    public double getTotalDue() {
        return totalDue;
    }

    // সেটার মেথড
    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalDue(double totalDue) {
        this.totalDue = totalDue;
    }
}