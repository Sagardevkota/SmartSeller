package com.example.smartseller.ui.home.MyProductFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.smartseller.R;
import com.example.smartseller.data.model.Products;
import com.example.smartseller.data.network.JsonResponse;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentMyProductDetailsBinding;
import com.example.smartseller.ui.home.HomeActivity;
import com.example.smartseller.util.session.Session;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;


public class MyProductDetails extends Fragment {

    private static final String TAG = "MY_PRODUCT_DETAILS";
    private FragmentMyProductDetailsBinding binding;
    private Session session;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentMyProductDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        session = new Session(getContext());
        getPassedValues();

        binding.btnEdit.setOnClickListener(view12 -> {

            Products passedProduct = getArguments().getParcelable("product");
            MyProductDetailsDirections.ActionProductToItsDetailsEdit actionProductToItsDetailsEdit =
                    MyProductDetailsDirections.actionProductToItsDetailsEdit(passedProduct);
            Navigation.findNavController(view).navigate(actionProductToItsDetailsEdit);

        });


        return view;
    }

    void getPassedValues() {

        Bundle b = getArguments();
        Products products = b.getParcelable("product");
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle(products.getProductName());

        binding.ivDelete.setOnClickListener(view -> {

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialog);
            builder.setTitle("Confirmation")
                    .setMessage("Are you sure you want to delete the product? \n \n Note:It cant be reverted back")
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton("Yes", (dialogInterface, i) -> deleteProduct(products.getProductId()))
                    .create().show();


        });

        binding.tvProductName.setText(products.getProductName());

        //set values
        binding.tvDesc.setText(products.getDesc());
        binding.tvPrice.setText("Rs. " + products.getPrice());
        binding.tvBrand.setText(products.getBrand());
        binding.tvCategory.setText(products.getCategory());
        binding.tvSku.setText(products.getSku());
        binding.tvType.setText(products.getType());
        binding.tvDiscount.setText(products.getDiscount() + "%");
        binding.tvStock.setText(String.valueOf(products.getStock()));

        if (products.getColors().size() != 0)
            products.getColors().forEach(color -> {
                Chip chip = new Chip(getContext());
                chip.setText(color);
                binding.colorChipGroup.addView(chip);
            });

        if (products.getSizes().size() != 0)
            products.getSizes().forEach(size -> {
                Chip chip = new Chip(getContext());
                chip.setText(size);
                binding.sizeChipGroup.addView(chip);
            });

        try {
            String url = SmartAPI.IMG_BASE_URL + products.getPicturePath();
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(binding.ivProductImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Load", e.getMessage());
                        }
                    });
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }

    }

    private void deleteProduct(int productId) {

        Call<JsonResponse> delProduct = SmartAPI.getApiService().deleteProduct(session.getJWT(), productId);
        delProduct.enqueue(new retrofit2.Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("200 OK")) {
                        Toasty.success(getContext(), response.body().getMessage()).show();
                        ((HomeActivity)getActivity()).onSupportNavigateUp();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });

    }


}