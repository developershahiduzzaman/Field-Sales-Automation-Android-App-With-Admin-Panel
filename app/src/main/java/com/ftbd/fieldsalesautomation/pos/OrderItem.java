package com.ftbd.fieldsalesautomation.pos;

import com.google.gson.annotations.SerializedName;

public class OrderItem {
    @SerializedName("id")
    private int productId;

    @SerializedName("qty")
    private int quantity;

    @SerializedName("price")
    private double price;

    @SerializedName("name")
    public String name;

    public OrderItem(int productId, int quantity, double price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }
}
