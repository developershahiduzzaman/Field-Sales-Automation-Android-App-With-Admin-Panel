package com.ftbd.fieldsalesautomation.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftbd.fieldsalesautomation.models.CartManager;
import com.ftbd.fieldsalesautomation.R;
import com.ftbd.fieldsalesautomation.models.Product;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    private List<Product> productListFull; // অরিজিনাল ডাটা সংরক্ষণের জন্য

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
        // শুরুতে অরিজিনাল লিস্টের একটি কপি তৈরি করে রাখা
        this.productListFull = new ArrayList<>(productList);
    }

    // ১. সার্চ ফিল্টার মেথড
    public void filter(String text) {
        List<Product> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            filteredList.addAll(productListFull);
        } else {
            String filterPattern = text.toLowerCase().trim();
            for (Product item : productListFull) {
                // নামের সাথে মিললে লিস্টে যোগ করা
                if (item.getName().toLowerCase().contains(filterPattern)) {
                    filteredList.add(item);
                }
            }
        }
        this.productList = filteredList;
        notifyDataSetChanged();
    }

    // ২. সার্ভার থেকে ডাটা লোড হওয়ার পর কল করার জন্য মেথড
    public void updateList(List<Product> newList) {
        this.productList = newList;
        this.productListFull = new ArrayList<>(newList); // ফুল লিস্ট আপডেট
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.name.setText(product.getName());
        holder.price.setText("৳ " + product.getPrice());
        holder.stock.setText("Stock: " + product.getStock() + " pcs");

        holder.btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product, 1);
            Toast.makeText(v.getContext(), product.getName() + " কার্টে যোগ হয়েছে", Toast.LENGTH_SHORT).show();
        });

        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.pimg)
                .error(R.drawable.pimg)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, stock;
        ImageView image;
        ImageButton btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvProductName);
            price = itemView.findViewById(R.id.tvProductPrice);
            stock = itemView.findViewById(R.id.tvProductStock);
            image = itemView.findViewById(R.id.ivProduct);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}