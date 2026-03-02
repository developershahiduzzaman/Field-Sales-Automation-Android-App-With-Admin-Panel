package com.ftbd.fieldsalesautomation.pos;

import static com.ftbd.fieldsalesautomation.R.id.btnHome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftbd.fieldsalesautomation.Dashboard;
import com.ftbd.fieldsalesautomation.OrderHistoryActivity;
import com.ftbd.fieldsalesautomation.ProfileActivity;
import com.ftbd.fieldsalesautomation.R;
import com.ftbd.fieldsalesautomation.models.CartManager;

import java.net.URLEncoder;

public class MemoActivity extends AppCompatActivity {
    private String memoContent = "";
    private String phoneNumber = "";
    private LinearLayout btnHome, btnOrder, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        TextView tvMemoDetails = findViewById(R.id.tvMemoDetails);
        Button btnShareWhatsApp = findViewById(R.id.btnShareWhatsApp);

        // Intent থেকে ডাটা রিসিভ
        memoContent = getIntent().getStringExtra("memo_text");
        phoneNumber = getIntent().getStringExtra("phone");
        btnHome = findViewById(R.id.btnHomeMemo);
        btnOrder = findViewById(R.id.btnGoToHistory);
        btnProfile = findViewById(R.id.btnProfileMemo);



        tvMemoDetails.setText(memoContent);

        btnShareWhatsApp.setOnClickListener(v -> {
            shareToWhatsApp(phoneNumber, memoContent);
        });

        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(MemoActivity.this, Dashboard.class));
        });

        btnOrder.setOnClickListener(v -> {

                        Intent intent = new Intent(MemoActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);

                });

        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(MemoActivity.this, ProfileActivity.class));

        });
    }

    private void shareToWhatsApp(String phone, String text) {
        try {
            String cleanPhone = phone.replace("+", "").replace(" ", "");
            if (!cleanPhone.startsWith("88")) cleanPhone = "88" + cleanPhone;

            String url = "https://api.whatsapp.org/send?phone=" + cleanPhone + "&text=" + URLEncoder.encode(text, "UTF-8");
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            i.setPackage("com.whatsapp");
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp পাওয়া যায়নি!", Toast.LENGTH_SHORT).show();
        }
    }
}