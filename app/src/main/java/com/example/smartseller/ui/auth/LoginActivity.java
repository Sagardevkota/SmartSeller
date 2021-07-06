package com.example.smartseller.ui.auth;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.smartseller.R;
import com.example.smartseller.data.model.User;
import com.example.smartseller.data.network.JsonResponse;
import com.example.smartseller.data.network.JwtResponse;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.ActivityLoginBinding;
import com.example.smartseller.ui.home.HomeActivity;
import com.example.smartseller.util.session.Session;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LOGIN_ACTIVITY";
    ActivityLoginBinding binding;
    SharedPreferences sp;
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        session = new Session(LoginActivity.this);


        binding.loginClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();
                checkLogin(email, password);
            }
        });


    }

    private void checkLogin(String email, String password) {
        if (email.isEmpty() || password.isEmpty())
            Toasty.error(this, "Fields cant be empty").show();
        else {

            showProgressDialog("Logging in");
            User user = User.builder()
                    .userName(email)
                    .password(password)
                    .build();

           SmartAPI.getApiService().login(user)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(response -> {
                       String status = response.getStatus();
                       String message = response.getMessage();
                       String role = response.getRole();

                       String[] roles = role
                               .substring(1, role.length() - 1)
                               .split(","); //role can be "[ADMIN,USER]"
                       if (status.equalsIgnoreCase("200 OK")) {
                           if (Arrays.stream(roles)
                                   .anyMatch(role1 -> role1.equalsIgnoreCase("SELLER"))) {
                               Toasty.success(getApplicationContext(), "Login Successful").show();
                               String token = response.getJwt();
                               session.setToken(token);
                               session.setJWT("Bearer " + token);
                               getUserDetails();
                               hideProgressDialog();
                               goToHomeActivity();
                           } else {
                               Toasty.error(getApplicationContext(), "You are not seller Please login  to smart app!!").show();
                               hideProgressDialog();
                           }


                       } else {
                           hideProgressDialog();
                           Toasty.error(getApplicationContext(), message).show();
                       }
                   },throwable -> Log.e(TAG, "checkLogin: "+throwable.getMessage() ));


        }

    }

    private void getUserDetails() {

        SmartAPI.getApiService().getUserDetails(session.getJWT())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(user -> {
                    session.setUsername(user.getUserName());
                    session.setUserId(user.getId());
                },throwable -> Log.e(TAG, "getUserDetails: "+throwable.getMessage() ));

    }

    @Override
    public void onBackPressed() {

        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(this, R.style.AlertDialog);
        alert.setMessage("Are you sure you want to exit?").setTitle("Confirmation").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();


    }

    public void goToHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }





    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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