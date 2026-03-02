package com.ftbd.fieldsalesautomation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.ftbd.fieldsalesautomation.api.RetrofitClient;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {

    private EditText etName, etPhone, etIdNumber;
    private CircleImageView editProfileImage;
    private Button btnSave;
    private Uri selectedImageUri = null;
    private String token;
    private static final int PICK_IMAGE_REQUEST = 101;
    private static final int PERMISSION_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // ১. ভিউ ইনিশিয়ালাইজেশন
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etIdNumber = findViewById(R.id.etIdNumber);
        editProfileImage = findViewById(R.id.editProfileImage);
        btnSave = findViewById(R.id.btnSave);

        // ২. SharedPreferences থেকে টোকেন নেওয়া
        token = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("token", "");

        // ৩. আগের ডাটা সেট করা (Pre-fill from Intent)
        setupInitialData();

        // ৪. ছবি পরিবর্তনের জন্য ক্লিক লিসেনার (পারমিশন চেকসহ)
        editProfileImage.setOnClickListener(v -> checkPermissionAndOpenGallery());

        // ৫. সেভ বাটন লজিক
        btnSave.setOnClickListener(v -> updateProfile());
    }

    private void setupInitialData() {
        etName.setText(getIntent().getStringExtra("user_name"));
        etPhone.setText(getIntent().getStringExtra("user_phone"));
        etIdNumber.setText(getIntent().getStringExtra("user_id_number"));

        String imageUrl = getIntent().getStringExtra("user_image");
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).placeholder(R.drawable.default_avatar).into(editProfileImage);
        }
    }

    private void checkPermissionAndOpenGallery() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            editProfileImage.setImageURI(selectedImageUri);
        }
    }

    private void updateProfile() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String idNumber = etIdNumber.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name required");
            return;
        }

        // RequestBody তৈরি
        RequestBody rName = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody rPhone = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody rId = RequestBody.create(MediaType.parse("text/plain"), idNumber);

        MultipartBody.Part imagePart = null;
        if (selectedImageUri != null) {
            String path = getRealPathFromURI(selectedImageUri);
            if (path != null) {
                File file = new File(path);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            }
        }

        // এপিআই কল
        RetrofitClient.getApiService().updateProfile("Bearer " + token, rName, rPhone, rId, imagePart)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EditProfile.this, "Success!", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(EditProfile.this, "Update Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(EditProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(index);
            }
        } catch (Exception e) {
            Log.e("PathError", e.getMessage());
        }
        return path;
    }
}