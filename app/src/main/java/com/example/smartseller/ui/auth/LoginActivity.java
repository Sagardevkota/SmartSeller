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

import java.util.Base64;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    SharedPreferences sp;
    private Session session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        session=new Session(LoginActivity.this);






        binding.loginClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=binding.etEmail.getText().toString().trim();
                String password=binding.etPassword.getText().toString().trim();
                checkLogin(email, password);
            }
        });




    }

    private void checkLogin(String email, String password) {
        Log.d("emailpass",email+password);
        if (email.isEmpty() || password.isEmpty())
        {
            Toasty.error(this, "Fields cant be empty").show();
            return;
        }

        else {

            showProgressDialog("Logging in");
            Call<JwtResponse> checklogin = SmartAPI.getApiService().login(new User(email, password));
            checklogin.enqueue(new Callback<JwtResponse>() {
                @Override
                public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                    if (response.isSuccessful()) {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();
                        String role = response.body().getRole();
                        if (status.equalsIgnoreCase("200 OK")) {
                            if (role.equalsIgnoreCase("SELLER")) {
                                Toasty.success(getApplicationContext(),"Login Successfull").show();
                                String token = response.body().getJwt();
                                session.setToken(token);
                                session.setJWT("Bearer "+token);
                             String userName=   getUserName(token);
                             session.setUsername(userName);
                             getUserId(userName);
                             hideProgressDialog();
                                goToHomeActivity();
                            } else
                            {
                                Toasty.error(getApplicationContext(), "You are not seller Please login  to smart app!!").show();
                                hideProgressDialog();
                            }


                        } else {
                            hideProgressDialog();
                            Toasty.error(getApplicationContext(), message).show();
                        }

                    } else {
                        hideProgressDialog();
                        Toasty.error(getApplicationContext(), response.errorBody().toString()).show();
                    }


                }

                @Override
                public void onFailure(Call<JwtResponse> call, Throwable t) {
                    Toasty.error(getApplicationContext(),t.getMessage()).show();

                }
            });

        }

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


    public void getUserId(String userName) {
        Call<JsonResponse> getUserId = SmartAPI.getApiService().getUserId(session.getJWT(), userName);
        getUserId.enqueue(new retrofit2.Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("200 OK"))
                        session.setUserId(Integer.valueOf(message));



                }


            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Log.e("error", t.getMessage());

            }
        });




    }

    private String getUserName(String jwt){
        String token = jwt;
        String[] split_string = token.split("\\.");
        String payload = split_string[1];
        String body = new String(Base64.getDecoder().decode(payload));
        HashMap<String, String> map = new Gson().fromJson(body, new TypeToken<HashMap<String, String>>() {
        }.getType());
        session.setUsername(map.get("sub"));

        return map.get("sub");

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