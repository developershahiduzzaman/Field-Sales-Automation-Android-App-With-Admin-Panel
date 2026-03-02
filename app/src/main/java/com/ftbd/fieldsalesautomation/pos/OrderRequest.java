package com.ftbd.fieldsalesautomation.pos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderRequest {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("customer_id")
    private int customerId;

    @SerializedName("shop_name")
    private String shopName;

    @SerializedName("total_amount")
    private double totalAmount;

    // নতুন দুটি ফিল্ড যোগ করা হলো
    @SerializedName("paid_amount")
    private double paidAmount;

    @SerializedName("due_amount")
    private double dueAmount;

    @SerializedName("items")
    private List<OrderItemRequest> items;

    // কনস্ট্রাক্টর আপডেট (৭টি প্যারামিটার)
    public OrderRequest(int userId, int customerId, String shopName, double totalAmount, double paidAmount, double dueAmount, List<OrderItemRequest> items) {
        this.userId = userId;
        this.customerId = customerId;
        this.shopName = shopName;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.dueAmount = dueAmount;
        this.items = items;
    }

    // গেটার এবং সেটার (Retrofit এর জন্য এগুলো থাকা ভালো)
    public int getUserId() { return userId; }
    public int getCustomerId() { return customerId; }
    public String getShopName() { return shopName; }
    public double getTotalAmount() { return totalAmount; }
    public double getPaidAmount() { return paidAmount; }
    public double getDueAmount() { return dueAmount; }
    public List<OrderItemRequest> getItems() { return items; }
}