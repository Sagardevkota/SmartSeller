package com.example.smartseller.data.network;


import com.example.smartseller.data.model.ColorAttribute;
import com.example.smartseller.data.model.Conversation;
import com.example.smartseller.data.model.ConversationResponse;
import com.example.smartseller.data.model.MessageResponse;
import com.example.smartseller.data.model.OrderResponse;
import com.example.smartseller.data.model.Products;
import com.example.smartseller.data.model.SizeAttribute;
import com.example.smartseller.data.model.User;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;

import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;


public class SmartAPI {


    public static apiService apiService=null;
//    public static String base_url="http://52.171.61.18:8080/";
    public static String base_url="http://10.0.2.2:8081/";

    public static apiService getApiService(){
        if (apiService==null){
            HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            OkHttpClient okHttpClient=new OkHttpClient()
                    .newBuilder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            apiService=retrofit.create(apiService.class);
        }
        return apiService;
    }

   public interface apiService{


        //user services
       @POST(value = "/api/login")
       Call<JwtResponse> login(@Body User user);

       @GET(value = "/api/user/{userName}")
       Call<User> getUserDetails(@Header("Authorization")String jwt,@Path("userName")String userName);

       @GET(value = "/api/user/id/{userName}")
       Call<JsonResponse> getUserId(@Header("Authorization") String jwt, @Path("userName") String userName);

       @PUT(value = "/api/user/{userId}/{newUserName}")
       Call<JsonResponse> updateEmail(@Header("Authorization") String jwt, @Path("userId")Integer userId, @Path("newUserName")String newUserName);

       @PUT(value = "/api/user/{userId}/phone/{newPhone}")
       Call<JsonResponse> updatePhone(@Header("Authorization") String jwt,@Path("userId")Integer userId,@Path("newPhone")String newPhone);

       @PUT(value = "/api/user/{userId}/delivery/{newDelivery}")
       Call<JsonResponse> updateDelivery(@Header("Authorization") String jwt,@Path("userId")Integer userId,@Path("newDelivery")String newDelivery);



       //product services
       @POST(value = "/api/product")
       Call<JsonResponse> addProduct(@Header("Authorization") String jwt,@Body Products products);

       @GET(value = "/api/product/sellerId/{sellerId}")
       Call<List<Products>> getProducts(@Header("Authorization")String jwt,@Path("sellerId") Integer sellerId);

       @PUT(value = "/api/product")
       Call<JsonResponse> updateProduct(@Header("Authorization")String jwt,@Body Products products);

       @HTTP(method = "DELETE", path = "/api/product/{productId}")
       Call<JsonResponse> deleteProduct(@Header("Authorization")String jwt,@Path("productId")Integer productId);




       //product attributes
       @POST(value = "/api/color")
       Call<JsonResponse> addColor(@Header("Authorization") String jwt,@Body ColorAttribute colorAttribute);
       @POST(value = "/api/size")
       Call<JsonResponse> addSize(@Header("Authorization") String jwt,@Body SizeAttribute sizeAttribute);

       @GET(value = "/api/color/{productId}")
      Call<List<ColorAttribute> > getColors(@Header("Authorization")String jwt,@Path("productId") Integer productId);

       @GET(value = "/api/size/{productId}")
      Call<List<SizeAttribute>>  getSizes(@Header("Authorization")String jwt,@Path("productId") Integer productId);



       @Multipart
       @POST("/api/image/")
       Call<JsonResponse> uploadImage(@Header("Authorization")String jwt,@Part MultipartBody.Part image);


       //order services
       @GET(value = "/api/order/seller/id/{sellerId}/status/{status}")
       Call<List<OrderResponse>> getOrders(@Header("Authorization")String jwt,@Path("sellerId")Integer sellerId,@Path("status") String status);

       @GET(value = "/api/order/seller/count/{sellerId}/{status}")
        Call<JsonResponse> getOrderCount(@Header("Authorization")String jwt,@Path("sellerId") Integer sellerId,@Path("status") String status);

       @PUT(value = "/api/order/{orderId}/date/{orderedDate}")
       Call<JsonResponse> changeDeliveredDate(@Header("Authorization")String jwt,@Path("orderId") Integer orderId,@Path("orderedDate") String orderedDate);

       @PUT(value = "/api/order/{orderId}/status/{status}")
       Call<JsonResponse> changeStatus(@Header("Authorization")String jwt,@Path("orderId") Integer orderId,@Path("status") String status);

       //total conversation of seller products
       @GET(value = "/api/conversation/seller/id/{sellerId}")
       Call<List<MessageResponse>> getConversations(@Header("Authorization")String jwt,@Path("sellerId") Integer sellerId);

       //conversation of product
       @GET(value = "/api/conversation/{productId}")
       Call<List<ConversationResponse>> getConversation(@Header("Authorization")String jwt, @Path("productId")Integer productId);

       @POST(value = "/api/conversation")
       Call<JsonResponse> addConversation(@Header("Authorization")String jwt,@Body Conversation conversation);

   }

}
