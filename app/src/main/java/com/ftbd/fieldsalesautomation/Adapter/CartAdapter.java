package com.ftbd.fieldsalesautomation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ftbd.fieldsalesautomation.R;
import com.ftbd.fieldsalesautomation.models.CartManager;
import com.ftbd.fieldsalesautomation.pos.CartActivity;
import com.ftbd.fieldsalesautomation.pos.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> cartItems;
    private Context context;

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.tvName.setText(item.getProduct().getName());
        holder.tvQty.setText(String.valueOf(item.getQuantity()));

        double totalPrice = item.getProduct().getPrice() * item.getQuantity();
        holder.tvPrice.setText("৳ " + totalPrice);

        // প্লাস বাটন লজিক
        holder.btnPlus.setOnClickListener(v -> {
            int currentQty = item.getQuantity();
            item.setQuantity(currentQty + 1);

            // ডাটা আপডেট
            notifyItemChanged(position);
            updateTotalInActivity();
        });

        // মাইনাস বাটন লজিক
        holder.btnMinus.setOnClickListener(v -> {
            int currentQty = item.getQuantity();
            if (currentQty > 1) {
                item.setQuantity(currentQty - 1);
                notifyItemChanged(position);
            } else {
                // ১ এর নিচে নামলে কার্ট থেকে রিমুভ
                cartItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size());
            }
            updateTotalInActivity();
        });
    }

    private void updateTotalInActivity() {
        if (context instanceof CartActivity) {
            ((CartActivity) context).updateTotalUI();
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvPrice;
        ImageButton btnPlus, btnMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartItemName);
            tvQty = itemView.findViewById(R.id.tvCartItemQty);
            tvPrice = itemView.findViewById(R.id.tvCartItemPrice);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}