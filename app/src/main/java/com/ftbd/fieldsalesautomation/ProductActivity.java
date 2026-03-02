package com.ftbd.fieldsalesautomation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftbd.fieldsalesautomation.Adapter.ProductAdapter;
import com.ftbd.fieldsalesautomation.api.RetrofitClient;
import com.ftbd.fieldsalesautomation.models.Product;
import com.ftbd.fieldsalesautomation.pos.CartActivity;
import com.ftbd.fieldsalesautomation.models.CartManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private ProgressBar progressBar;
    private EditText etSearch; // সার্চ বক্সের জন্য

    // Bottom Navigation Buttons
    private LinearLayout btnHome, btnCart, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // ১. ভিউ ইনিশিয়ালাইজেশন
        initViews();

        // ২. রিসাইকেলার ভিউ সেটআপ
        setupRecyclerView();

        // ৩. সার্চ লজিক সেটআপ
        setupSearch();

        // ৪. বটম নেভিগেশন ক্লিক লজিক
        setupBottomNavigation();

        // ৫. সার্ভার থেকে প্রোডাক্ট লোড করা
        loadProductsFromServer();

        String shopName = getIntent().getStringExtra("SHOP_NAME");
        if (shopName != null) {
            CartManager.getInstance().setShopName(shopName);
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rvProducts);
        progressBar = findViewById(R.id.progressBar);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnGoToCart);
        btnProfile = findViewById(R.id.btnProfile);
        etSearch = findViewById(R.id.etSearch); // XML এর ID অনুযায়ী
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);
    }

    private void setupSearch() {
        // সার্চ বক্সে টাইপ করার সাথে সাথে ফিল্টার হবে
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupBottomNavigation() {
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(ProductActivity.this, Dashboard.class));
        });

        btnCart.setOnClickListener(v -> {
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(this, "কার্ট খালি! পণ্য যোগ করুন।", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ProductActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProductActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void loadProductsFromServer() {
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getApiService().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    // অ্যাডাপ্টারের কাস্টম মেথড ব্যবহার করে ডাটা আপডেট করা
                    adapter.updateList(response.body());
                    Log.d("API_SUCCESS", "Items: " + response.body().size());
                } else {
                    Toast.makeText(ProductActivity.this, "সার্ভার এরর: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("API_FAIL", "Error: " + t.getMessage());
                Toast.makeText(ProductActivity.this, "ইন্টারনেট কানেকশন চেক করুন", Toast.LENGTH_SHORT).show();
            }
        });
    }
}