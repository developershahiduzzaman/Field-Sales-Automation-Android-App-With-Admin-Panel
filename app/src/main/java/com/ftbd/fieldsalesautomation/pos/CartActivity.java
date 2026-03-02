package com.ftbd.fieldsalesautomation.pos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftbd.fieldsalesautomation.Adapter.CartAdapter;
import com.ftbd.fieldsalesautomation.R;
import com.ftbd.fieldsalesautomation.api.RetrofitClient;
import com.ftbd.fieldsalesautomation.models.CartManager;
import com.ftbd.fieldsalesautomation.models.Shop;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private TextView tvTotal;
    private Button btnConfirm;
    private RecyclerView rvCart;
    private CartAdapter adapter;
    private List<Shop> allShops = new ArrayList<>();
    private String selectedShopPhone = "";
    private AutoCompleteTextView shopDropdown;

    // এগুলোকে গ্লোবাল রাখতে হবে যাতে সব মেথড থেকে পাওয়া যায়
    private TextInputEditText etCashReceived;
    private RadioGroup rgPaymentMode;
    private TextInputLayout tilPaidAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // ভিউ আইডি চিনিয়ে দেওয়া
        tvTotal = findViewById(R.id.tvTotalBill);
        btnConfirm = findViewById(R.id.btnConfirmOrder);
        rvCart = findViewById(R.id.rvCart);
        shopDropdown = findViewById(R.id.shopDropdown);
        rgPaymentMode = findViewById(R.id.rgPaymentMode);
        tilPaidAmount = findViewById(R.id.tilPaidAmount);
        etCashReceived = findViewById(R.id.etCashReceived);

        // ১. RecyclerView সেটআপ
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        List<CartItem> items = CartManager.getInstance().getCartItems();
        adapter = new CartAdapter(items);
        rvCart.setAdapter(adapter);

        // ২. টোটাল অ্যামাউন্ট সেট
        tvTotal.setText("৳ " + CartManager.getInstance().getTotalAmount());

        // ৩. শপ ড্রপডাউন লোড করা
        loadShopsFromServer();

        // ৪. শপ সিলেক্ট করার লিসেনার
        shopDropdown.setOnItemClickListener((parent, view, position, id) -> {
            Shop selected = (Shop) parent.getItemAtPosition(position);
            CartManager.getInstance().setShopName(selected.getShopName());
            selectedShopPhone = selected.getPhone();
        });

        // ৫. পেমেন্ট মোড চেঞ্জ লিসেনার
        rgPaymentMode.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbPartialOrDue) {
                tilPaidAmount.setVisibility(View.VISIBLE);
                etCashReceived.setText("");
            } else {
                tilPaidAmount.setVisibility(View.GONE);
                etCashReceived.setText("0");
            }
        });

        // ৬. কনফার্ম বাটন ক্লিক
        btnConfirm.setOnClickListener(v -> {
            if (items.isEmpty()) {
                Toast.makeText(this, "কার্ট খালি!", Toast.LENGTH_SHORT).show();
            } else if (selectedShopPhone.isEmpty()) {
                Toast.makeText(this, "দোকান সিলেক্ট করুন!", Toast.LENGTH_SHORT).show();
            } else {
                double totalAmount = CartManager.getInstance().getTotalAmount();
                double paidAmount;
                double dueAmount;

                if (rgPaymentMode.getCheckedRadioButtonId() == R.id.rbFullPaid) {
                    paidAmount = totalAmount;
                    dueAmount = 0;
                } else {
                    String cashInput = etCashReceived.getText().toString().trim();
                    paidAmount = cashInput.isEmpty() ? 0 : Double.parseDouble(cashInput);
                    dueAmount = totalAmount - paidAmount;
                }

                // এখানে দুইটা ভ্যালু পাঠাচ্ছি
                saveOrderToLaravel(paidAmount, dueAmount);
            }
        });
    }

    private void loadShopsFromServer() {
        RetrofitClient.getApiService().getAllShops().enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allShops = response.body();
                    ArrayAdapter<Shop> shopAdapter = new ArrayAdapter<>(CartActivity.this,
                            android.R.layout.simple_dropdown_item_1line, allShops);
                    shopDropdown.setAdapter(shopAdapter);
                }
            }
            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "দোকান লিস্ট লোড হয়নি!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // মেথডটি আপডেট করা হয়েছে (প্যারামিটারসহ)
    private void saveOrderToLaravel(double paid, double due) {
        android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(this);
        progressDialog.setMessage("অর্ডার সেভ হচ্ছে...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        btnConfirm.setEnabled(false);

        List<CartItem> currentCartItems = new ArrayList<>(CartManager.getInstance().getCartItems());
        double total = CartManager.getInstance().getTotalAmount();
        String shopName = CartManager.getInstance().getShopName();

        List<OrderItemRequest> apiItems = new ArrayList<>();
        for (CartItem item : currentCartItems) {
            apiItems.add(new OrderItemRequest(
                    item.getProduct().getId(),
                    item.getQuantity(),
                    item.getProduct().getPrice()
            ));
        }

        int testUserId = 3;
        int testCustomerId = 1;

        // এখন OrderRequest এ paid এবং due পাঠাচ্ছি
        OrderRequest request = new OrderRequest(testUserId, testCustomerId, shopName, total, paid, due, apiItems);

        RetrofitClient.getApiService().saveOrder(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                btnConfirm.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String serverMsg = response.body().string();
                        if (serverMsg.contains("success")) {
                            Toast.makeText(CartActivity.this, "অর্ডার সফলভাবে সেভ হয়েছে!", Toast.LENGTH_SHORT).show();

                            String memoBody = generateMemoString(shopName, currentCartItems, total, paid, due);
                            Intent intent = new Intent(CartActivity.this, MemoActivity.class);
                            intent.putExtra("memo_text", memoBody);
                            intent.putExtra("phone", selectedShopPhone);
                            startActivity(intent);

                            CartManager.getInstance().clearCart();
                            finish();
                        } else {
                            Toast.makeText(CartActivity.this, "সেভ হয়নি: " + serverMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e("FINAL_CHECK", "Error: " + e.getMessage());
                    }
                } else {
                    Toast.makeText(CartActivity.this, "সার্ভার এরর!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                btnConfirm.setEnabled(true);
                Toast.makeText(CartActivity.this, "নেটওয়ার্ক সমস্যা!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateMemoString(String shopName, List<CartItem> items, double total, double paid, double due) {
        StringBuilder memo = new StringBuilder();
        memo.append("🏪 *দোকান: ").append(shopName).append("*\n");
        memo.append("━━━━━━━━━━━━━━━━━━━━\n");
        for (CartItem item : items) {
            memo.append("🔹 ").append(item.getProduct().getName())
                    .append("\n   ").append(item.getQuantity()).append(" x ")
                    .append(item.getProduct().getPrice()).append(" = ৳")
                    .append(item.getProduct().getPrice() * item.getQuantity()).append("\n");
        }
        memo.append("━━━━━━━━━━━━━━━━━━━━\n");
        memo.append("*মোট বিল: ৳").append(total).append("*\n");
        memo.append("*জমা: ৳").append(paid).append("*\n");
        memo.append("*বাকি: ৳").append(due).append("*\n");
        return memo.toString();
    }

    public void updateTotalUI() {
        double total = 0;
        for (CartItem item : CartManager.getInstance().getCartItems()) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        tvTotal.setText("৳ " + total);
        CartManager.getInstance().setTotalAmount(total);
    }
}