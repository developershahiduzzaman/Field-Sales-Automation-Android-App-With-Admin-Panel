package com.ftbd.fieldsalesautomation.models;

import com.google.gson.annotations.SerializedName;

public class OrderModel {
    @SerializedName("id")
    private int orderId;

    // আপনার লারাভেল কন্ট্রোলার এটি দিয়েই শপ খুঁজে বের করবে
    @SerializedName("customer_id")
    private int customerId;

    @SerializedName("total_amount")
    private String totalAmount;

    @SerializedName("paid_amount")
    private String paidAmount;

    @SerializedName("due_amount")
    private String dueAmount;

    @SerializedName("created_at")
    private String date;

    @SerializedName("shop_name")
    private String shopNameFromApi;

    @SerializedName("shop")
    private ShopDetails shop;

    public String getOrderId() { return String.valueOf(orderId); }

    // এই মেথডটি এখন আপনার OrderHistoryActivity তে এরর দূর করবে
    public String getCustomerId() { return String.valueOf(customerId); }

    public String getTotalAmount() { return totalAmount != null ? totalAmount : "0.00"; }

    public String getPaidAmount() {
        return (paidAmount != null && !paidAmount.equals("null")) ? paidAmount : "0.00";
    }

    public String getDueAmount() {
        return (dueAmount != null && !dueAmount.equals("null")) ? dueAmount : "0.00";
    }

    public String getDate() {
        if (date != null && date.length() > 10) return date.substring(0, 10);
        return date;
    }

    public String getShopName() {
        if (shopNameFromApi != null && !shopNameFromApi.isEmpty() && !shopNameFromApi.equals("null")) {
            return shopNameFromApi;
        } else if (shop != null && shop.shopName != null) {
            return shop.shopName;
        }
        return "Unknown Shop";
    }

    public static class ShopDetails {
        @SerializedName("shop_name")
        public String shopName;
    }
}