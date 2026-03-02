package com.ftbd.fieldsalesautomation.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ftbd.fieldsalesautomation.R;
import com.ftbd.fieldsalesautomation.models.ViewOrderItem;
import com.ftbd.fieldsalesautomation.pos.OrderItem;

import org.jspecify.annotations.NonNull;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
    private List<ViewOrderItem> items;

    public MemoAdapter(List<ViewOrderItem> items) { this.items = items; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // একটি ছোট row layout লাগবে (item_memo_row.xml)
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // এখানে আপনার মডেলটি ব্যবহার করুন
        ViewOrderItem item = items.get(position);

        // ১. আপনার মডেলের getItemName() মেথডটি ব্যবহার করুন
        holder.name.setText(item.getItemName());

        // ২. সরাসরি পাবলিক ভেরিয়েবল qty ব্যবহার করুন
        holder.qty.setText(String.valueOf(item.qty));

        // ৩. সরাসরি পাবলিক ভেরিয়েবল price ব্যবহার করুন
        holder.amount.setText("৳ " + item.price);
    }
    @Override
    public int getItemCount() { return items.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, qty, amount;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tvRowItemName);
            qty = v.findViewById(R.id.tvRowQty);
            amount = v.findViewById(R.id.tvRowAmount);
        }
    }
}