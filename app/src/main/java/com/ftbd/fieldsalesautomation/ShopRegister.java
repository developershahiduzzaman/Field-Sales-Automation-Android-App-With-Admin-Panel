package com.ftbd.fieldsalesautomation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ftbd.fieldsalesautomation.api.ApiService;
import com.ftbd.fieldsalesautomation.api.RetrofitClient;
import com.ftbd.fieldsalesautomation.models.Shop;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopRegister extends AppCompatActivity {

    private TextInputEditText etShopName, etPhone, etAddress, etLocation;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_register);


        etShopName = findViewById(R.id.etShopName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etLocation = findViewById(R.id.etLocation);
        btnRegister = findViewById(R.id.btnSubmitShop);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveShopData();
            }
        });
    }

    private void saveShopData() {
        String name = etShopName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String location = etLocation.getText().toString().trim();


        if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }



       //Model
        Shop shop = new Shop(name, phone, address, location);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.registerShop(shop).enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                btnRegister.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(ShopRegister.this, "দোকান সফলভাবে নিবন্ধিত হয়েছে!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(ShopRegister.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                btnRegister.setEnabled(true);
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(ShopRegister.this, "নেটওয়ার্ক এরর! কানেকশন চেক করুন", Toast.LENGTH_SHORT).show();
            }
        });
    }
}