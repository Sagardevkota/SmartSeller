package com.example.smartseller.ui.home.AccountFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartseller.R;
import com.example.smartseller.data.model.User;
import com.example.smartseller.data.network.JsonResponse;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentAccountEditBinding;
import com.example.smartseller.ui.auth.LoginActivity;
import com.example.smartseller.util.session.Session;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountEdit extends Fragment {
    
    private FragmentAccountEditBinding binding;
    private Session session;

   

    public AccountEdit() {
        // Required empty public constructor
    }

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAccountEditBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        session=new Session(getContext());
        getPassedValues();
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Account()).commit();
            }
        });

        binding.buSaveEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString();
                if (email.isEmpty())
                    Toasty.error(getContext(), "Field cant be empty").show();

                else if (email.equals(session.getusername()))
                    Toasty.error(getContext(), "No changes made").show();
                else
                    updateEmail(email);


            }
        });
        binding.buSavePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = binding.etPhone.getText().toString();
                if (phone.isEmpty())
                    Toasty.error(getContext(), "Field cant be empty").show();
                else if (phone.length() != 10)
                    Toasty.error(getContext(), "Please enter valid phone number").show();
                else
                    updatePhone(phone);
            }
        });
        binding.buSaveDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String delivery = binding.etDelivery.getText().toString();
                if (!delivery.isEmpty())
                    updateDelivery(delivery);
                else
                    Toasty.error(getContext(), "Field cant be empty").show();

            }
        });

        return view;
         }

    private void getPassedValues() {
        Bundle b=getArguments();
        if (b!=null){
            User user=b.getParcelable("userObj");
            binding.etEmail.setText(user.getUserName());
            binding.etPhone.setText(user.getPhone());
            binding.etDelivery.setText(user.getDeliveryAddress());

        }
    }

    private void updateEmail(String email) {
        MaterialAlertDialogBuilder alertDialogBuilder=new MaterialAlertDialogBuilder(getContext());
        alertDialogBuilder.setTitle("Logout Confirmation")
                .setMessage("You will be logged out after changing your email address")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Call<JsonResponse> updateEmail = SmartAPI.getApiService().updateEmail(session.getJWT(), session.getUserId(), email);
                        updateEmail.enqueue(new Callback<JsonResponse>() {
                            @Override
                            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                                if (!response.isSuccessful())
                                    Log.d("unsuccessful", "unsuccessful");
                                else {
                                    String status = response.body().getStatus();
                                    String message = response.body().getMessage();
                                    if (status.equalsIgnoreCase("200 Ok"))

                                    {
                                        Intent intent=new Intent(getActivity(), LoginActivity.class);
                                        session.destroy();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                        Toasty.success(getContext(), message).show();
                                    }
                                    else
                                        Toasty.error(getContext(), message).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<JsonResponse> call, Throwable t) {

                            }
                        });


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();


    }

    private void updatePhone(String phone) {
        Call<JsonResponse> updatePhone = SmartAPI.getApiService().updatePhone(session.getJWT(), session.getUserId(), phone);
        updatePhone.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (!response.isSuccessful())
                    Log.d("unsuccessful", "unsuccessful");
                else {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("200 Ok"))
                        Toasty.success(getContext(), message).show();
                    else
                        Toasty.error(getContext(), message).show();
                }

            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });


    }

    private void updateDelivery(String delivery) {
        Call<JsonResponse> updateDelivery = SmartAPI.getApiService().updateDelivery(session.getJWT(), session.getUserId(), delivery);
        updateDelivery.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (!response.isSuccessful())
                    Log.d("unsuccessful", "unsuccessful");
                else {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("200 Ok"))
                        Toasty.success(getContext(), message).show();
                    else
                        Toasty.error(getContext(), message).show();
                }

            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });


    }
}