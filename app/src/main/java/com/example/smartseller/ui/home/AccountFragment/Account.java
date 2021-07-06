package com.example.smartseller.ui.home.AccountFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.smartseller.R;
import com.example.smartseller.data.model.User;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentAccountBinding;
import com.example.smartseller.util.session.Session;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class Account extends Fragment {
    private static final String TAG = "ACCOUNT";
    private FragmentAccountBinding binding;
    private Session session;
    private User user;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        session = new Session(getContext());
        getAccountDetails();

        binding.ivEdit.setOnClickListener((view1 -> {

            AccountDirections.ActionAccountToAccountEditFragment actionAccountToAccountEditFragment
                    =
                    AccountDirections.actionAccountToAccountEditFragment(user);

            Navigation.findNavController(view)
                    .navigate(actionAccountToAccountEditFragment);



        }));
        return view;
    }

    private void getAccountDetails() {
        SmartAPI.getApiService().getUserDetails(session.getJWT())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setView,
                        throwable -> Log.e(TAG, "getAccountDetails: " + throwable.getMessage()));

    }

    private void setView(User body) {
        this.user = body;
        binding.tvUserName.setText(body.getUserName());
        binding.tvPhone.setText(body.getPhone());
        binding.tvAddress.setText(body.getDeliveryAddress());
        binding.tvAccountType.setText(body.getRole());

    }
}