package com.example.smartseller.ui.home.AccountFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartseller.R;
import com.example.smartseller.data.model.User;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentAccountBinding;
import com.example.smartseller.util.session.Session;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Account extends Fragment {
    private FragmentAccountBinding binding;
    private Session session;
    private User user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAccountBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        session=new Session(getContext());
        getAccountDetails();
        binding.ivEdit.setOnClickListener((view1 -> {
            AccountEdit edit=new AccountEdit();
            Bundle args=new Bundle();
            args.putParcelable("userObj",user);
            edit.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,edit).commit();


        }));
        return view;
    }

    private void getAccountDetails(){
        Call<User> getUser= SmartAPI.getApiService().getUserDetails(session.getJWT(),session.getusername());
        getUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful())
                {
                    binding.ivEdit.setEnabled(true);
                    Log.d("user",response.body().toString());
                    setView(response.body());
                }
                else
                binding.ivEdit.setEnabled(false);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                binding.ivEdit.setEnabled(false);

            }
        });

    }

    private void setView(User body) {
        user=body;
        binding.tvUserName.setText(body.getUserName());
        binding.tvPhone.setText(body.getPhone());
        binding.tvAddress.setText(body.getDeliveryAddress());
        binding.tvAccountType.setText(body.getRole());

    }
}