package com.example.smartseller.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;


import com.example.smartseller.NavigationExtensionsKt;
import com.example.smartseller.R;
import com.example.smartseller.databinding.ActivityHomeBinding;
import com.example.smartseller.ui.home.MessageFragment.MessageFragment;
import com.example.smartseller.util.session.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Arrays;
import java.util.List;

import kotlin.collections.CollectionsKt;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HOME_ACTIVITY";
    private LiveData<NavController> currentNavController;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView( view );
        if (savedInstanceState == null) {
            this.setupBottomNavigationBar();
        }

        binding.fabMessage.setOnClickListener(v -> getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container,new MessageFragment())
                .commit());

    }

    protected void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );
        this.setupBottomNavigationBar();
    }


    void setupBottomNavigationBar() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        List<Integer> navGraphIds = Arrays.asList(
                R.navigation.home,
                R.navigation.account,
                R.navigation.myproduct,
                R.navigation.order,
                R.navigation.addproduct

        );
        LiveData<NavController> controller = NavigationExtensionsKt.setupWithNavController(
                bottomNavigationView,
                navGraphIds,
                getSupportFragmentManager(),
                R.id.fragment_container, getIntent() );
        controller.observe(this,navController -> {
            androidx.navigation.ui.NavigationUI.setupActionBarWithNavController(this,navController);

        });


        this.currentNavController = controller;


    }

    private void showExitDialog() {
        new MaterialAlertDialogBuilder(this)
               .setTitle("Exit App")
               .setMessage("Do you want to exit app?")
                .setCancelable(false)
               .setPositiveButton("Yes", (dialog, which) -> {
                   finishAffinity();
               })
               .setNegativeButton("No", (dialog, which) -> {
                   dialog.dismiss();
               }).show();
    }

    public boolean onSupportNavigateUp() {
        LiveData<NavController> var10000 = this.currentNavController;
        boolean var2;
        if (var10000 != null) {
            NavController var1 = var10000.getValue();
             if (var1 != null) {
                var2 = var1.navigateUp();
                return var2;
            }

        }


        var2 = false;
        return var2;
    }

    @Override
    public void onBackPressed() {
        LiveData<NavController> controllerLiveData = this.currentNavController;
        String current = (String) controllerLiveData.getValue().getCurrentDestination().getLabel();
        Log.i(TAG, "onBackPressed: "+current);
        if (current.equals("Home"))
            showExitDialog();
        else
            super.onBackPressed();
    }
}