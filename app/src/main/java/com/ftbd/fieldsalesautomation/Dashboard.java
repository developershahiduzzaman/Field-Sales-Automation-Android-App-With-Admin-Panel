package com.ftbd.fieldsalesautomation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.ftbd.fieldsalesautomation.api.RetrofitClient;
import com.ftbd.fieldsalesautomation.models.CartManager;
import com.ftbd.fieldsalesautomation.models.SalesResponse;
import com.ftbd.fieldsalesautomation.pos.CartActivity;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {
    MaterialButton btnSell, btnShopManagement, btnOrderHistory, btnSalesReport, btnDueCollection;
    private LinearLayout btnHome, btnCart, btnProfile;

    // নতুন TextView গুলো যোগ করা হয়েছে
    private TextView tvTodaySales, tvTodayTarget, tvTodayPaid, tvTotalDueAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        // View Initialize
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnGoToCart);
        btnProfile = findViewById(R.id.btnProfile);

        tvTodaySales = findViewById(R.id.tvTodaySales);
        tvTodayTarget = findViewById(R.id.tvTodayTarget);
        tvTodayPaid = findViewById(R.id.tvTodayPaid); // নতুন আইডি
        tvTotalDueAmount = findViewById(R.id.tvTotalDueAmount); // নতুন আইডি

        btnOrderHistory = findViewById(R.id.btnOrderHistory);


        loadDashboardData();
        setupBottomNavigation();

        btnSell = findViewById(R.id.btnSell);
        btnShopManagement = findViewById(R.id.btnShopManagement);


        // Click Listeners
        btnSell.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard.this, ProductActivity.class));
        });

        btnShopManagement.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard.this, ShopActivity.class));
        });

        btnOrderHistory.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard.this, OrderHistoryActivity.class));
        });

    }

    private void loadDashboardData() {
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userIdStr = pref.getString("user_id_str", "0");
        int userId = Integer.parseInt(userIdStr);

        Log.d("DASHBOARD_DEBUG", "UserID sending to API: " + userId);

        RetrofitClient.getApiService().getUserSales(userId).enqueue(new Callback<SalesResponse>() {
            @Override
            public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SalesResponse data = response.body();

                    // ডাটাগুলো ভেরিয়েবলে নেওয়া
                    double sales = data.getTodaySales();
                    double target = data.getTodayTarget();
                    double paidToday = data.getTodayPaid(); // API তে এটি থাকতে হবে
                    double totalDue = data.getTotalDue();   // API তে এটি থাকতে হবে

                    // UI তে ডাটা সেট করা
                    tvTodaySales.setText("৳ " + String.format("%.2f", sales));
                    tvTodayTarget.setText("৳ " + String.format("%,.0f", target));
                    tvTodayPaid.setText("৳ " + String.format("%.2f", paidToday));
                    tvTotalDueAmount.setText("৳ " + String.format("%.2f", totalDue));

                    Log.d("DASHBOARD_DEBUG", "Data Updated Successfully");
                }
            }

            @Override
            public void onFailure(Call<SalesResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(Dashboard.this, "সার্ভার কানেকশন এরর!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData(); // প্রতিবার ড্যাশবোর্ডে ফিরলে ডাটা আপডেট হবে
    }

    private void setupBottomNavigation() {
        btnHome.setOnClickListener(v -> {
            Toast.makeText(this, "আপনি হোমেই আছেন", Toast.LENGTH_SHORT).show();
        });

        btnCart.setOnClickListener(v -> {
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(this, "কার্ট খালি! পণ্য যোগ করুন।", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Dashboard.this, CartActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}