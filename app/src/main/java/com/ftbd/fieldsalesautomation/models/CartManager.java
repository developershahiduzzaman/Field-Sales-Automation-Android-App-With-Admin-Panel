package com.ftbd.fieldsalesautomation.models;

import com.ftbd.fieldsalesautomation.pos.CartItem;
import com.ftbd.fieldsalesautomation.models.Product;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;
    private String selectedShopName = "Unknown Shop";
    private double totalAmount = 0;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) instance = new CartManager();
        return instance;
    }

    public void addToCart(Product product, int qty) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + qty);
                return;
            }
        }
        cartItems.add(new CartItem(product, qty));
    }

    public List<CartItem> getCartItems() { return cartItems; }

    public double getTotalAmount() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += (item.getProduct().getPrice() * item.getQuantity());
        }
        return total;
    }

    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public void clearCart() {
        cartItems.clear();
        totalAmount = 0;
    }

    public void setShopName(String name) { this.selectedShopName = name; }
    public String getShopName() { return selectedShopName; }
}