package com.ftbd.fieldsalesautomation.pos;

import com.google.gson.annotations.SerializedName;

public class OrderItemRequest {
    @SerializedName("product_id") // লারাভেল কন্ট্রোলার এটিই খুঁজছে
    private int product_id;

    @SerializedName("quantity")   // লারাভেল কন্ট্রোলার এটিই খুঁজছে
    private int quantity;

    @SerializedName("price")
    private double price;

    public OrderItemRequest(int product_id, int quantity, double price) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
    }
}