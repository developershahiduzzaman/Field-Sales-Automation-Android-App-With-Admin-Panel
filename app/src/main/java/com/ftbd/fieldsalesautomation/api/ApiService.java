package com.ftbd.fieldsalesautomation.api;

import com.ftbd.fieldsalesautomation.LoginRequest;
import com.ftbd.fieldsalesautomation.RegisterRequest;
import com.ftbd.fieldsalesautomation.models.DueResponse;
import com.ftbd.fieldsalesautomation.models.OrderModel;
import com.ftbd.fieldsalesautomation.models.Product;
import com.ftbd.fieldsalesautomation.models.SalesResponse;
import com.ftbd.fieldsalesautomation.models.Shop;
import com.ftbd.fieldsalesautomation.models.UserProfileResponse;
import com.ftbd.fieldsalesautomation.models.ViewOrderItem;
import com.ftbd.fieldsalesautomation.pos.OrderItem;
import com.ftbd.fieldsalesautomation.pos.OrderRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @Headers("Accept: application/json")
    @POST("login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);
    @Headers("Accept: application/json")
    @POST("register")
    Call<AuthResponse> register(@Body RegisterRequest registerRequest);
    @GET("products")
    Call<List<Product>> getProducts();

    @GET("shops")
    Call<List<Shop>> getAllShops();

    @POST("shop/register")
    Call<Shop> registerShop(@Body Shop shop);

    @Headers({"Accept: application/json"})
    @POST("orders")
    Call<ResponseBody> saveOrder(@Body OrderRequest request);

    @GET("user-daily-sales")
    Call<SalesResponse> getUserSales(@Query("user_id") int userId);


    @GET("get-order-history")
    Call<List<OrderModel>> getOrderHistory(@Query("user_id") String userId);

    @GET("order-details/{id}")
    Call<List<ViewOrderItem>> getOrderDetails(@Path("id") String orderId);



    @GET("shop/due/{id}")
    Call<DueResponse> getShopDue(@Path("id") int shopId);

    // ২. কালেকশন ডাটা পাঠানোর জন্য
    @FormUrlEncoded
    @POST("collection/store")
    Call<ResponseBody> submitCollection(
            @Field("shop_id") String shopId,
            @Field("user_id") String userId,
            @Field("amount") double amount
    );


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("user/profile")
    Call<UserProfileResponse> getUserProfile(@Header("Authorization") String token);

    @Multipart
    @POST("user/update-profile") // আপনার লারাভেল রাউট অনুযায়ী
    Call<ResponseBody> updateProfile(
            @Header("Authorization") String token,
            @Part("name") RequestBody name,
            @Part("phone") RequestBody phone,
            @Part("id_number") RequestBody id_number,
            @Part MultipartBody.Part image
    );
}