package com.ftbd.fieldsalesautomation.models;

import com.google.gson.annotations.SerializedName;

public class ViewOrderItem {
    @SerializedName("quantity")
    public int qty;

    @SerializedName("price")
    public String price;

    @SerializedName("product")
    public ProductData product;

    public class ProductData {
        @SerializedName("name")
        public String name;
    }

    // সহজে নাম পাওয়ার জন্য একটি মেথড
    public String getItemName() {
        return (product != null) ? product.name : "Unknown Product";
    }
}