package com.ftbd.fieldsalesautomation;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftbd.fieldsalesautomation.Adapter.ShopAdapter;
import com.ftbd.fieldsalesautomation.api.ApiService;
import com.ftbd.fieldsalesautomation.api.RetrofitClient;
import com.ftbd.fieldsalesautomation.models.Shop;
import com.ftbd.fieldsalesautomation.pos.CartActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShopAdapter adapter;
    private FloatingActionButton btnAddShop;
    LinearLayout btnHome, btnCart, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        recyclerView = findViewById(R.id.recyclerView);
        btnAddShop = findViewById(R.id.btnAddShop);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnGoToCart);
        btnProfile = findViewById(R.id.btnProfile);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShopAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);


        fetchShops();

        btnAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopActivity.this, ShopRegister.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(ShopActivity.this, Dashboard.class));
        });

        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(ShopActivity.this, CartActivity.class));
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ShopActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void fetchShops() {

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Shop>> call = apiService.getAllShops();

        call.enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Shop> shopList = response.body();


                    adapter = new ShopAdapter(shopList, ShopActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {

                    Toast.makeText(ShopActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(ShopActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        fetchShops();
    }
}