package com.example.smartseller.ui.home.MyProductFragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartseller.R;
import com.example.smartseller.data.model.ColorAttribute;
import com.example.smartseller.data.model.Products;
import com.example.smartseller.data.model.SizeAttribute;
import com.example.smartseller.data.network.JsonResponse;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentMyProductDetailsBinding;
import com.example.smartseller.util.session.Session;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;


public class MyProductDetails extends Fragment {

    private FragmentMyProductDetailsBinding binding;
    private Session session;
    private String size="";
    private String color="";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       binding=FragmentMyProductDetailsBinding.inflate(getLayoutInflater());
       View view=binding.getRoot();
       session=new Session(getContext());
       getPassedValues();



       binding.ivBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MyProduct()).commit();
           }
       });



       binding.btnEdit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               MyProductDetailsEdit mpde=new MyProductDetailsEdit();
               Bundle args=new Bundle();
               Products passedProduct=getArguments().getParcelable("productObj");
               Products products=new Products(
                 passedProduct.getProductId(),passedProduct.getProductName(),passedProduct.getDesc(),passedProduct.getPrice(),passedProduct.getCategory(),passedProduct.getBrand(),passedProduct.getSku(),passedProduct.getType(),passedProduct.getPicture_path(),passedProduct.getDiscount(),passedProduct.getStock(),passedProduct.getSeller_id()
                       ,color,size
               );
               args.putParcelable("productObj",products);
               mpde.setArguments(args);
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mpde).commit();


           }
       });


       return view;
    }

    void getPassedValues(){

            Bundle b=getArguments();
            Products products=b.getParcelable("productObj");
            Toasty.success(getContext(),products.getProductName()).show();
        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext(),R.style.AlertDialog);
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to delete the product? \n \n Note:It cant be reverted back")
                        .
                        setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteProduct(products.getProductId());
                            }
                        })
                        .create().show();


            }
        });

            binding.tvProductName.setText(products.getProductName());
        getColor(products.getProductId());getSize(products.getProductId());
        //set values
            binding.tvDesc.setText(products.getDesc());
            binding.tvPrice.setText("Rs. "+products.getPrice());
            binding.tvBrand.setText(products.getBrand());
            binding.tvCategory.setText(products.getCategory());
            binding.tvSku.setText(products.getSku());
            binding.tvType.setText(products.getType());
            binding.tvDiscount.setText(String.valueOf(products.getDiscount())+"%");
            binding.tvStock.setText(String.valueOf(products.getStock()));

        try{
            String url=products.getPicture_path();
            Picasso.get()
                    .load(url)
                    .fit()
                    .into(binding.ivProductImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Load",e.getMessage());
                        }
                    });}
        catch (Exception e){
            Log.d("error",e.getMessage());
        }

    }

    private void deleteProduct(int productId) {

        Call<JsonResponse> delProduct=SmartAPI.getApiService().deleteProduct(session.getJWT(),productId);
        delProduct.enqueue(new retrofit2.Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getStatus().equalsIgnoreCase("200 OK"))
                    {
                        Toasty.success(getContext(),response.body().getMessage()).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MyProduct()).commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });

    }

    private void getColor(Integer productId) {
        Call<List<ColorAttribute>> getcolor= SmartAPI.getApiService().getColors(session.getJWT(),productId);
        getcolor.enqueue(new retrofit2.Callback<List<ColorAttribute>>() {
            @Override
            public void onResponse(Call<List<ColorAttribute>> call, Response<List<ColorAttribute>> response) {

                if (response.isSuccessful())
                {


                    for (ColorAttribute c:response.body()){
                        String col=c.getColor();
                        color=color+col+",";
                    }
                    if (color.length()==0)
                        binding.tvColor.setText("No color option for this product");
                        else
                    binding.tvColor.setText(color);

                }
            }

            @Override
            public void onFailure(Call<List<ColorAttribute>> call, Throwable t) {

            }
        });

    }

    private void getSize(Integer productId) {
        Call<List<SizeAttribute>> getSize=SmartAPI.getApiService().getSizes(session.getJWT(),productId);
        getSize.enqueue(new retrofit2.Callback<List<SizeAttribute>>() {
            @Override
            public void onResponse(Call<List<SizeAttribute>> call, Response<List<SizeAttribute>> response) {
                if (response.isSuccessful()){

                    for (SizeAttribute s:response.body()){
                        String siz=String.valueOf(s.getSize());

                        size=size+siz+",";
                    }
                    if (size.length()==0)
                        binding.tvSize.setText("No size option for this product");
                        else
                    binding.tvSize.setText(size);
                }
            }

            @Override
            public void onFailure(Call<List<SizeAttribute>> call, Throwable t) {

            }
        });
    }
}