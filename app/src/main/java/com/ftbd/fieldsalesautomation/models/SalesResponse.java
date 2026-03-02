package com.ftbd.fieldsalesautomation.models;

import com.google.gson.annotations.SerializedName;

public class SalesResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("today_sales") //
    private double todaySales;

    @SerializedName("today_target")
    private double todayTarget;

    @SerializedName("today_paid")
    private double todayPaid;

    @SerializedName("total_due")
    private double totalDue;

    // Getter Method
    public double getTodaySales() {
        return todaySales;
    }
    public double getTodayTarget() {
        return todayTarget;
    }

    public double getTodayPaid() { return todayPaid; }
    public double getTotalDue() { return totalDue; }
}