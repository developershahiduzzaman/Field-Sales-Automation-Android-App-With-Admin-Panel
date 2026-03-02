package com.ftbd.fieldsalesautomation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftbd.fieldsalesautomation.Adapter.MemoAdapter;
import com.ftbd.fieldsalesautomation.api.ApiService;
import com.ftbd.fieldsalesautomation.api.RetrofitClient;
import com.ftbd.fieldsalesautomation.models.ViewOrderItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {
    // tvPaid এবং tvDue যোগ করা হয়েছে
    private TextView tvShop, tvId, tvDate, tvTotal, tvPaid, tvDue;
    private RecyclerView rvItems;
    private Button btnPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // View Initialize
        tvShop = findViewById(R.id.tvMemoShopName);
        tvId = findViewById(R.id.tvMemoOrderId);
        tvDate = findViewById(R.id.tvMemoDate);
        tvTotal = findViewById(R.id.tvMemoTotal);

        // নতুন আইডিগুলো Initialize করা হলো
        tvPaid = findViewById(R.id.tvMemoPaid);
        tvDue = findViewById(R.id.tvMemoDue);

        rvItems = findViewById(R.id.rvOrderItems);
        btnPrint = findViewById(R.id.btnPrintMemo);

        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // ১. ডাটা রিসিভ করা (Intent থেকে)
        String shopName = getIntent().getStringExtra("SHOP_NAME");
        String orderId = getIntent().getStringExtra("ORDER_ID");
        String amount = getIntent().getStringExtra("TOTAL_AMOUNT");
        String date = getIntent().getStringExtra("DATE");

        // পেইড এবং ডিউ রিসিভ করা
        String paid = getIntent().getStringExtra("PAID_AMOUNT");
        String due = getIntent().getStringExtra("DUE_AMOUNT");

        // ২. ডাটা সেট করা
        tvShop.setText(shopName);
        tvId.setText("Order ID: #" + orderId);
        tvDate.setText("Date: " + date);
        tvTotal.setText("৳ " + amount);

        // পেইড এবং ডিউ সেট করা (নাল চেকসহ)
        tvPaid.setText("৳ " + (paid != null ? paid : "0.00"));
        tvDue.setText("৳ " + (due != null ? due : "0.00"));

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // বাটন হাইড করা যাতে স্ক্রিনশটে বাটন না আসে
                btnPrint.setVisibility(View.GONE);
                v.postDelayed(() -> {
                    View memoView = findViewById(R.id.memoLayout);
                    Bitmap bitmap = getBitmapFromView(memoView);
                    btnPrint.setVisibility(View.VISIBLE);
                    if (bitmap != null) {
                        shareBitmap(bitmap);
                    }
                }, 100);
            }
        });

        fetchOrderItems(orderId);
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void shareBitmap(Bitmap bitmap) {
        try {
            File cachePath = new File(getExternalCacheDir(), "images");
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath + "/memo.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            File imagePath = new File(getExternalCacheDir(), "images");
            File newFile = new File(imagePath, "memo.png");
            Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", newFile);

            if (contentUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                startActivity(Intent.createChooser(shareIntent, "মেমো শেয়ার করুন"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchOrderItems(String orderId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getOrderDetails(orderId).enqueue(new Callback<List<ViewOrderItem>>() {
            @Override
            public void onResponse(Call<List<ViewOrderItem>> call, Response<List<ViewOrderItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MemoAdapter adapter = new MemoAdapter(response.body());
                    rvItems.setAdapter(adapter);
                } else {
                    Toast.makeText(OrderDetailsActivity.this, "কোনো আইটেম পাওয়া যায়নি", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ViewOrderItem>> call, Throwable t) {
                Toast.makeText(OrderDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}