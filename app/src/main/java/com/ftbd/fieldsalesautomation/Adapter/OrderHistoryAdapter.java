package com.ftbd.fieldsalesautomation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftbd.fieldsalesautomation.OrderDetailsActivity;
import com.ftbd.fieldsalesautomation.R;
import com.ftbd.fieldsalesautomation.models.OrderModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private List<OrderModel> orderList;
    private Context context;
    private OnPaymentClickListener paymentListener; // ইন্টারফেস ভেরিয়েবল

    // ইন্টারফেস তৈরি
    public interface OnPaymentClickListener {
        void onPayClick(OrderModel order);
    }

    // কনস্ট্রাক্টর আপডেট (লিস্ট এবং লিসেনার সহ)
    public OrderHistoryAdapter(List<OrderModel> orderList, OnPaymentClickListener listener) {
        this.orderList = orderList;
        this.paymentListener = listener;
    }

    public void updateList(List<OrderModel> newList) {
        this.orderList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        if (order != null) {
            holder.tvShopName.setText(order.getShopName() != null ? order.getShopName() : "Unknown Shop");
            holder.tvOrderId.setText("অর্ডার আইডি: #" + order.getOrderId());
            holder.tvTotal.setText("৳ " + (order.getTotalAmount() != null ? order.getTotalAmount() : "0.00"));
            holder.tvDate.setText(order.getDate() != null ? order.getDate() : "N/A");

            // --- ডিউ ক্যালকুলেশন এবং বাটন লজিক ---
            double dueAmount = 0;
            try {
                // আপনার মডেল থেকে ডিউ অ্যামাউন্ট নিচ্ছি
                dueAmount = Double.parseDouble(order.getDueAmount());
            } catch (Exception e) {
                dueAmount = 0;
            }

            if (dueAmount > 0) {
                holder.btnPay.setVisibility(View.VISIBLE);
                holder.btnPay.setText("৳" + dueAmount + "বকেয়া নিন");
                holder.tvStatus.setText("বকেয়া");
                holder.tvStatus.setTextColor(Color.RED);

                holder.btnPay.setOnClickListener(v -> {
                    if (paymentListener != null) {
                        paymentListener.onPayClick(order);
                    }
                });
            } else {
                holder.btnPay.setVisibility(View.GONE);
                holder.tvStatus.setText("পেইড");
                holder.tvStatus.setTextColor(Color.parseColor("#2E7D32"));
            }

            // ডিটেইলস পেজে যাওয়ার লজিক
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("ORDER_ID", order.getOrderId());
                intent.putExtra("SHOP_NAME", order.getShopName());
                intent.putExtra("TOTAL_AMOUNT", order.getTotalAmount());
                intent.putExtra("PAID_AMOUNT", order.getPaidAmount());
                intent.putExtra("DUE_AMOUNT", order.getDueAmount());
                intent.putExtra("DATE", order.getDate());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvShopName, tvOrderId, tvDate, tvTotal, tvStatus;
        MaterialButton btnPay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnPay = itemView.findViewById(R.id.btnPay);
        }
    }
}