package com.ftbd.fieldsalesautomation;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftbd.fieldsalesautomation.Adapter.OrderHistoryAdapter;
import com.ftbd.fieldsalesautomation.api.ApiService;
import com.ftbd.fieldsalesautomation.api.RetrofitClient;
import com.ftbd.fieldsalesautomation.models.OrderModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<OrderModel> orderList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView tvNoData;
    private ImageButton btnBack;

    LinearLayout btnHome, btnSell, btnProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // View Initialize
        recyclerView = findViewById(R.id.rvOrderHistory);
        progressBar = findViewById(R.id.progressBar);
        tvNoData = findViewById(R.id.tvNoData);
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnSell = findViewById(R.id.btnSell);
        btnProfile = findViewById(R.id.btnProfile);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderHistoryActivity.this, Dashboard.class);
                startActivity(intent);

            }
        });
        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderHistoryActivity.this, ProductActivity.class);
                startActivity(intent);

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderHistoryActivity.this, ProfileActivity.class);
                startActivity(intent);

            }
        });


        recyclerView.setAdapter(adapter);
        // ডাটা লোড করা
        fetchOrderHistory();
    }

    private void fetchOrderHistory() {
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUserIdStr = sharedPreferences.getString("user_id_str", null);

        if (savedUserIdStr == null) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "ইউজার লগইন করা নেই!", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiService apiService = RetrofitClient.getApiService();

        Call<List<OrderModel>> call = apiService.getOrderHistory(String.valueOf(savedUserIdStr));

        call.enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    orderList = response.body();

                    if (orderList.isEmpty()) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        adapter = new OrderHistoryAdapter(orderList, new OrderHistoryAdapter.OnPaymentClickListener() {
                            @Override
                            public void onPayClick(OrderModel order) {
                                // টাকা নিন বাটনে ক্লিক করলে এই ফাংশনটি চলবে
                                showCollectionDialog(order);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(OrderHistoryActivity.this, "সার্ভার রেসপন্স করছে না", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OrderHistoryActivity.this, "ইন্টারনেট কানেকশন চেক করুন", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // এই ফাংশনটি পপআপ ডায়ালগ দেখাবে
    private void showCollectionDialog(OrderModel order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("টাকা জমা নিন");

        // দোকান এবং বাকির পরিমাণ দেখানো
        builder.setMessage("দোকান: " + order.getShopName() + "\nবাকি আছে: ৳" + order.getDueAmount());

        // টাকা ইনপুট নেওয়ার জন্য এডিট টেক্সট
        final EditText input = new EditText(this);
        input.setHint("টাকার পরিমাণ লিখুন");
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // বক্সের চারপাশে একটু জায়গা (Padding) দেওয়া
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 50; // পিক্সেল হিসেবে মার্জিন
        params.rightMargin = 50;
        input.setLayoutParams(params);
        container.addView(input);
        builder.setView(container);

        builder.setPositiveButton("জমা দিন", (dialog, which) -> {
            String amount = input.getText().toString();
            if (!amount.isEmpty()) {
                // সার্ভারে টাকা পাঠানোর জন্য আপনার এপিআই ফাংশন কল করুন
                submitDueCollection(order.getOrderId(), Double.parseDouble(amount));
            } else {
                Toast.makeText(this, "টাকার পরিমাণ লিখুন", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("বাতিল", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // সার্ভারে ডাটা পাঠানোর ফাংশন
    // মেথড সিগনেচার পরিবর্তন করে String shopId নিন
    private void submitDueCollection(String shopId, double amount) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id_str", "1");

        ApiService apiService = RetrofitClient.getApiService();

        // লারাভেল কন্ট্রোলার shop_id, user_id এবং amount এই ৩টি জিনিস চায়
        apiService.submitCollection(shopId, userId, amount).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(OrderHistoryActivity.this, "পেমেন্ট সফলভাবে জমা হয়েছে!", Toast.LENGTH_LONG).show();
                    fetchOrderHistory(); // ডাটা রিফ্রেশ হবে
                } else {
                    // সার্ভার কি এরর দিচ্ছে তা দেখতে চাইলে Logcat চেক করুন
                    Toast.makeText(OrderHistoryActivity.this, "সার্ভার এরর! (Code: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(OrderHistoryActivity.this, "কানেকশন এরর: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}