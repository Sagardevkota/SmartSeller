package com.example.smartseller.ui.home.AddProductFragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartseller.R;
import com.example.smartseller.data.model.ColorAttribute;
import com.example.smartseller.data.model.Products;
import com.example.smartseller.data.model.SizeAttribute;
import com.example.smartseller.data.network.JsonResponse;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentAddProductFinalPageBinding;
import com.example.smartseller.util.session.Session;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class addProductFinalPage extends Fragment {
    private FragmentAddProductFinalPageBinding binding;
    private Session session;


    public addProductFinalPage() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddProductFinalPageBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        session=new Session(getActivity());
        binding.tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        return view;
    }

    private void getPassedValues(String message) {
        //getting arguments from previous fragment
        String productName=getArguments().getString("product_name");
        String productDesc=getArguments().getString("product_desc");
        String price=getArguments().getString("product_price");
        String category=getArguments().getString("category");
        String brand=getArguments().getString("brand");
        String sku=getArguments().getString("sku");
        String type=getArguments().getString("type");
        String picture_path=message;
        String Stringstock=getArguments().getString("stock");
        Integer stock=Integer.valueOf(Stringstock);

        String discountStr=getArguments().getString("discount");
        Integer discount=Integer.valueOf(discountStr);
        Integer seller_id=session.getUserId();


        Products products=new Products(productName,productDesc,price,category,brand,sku,type,picture_path,discount,stock,seller_id);
       postProduct(products);

    }

    private void postProduct(Products products) {
        Call<JsonResponse> responseCall=SmartAPI.getApiService().addProduct(session.getJWT(),products);
        responseCall.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equalsIgnoreCase("200 OK")){
                        Integer productId=Integer.valueOf(response.body().getMessage());
                        addAttributes(productId);
                    }
                    else
                        Toasty.error(getContext(),response.body().getMessage()).show();
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });
    }

    private void addAttributes(Integer productId) {

        String color=binding.etColor.getText().toString().trim();

                ColorAttribute colorAttribute=new ColorAttribute(productId,color);
                Call<JsonResponse> addColor=SmartAPI.getApiService().addColor(session.getJWT(),colorAttribute);
                addColor.enqueue(new Callback<JsonResponse>() {
                    @Override
                    public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                        

                    }

                    @Override
                    public void onFailure(Call<JsonResponse> call, Throwable t) {

                    }
                });

                String sizeStr=binding.etSize.getText().toString().trim();
                SizeAttribute sizeAttribute=new SizeAttribute(productId,sizeStr);
                Call<JsonResponse> sizeCall=SmartAPI.getApiService().addSize(session.getJWT(),sizeAttribute);
                sizeCall.enqueue(new Callback<JsonResponse>() {
                    @Override
                    public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {


                    }

                    @Override
                    public void onFailure(Call<JsonResponse> call, Throwable t) {

                    }
                });



            showOkDialog();


    }

    private void showOkDialog() {
        MaterialAlertDialogBuilder alertDialogBuilder=
                new MaterialAlertDialogBuilder(getContext(),R.style.AlertDialog)
                .setMessage("Product Added successfully")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new addProduct()).commit();
                    }
                });

        alertDialogBuilder.create().show();


    }

    private void uploadImage(){

        showProgressDialog("Adding Product");
        String imagePath=getArguments().getString("imagePath");
        File file = new File(imagePath);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);


        Call<JsonResponse> uploadCall=SmartAPI.getApiService().uploadImage(session.getJWT(),body);
        uploadCall.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getStatus().equalsIgnoreCase("200 OK"))
                    getPassedValues(response.body().getMessage());
                    else Toasty.error(getContext(),response.body().getMessage()).show();

                }

                hideProgressDialog();


            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Toasty.error(getContext(),t.getMessage()).show();

            }
        });



    }

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext(), R.style.AlertDialog);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }




}