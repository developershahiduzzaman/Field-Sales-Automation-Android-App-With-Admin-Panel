package com.ftbd.fieldsalesautomation.models;

import com.google.gson.annotations.SerializedName;

public class Shop {
    private int id;

    @SerializedName("shop_name")
    private String shop_name;

    private String phone;
    private String address;
    private String location;

    public Shop(String shop_name, String phone, String address, String location) {
        this.shop_name = shop_name;
        this.phone = phone;
        this.address = address;
        this.location = location;
    }

    // Getters
    public String getShopName() { return shop_name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getLocation() { return location; }

    // ToString
    @Override
    public String toString() {
        return shop_name;
    }
}