package com.ftbd.fieldsalesautomation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ftbd.fieldsalesautomation.api.AuthResponse;
import com.ftbd.fieldsalesautomation.api.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    private TextInputLayout emailLayout, passLayout;
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {

            startActivity(new Intent(LoginActivity.this, Dashboard.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_login);



        emailLayout = findViewById(R.id.emailLayout);
        passLayout = findViewById(R.id.passLayout);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);


        startEntranceAnimation();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    emailLayout.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, android.R.anim.fade_in));
                    etEmail.setError("Email required");
                } else if (password.isEmpty()) {
                    etPassword.setError("Password required");
                } else {
                    loginProcess(email, password);
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


    }

    private void startEntranceAnimation() {
        emailLayout.setTranslationY(400);
        passLayout.setTranslationY(400);
        btnLogin.setTranslationY(400);

        emailLayout.setAlpha(0f);
        passLayout.setAlpha(0f);
        btnLogin.setAlpha(0f);

        emailLayout.animate().translationY(0).alpha(1f).setDuration(800).setStartDelay(200).start();
        passLayout.animate().translationY(0).alpha(1f).setDuration(800).setStartDelay(400).start();
        btnLogin.animate().translationY(0).alpha(1f).setDuration(800).setStartDelay(600).start();
    }

    private void loginProcess(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        RetrofitClient.getApiService().login(loginRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    // ১. রেসপন্স থেকে টোকেনটি ধরুন
                    String token = response.body().getToken(); // আপনার AuthResponse মডেলে getToken() থাকতে হবে
                    int userId = response.body().getUserId();

                    Log.d("LOGIN_DEBUG", "Token: " + token);

                    SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    // ২. টোকেনটি "token" কি (Key) দিয়ে সেভ করুন
                    editor.putString("token", token);
                    editor.putString("user_id_str", String.valueOf(userId));
                    editor.putBoolean("isLoggedIn", true);

                    if (editor.commit()) {
                        startActivity(new Intent(LoginActivity.this, Dashboard.class));
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Login Credentials!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}