package com.example.smartseller.data.network;


import com.example.smartseller.data.model.Conversation;
import com.example.smartseller.data.model.ConversationResponse;
import com.example.smartseller.data.model.MessageResponse;
import com.example.smartseller.data.model.OrderResponse;
import com.example.smartseller.data.model.Products;
import com.example.smartseller.data.model.User;


import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;

import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;


public class SmartAPI {

    public static apiService apiService = null;
    //    public static String base_url="http://52.171.61.18:8080/";
//    public static String base_url = "http://10.0.2.2:8081/";
    public static final String BASE_URL = "http://157.55.181.67:8080";//newest
    public static String base_url = "http://23.101.181.211:8080/";

    public static final String IMG_BASE_URL="https://bese2016smartstore.blob.core.windows.net/bese2016blob/";

    public static apiService getApiService() {
        if (apiService == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient()
                    .newBuilder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();

            apiService = retrofit.create(apiService.class);
        }
        return apiService;
    }

    public interface apiService {


        //user services
        @POST(value = "/api/login")
        Observable<JwtResponse> login(@Body User user);

        @GET(value = "/api/user/details")
        Observable<User> getUserDetails(@Header("Authorization") String jwt);

        @PUT(value = "/api/user/{newUserName}")
        Call<JsonResponse> updateEmail(@Header("Authorization") String jwt, @Path("userId") Integer userId, @Path("newUserName") String newUserName);

        @PUT(value = "/api/user/phone/{newPhone}")
        Call<JsonResponse> updatePhone(@Header("Authorization") String jwt, @Path("userId") Integer userId, @Path("newPhone") String newPhone);

        @PUT(value = "/api/user/delivery/{newDelivery}")
        Call<JsonResponse> updateDelivery(@Header("Authorization") String jwt, @Path("userId") Integer userId, @Path("newDelivery") String newDelivery);


        //product services
        @Multipart
        @POST(value = "/api/seller/products")
        Observable<JsonResponse> addProduct(@Header("Authorization") String jwt, @Part("products") Products products,@Part MultipartBody.Part image);

        @GET(value = "/api/seller/products/")
        Observable<List<Products>> getProducts(@Header("Authorization") String jwt);

        @Multipart
        @PUT(value = "/api/seller/products")
        Observable<JsonResponse> updateProduct(@Header("Authorization") String jwt, @Part("products") Products products,@Part MultipartBody.Part image);


        @PUT(value = "/api/seller/products/no-image")
        Observable<JsonResponse> updateProductWithoutImage(@Header("Authorization") String jwt, @Body Products products);


        @HTTP(method = "DELETE", path = "/api/seller/products/{productId}")
        Call<JsonResponse> deleteProduct(@Header("Authorization") String jwt, @Path("productId") Integer productId);



        //order services
        @GET(value = "/api/seller/orders/status/{status}")
        Observable<List<OrderResponse>> getOrders(@Header("Authorization") String jwt, @Path("status") String status);

        @GET(value = "/api/seller/orders/count/{status}")
        Call<JsonResponse> getOrderCount(@Header("Authorization") String jwt, @Path("sellerId") Integer sellerId, @Path("status") String status);

        @PUT(value = "/api/seller/orders/{orderId}/date/{orderedDate}")
        Call<JsonResponse> changeDeliveredDate(@Header("Authorization") String jwt, @Path("orderId") Integer orderId, @Path("orderedDate") String orderedDate);

        @PUT(value = "/api/seller/orders/{orderId}/status/{status}")
        Call<JsonResponse> changeStatus(@Header("Authorization") String jwt, @Path("orderId") Integer orderId, @Path("status") String status);

        //total conversation of seller products
        @GET(value = "/api/seller/conversations")
        Call<List<MessageResponse>> getConversations(@Header("Authorization") String jwt);

        //conversation of product
        @GET(value = "/api/seller/conversations/{productId}")
        Call<List<ConversationResponse>> getConversation(@Header("Authorization") String jwt, @Path("productId") Integer productId);

        @POST(value = "/api/conversations")
        Call<JsonResponse> addConversation(@Header("Authorization") String jwt, @Body Conversation conversation);

    }

}
