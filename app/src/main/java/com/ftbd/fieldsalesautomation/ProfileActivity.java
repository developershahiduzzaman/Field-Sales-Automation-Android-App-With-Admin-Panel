package com.ftbd.fieldsalesautomation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.ftbd.fieldsalesautomation.api.RetrofitClient;
import com.ftbd.fieldsalesautomation.models.CartManager;
import com.ftbd.fieldsalesautomation.models.UserProfile;
import com.ftbd.fieldsalesautomation.models.UserProfileResponse;
import com.ftbd.fieldsalesautomation.pos.CartActivity;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private TextView tvName, tvEmail, tvPhone, tvIdNumber;
    private Button btnEditProfile, btnLogout;
    private LinearLayout btnHome, btnGoToCart, btnProfile;

    // এপিআই থেকে পাওয়া ইমেজ ইউআরএল রাখার জন্য ভ্যারিয়েবল
    private String currentImageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // ভিউ ইনিশিয়ালাইজেশন
        profileImageView = findViewById(R.id.profileImageView);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvIdNumber = findViewById(R.id.tvIdNumber);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        // নতুন যোগ করা ভিউ (Logout এবং Nav Menu)
        btnLogout = findViewById(R.id.btnLogout);
        btnHome = findViewById(R.id.btnHome);
        btnGoToCart = findViewById(R.id.btnGoToCart);
        btnProfile = findViewById(R.id.btnProfile);

        // প্রোফাইল ডাটা লোড করা
        loadUserProfile();

        // ১. এডিট বাটনে ক্লিক করলে এডিট স্ক্রিনে যাবে
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfile.class);
            intent.putExtra("user_email", tvEmail.getText().toString());
            intent.putExtra("user_name", tvName.getText().toString());
            intent.putExtra("user_phone", tvPhone.getText().toString());
            intent.putExtra("user_id_number", tvIdNumber.getText().toString().replace("ID: ", ""));
            intent.putExtra("user_image", currentImageUrl); // ইমেজ ইউআরএল পাঠানো
            startActivity(intent);
        });

        // ২. লগআউট লজিক
        btnLogout.setOnClickListener(v -> {
            logoutUser();
        });

        // ৩. বটম নেভিগেশন লজিক
        btnHome.setOnClickListener(v -> {
            // ড্যাশবোর্ড বা মেইন অ্যাক্টিভিটিতে নিয়ে যাবে
            startActivity(new Intent(ProfileActivity.this, Dashboard.class));
            finish();
        });

        btnGoToCart.setOnClickListener(v -> {
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(this, "কার্ট খালি! পণ্য যোগ করুন।", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(v -> {
            // আমরা অলরেডি প্রোফাইল স্ক্রিনেই আছি
            Toast.makeText(this, "You are already in Profile", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserProfile() {
        String token = "Bearer " + getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("token", "");
        Log.d("isLoggedIn", token);

        RetrofitClient.getApiService().getUserProfile(token).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile user = response.body().getUser();

                    tvName.setText(user.getName());
                    tvEmail.setText(user.getEmail());

                    // আইডি ও ফোন নাম্বার সেট করা
                    String idNum = (user.getIdNumber() == null || user.getIdNumber().isEmpty()) ? "Not Assigned" : user.getIdNumber();
                    String phoneNum = (user.getPhone() == null || user.getPhone().isEmpty()) ? "Not Added" : user.getPhone();

                    tvIdNumber.setText("ID: " + idNum);
                    tvPhone.setText(phoneNum);

                    // ইমেজ ইউআরএল সেভ করে রাখা এডিট স্ক্রিনে পাঠানোর জন্য
                    currentImageUrl = user.getPicture();

                    Glide.with(ProfileActivity.this)
                            .load(currentImageUrl)
                            .placeholder(R.drawable.default_avatar)
                            .error(R.drawable.default_avatar)
                            .into(profileImageView);
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser() {
        // ১. SharedPreferences থেকে টোকেন এবং সব ডাটা মুছে ফেলা
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        // ২. ইউজারকে লগইন স্ক্রিনে পাঠিয়ে দেওয়া
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        // ব্যাক স্ট্যাক ক্লিয়ার করা যাতে ব্যাক বাটনে চাপলে আবার প্রোফাইলে না আসে
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

    private void handleApiError(Response<?> response) {
        try {
            Log.e("API_ERROR", "Code: " + response.code());
            if (response.code() == 401) {
                // টোকেন এক্সপায়ার হয়ে গেলে অটো লগআউট
                logoutUser();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}