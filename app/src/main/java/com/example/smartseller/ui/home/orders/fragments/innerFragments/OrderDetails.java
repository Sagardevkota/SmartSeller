package com.example.smartseller.ui.home.orders.fragments.innerFragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.smartseller.R;
import com.example.smartseller.data.model.OrderResponse;
import com.example.smartseller.data.network.JsonResponse;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentOrderDetailsBinding;
import com.example.smartseller.ui.home.orders.fragments.mainFragment.Orders;
import com.example.smartseller.util.session.Session;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;


public class OrderDetails extends Fragment {
    private FragmentOrderDetailsBinding binding;
    private Session session;
    private Integer orderId;
    private String selectedStatus = "";
    private String selectedDeliveredDate = "";


    public OrderDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        session = new Session(getContext());
        getPassedValues();
        binding.tvChangeDeliveredDate.setOnClickListener(view12 -> changeDeliveredDate());
        binding.tvChangeStatus.setOnClickListener(view1 -> changeStatus());


        return view;
    }

    private void changeStatus() {
        ArrayList<String> status = new ArrayList<>();

        status.add("waiting");
        status.add("dispatched");
        status.add("completed");
        status.add("cancelled");

        final ArrayAdapter<String> adp = new ArrayAdapter<>(getContext(),
                android.R.layout.select_dialog_item, status);


        final Spinner sp = new Spinner(getContext());
        sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sp.setAdapter(adp);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedStatus = status.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialog);
        builder.setView(sp);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> updateStatus(orderId, selectedStatus)).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();


    }

    private void updateStatus(Integer orderId, String selectedStatus) {
        Call<JsonResponse> updStatus = SmartAPI.getApiService().changeStatus(session.getJWT(), orderId, selectedStatus);
        updStatus.enqueue(new retrofit2.Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("200 OK"))
                        binding.tvStatus.setText(selectedStatus);
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });
    }

    private void changeDeliveredDate() {
        ArrayList<String> deliveredDate = new ArrayList<>();
        String date = String.valueOf(java.time.LocalDate.now());
        deliveredDate.add("not delivered yet");
        deliveredDate.add(date);

        final ArrayAdapter<String> adp = new ArrayAdapter<>(getContext(),
                android.R.layout.select_dialog_item,
                deliveredDate);


        final Spinner sp = new Spinner(getContext());
        sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sp.setAdapter(adp);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedDeliveredDate = deliveredDate.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialog);
        builder.setView(sp);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateDeliveredDate(orderId, selectedDeliveredDate);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();


    }

    private void updateDeliveredDate(Integer orderId, String selectedDeliveredDate) {
        Call<JsonResponse> updDeliveredDate = SmartAPI.getApiService().changeDeliveredDate(session.getJWT(), orderId, selectedDeliveredDate);
        updDeliveredDate.enqueue(new retrofit2.Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("200 OK"))
                        binding.tvDeliveredDate.setText(selectedDeliveredDate);
                }

            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });
    }

    private void getPassedValues() {

        OrderDetailsArgs orderDetailsArgs = OrderDetailsArgs.fromBundle(getArguments());
        OrderResponse orderResponse = orderDetailsArgs.getOrder();

        //set
        orderId = orderResponse.getOrderId();
        binding.tvProductName.setText(orderResponse.getProductName());
        binding.tvPrice.setText("Rs. " + orderResponse.getPrice());
        binding.tvDeliveryAddress.setText(orderResponse.getDeliveryAddress());
        binding.tvOrderedUserName.setText(orderResponse.getUserName());
        binding.tvQty.setText(String.valueOf(orderResponse.getQuantity()));
        binding.tvOrderedDate.setText(orderResponse.getOrderedDate());
        binding.tvStatus.setText(orderResponse.getStatus());
        binding.tvOrderedContactNum.setText(orderResponse.getPhone());

        binding.tvDeliveredDate.setText(orderResponse.getDeliveredDate());
        if (orderResponse.getProductColor() == null)
            binding.tvProductColor.setText("Color option not available");
        else
            binding.tvProductColor.setText(orderResponse.getProductColor());

        binding.tvproductSize.setText(String.valueOf(orderResponse.getProductSize()));
        try {
            String url = SmartAPI.IMG_BASE_URL+orderResponse.getPicturePath();
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
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


}