package com.example.smartseller.ui;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;

import com.example.smartseller.R;


import com.example.smartseller.ui.auth.LoginActivity;
import com.example.smartseller.ui.home.HomeActivity;
import com.example.smartseller.util.session.Session;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.time.Instant;

import android.util.Base64;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity {
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new Session(MainActivity.this);
        checkIfLoggedIn();
    }

    public void checkIfLoggedIn() {
        if (!session.getToken().isEmpty()) {
            String token = session.getToken();
            String[] split_string = token.split("\\.");
            String payload = split_string[1];
            String body = new String(Base64.decode(payload, Base64.DEFAULT));
            HashMap<String, String> map = new Gson().fromJson(body, new TypeToken<HashMap<String, String>>() {
            }.getType());
            String exp = map.get("exp");

            if (checkISExpired(exp)) {
                Toasty.error(getApplicationContext(), "Session expired").show();
                session.destroy();
                goToLoginActivity();
            } else
                goToHomeActivity();

        } else
            goToLoginActivity();


    }


    private Boolean checkISExpired(String exp) {
        if (Instant.now().getEpochSecond() >= Long.valueOf(exp)) {
            Log.d("expired", "true");
            return true;
        } else
            return false;


    }


    private void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

    public void goToHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


}