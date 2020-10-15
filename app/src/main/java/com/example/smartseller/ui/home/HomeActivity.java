package com.example.smartseller.ui.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.smartseller.R;
import com.example.smartseller.databinding.ActivityHomeBinding;
import com.example.smartseller.ui.auth.LoginActivity;
import com.example.smartseller.ui.home.AccountFragment.Account;
import com.example.smartseller.ui.home.AddProductFragment.addProduct;
import com.example.smartseller.ui.home.MessageFragment.MessageFragment;
import com.example.smartseller.ui.home.MyProductFragment.MyProduct;
import com.example.smartseller.ui.home.homeFragment.home;
import com.example.smartseller.ui.home.orders.fragments.mainFragment.Orders;
import com.example.smartseller.util.session.Session;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        session=new Session(HomeActivity.this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new home(),"HomeFragment").commitAllowingStateLoss();

        setFragmentIsAttached("Home");
        binding.tvAppbar.setVisibility(View.VISIBLE);
        binding.fabLove.setVisibility(View.VISIBLE);
        binding.fabMessage.setVisibility(View.VISIBLE);
       

        binding.tvOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                binding.tvAppbar.setText("Orders");
                binding.tvAppbar.setVisibility(View.VISIBLE);
                binding.fabLove.setVisibility(View.VISIBLE);
                binding.fabMessage.setVisibility(View.GONE);


                setFragmentIsAttached("Orders");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Orders(),"OrderFragment").commitAllowingStateLoss();
            }
        });
        binding.tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragmentIsAttached("Home");
                binding.tvAppbar.setText("Dashboard");
                binding.tvAppbar.setVisibility(View.VISIBLE);
                binding.fabLove.setVisibility(View.VISIBLE);
                binding.fabMessage.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new home(),"OrderFragment").commitAllowingStateLoss();

            }
        });
        binding.tvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvAppbar.setText("Account");
                binding.tvAppbar.setVisibility(View.VISIBLE);
                binding.fabLove.setVisibility(View.VISIBLE);
                binding.fabMessage.setVisibility(View.GONE);

                setFragmentIsAttached("Account");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Account(),"OrderFragment").commitAllowingStateLoss();

            }
        });
        binding.ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MaterialAlertDialogBuilder alertDialogBuilder= new MaterialAlertDialogBuilder(HomeActivity.this,R.style.AlertDialog);
                alertDialogBuilder.setTitle("Confirmation").setMessage("Are you sure you want to logout")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(HomeActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                session.destroy();
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                ;

            }
        });
        binding.fabMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MessageFragment()).commit();

                binding.bottomAppBar.setVisibility(View.GONE);
                binding.fabLove.setVisibility(View.GONE);
           binding.tvAppbar.setText("Chats");
           binding.fabMessage.setVisibility(View.GONE);
            }
        });

        binding.tvMyProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragmentIsAttached("Product");
                binding.tvAppbar.setText("My Products");
                binding.bottomAppBar.setVisibility(View.VISIBLE);
                binding.fabLove.setVisibility(View.VISIBLE);
                binding.fabMessage.setVisibility(View.GONE);



                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MyProduct(),"ProductFragment").commitAllowingStateLoss();
            }
        });
        binding.fabLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvAppbar.setText("Add Product");
                setFragmentIsAttached("Add Product");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new addProduct()).commitAllowingStateLoss();
                binding.bottomAppBar.setVisibility(View.VISIBLE);
                binding.fabLove.setVisibility(View.VISIBLE);
                binding.fabMessage.setVisibility(View.GONE);
            }
        });

    }


    private void setFragmentIsAttached(String fragmentIsAttached) {
        switch (fragmentIsAttached){

            case "Orders":
                binding.tvOrders.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_orders_black, 0,0);
                binding.tvAccount.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_account_circle_24px, 0,0);
                binding.tvHome.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_home_24px, 0,0);
                binding.tvMyProducts.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_product, 0,0);
                binding.fabLove.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.White)));
                binding.tvOrders.setTextColor(Color.parseColor("#f44336"));
                binding.tvAccount.setTextColor(Color.BLACK);
                binding.tvMyProducts.setTextColor(Color.BLACK);
                binding.tvHome.setTextColor(Color.BLACK);
                binding.tvAddProduct.setTextColor(Color.BLACK);
                break;

            case "Home":
               binding.tvHome.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_home_black_24dp, 0,0);
               binding.tvHome.setTextColor(Color.parseColor("#f44336"));
                binding.tvAccount.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_account_circle_24px, 0,0);
                binding.tvOrders.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_orders, 0,0);
                binding.fabLove.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.White)));
                binding.tvMyProducts.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_product, 0,0);
                binding.tvAccount.setTextColor(Color.BLACK);
                binding.tvMyProducts.setTextColor(Color.BLACK);
                binding.tvOrders.setTextColor(Color.BLACK);
                binding.tvAddProduct.setTextColor(Color.BLACK);
                break;

            case "Account":
                binding.tvAccount.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_account_black_24dp, 0,0);
                binding.tvAccount.setTextColor(Color.parseColor("#f44336"));
                binding.tvHome.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_home_24px, 0,0);
                binding.tvOrders.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_orders, 0,0);
                binding.tvMyProducts.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_product, 0,0);
                binding.fabLove.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.White)));
                binding.tvOrders.setTextColor(Color.BLACK);
                binding.tvMyProducts.setTextColor(Color.BLACK);
                binding.tvHome.setTextColor(Color.BLACK);
                binding.tvAddProduct.setTextColor(Color.BLACK);
                break;

            case "Product":
                binding.tvAccount.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_account_circle_24px, 0,0);
                binding.tvHome.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_home_24px, 0,0);
                binding.tvOrders.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_orders, 0,0);
                binding.tvMyProducts.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_product_black, 0,0);
                binding.fabLove.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.White)));
                binding.tvMyProducts.setTextColor(Color.parseColor("#f44336"));
                binding.tvAccount.setTextColor(Color.BLACK);
                binding.tvOrders.setTextColor(Color.BLACK);
                binding.tvHome.setTextColor(Color.BLACK);
                binding.tvAddProduct.setTextColor(Color.BLACK);
                break;

            case "Add Product":
                binding.tvAccount.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_account_circle_24px, 0,0);
                binding.tvHome.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_home_24px, 0,0);
                binding.tvOrders.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_orders, 0,0);
                binding.tvMyProducts.setCompoundDrawablesWithIntrinsicBounds(0,  R.drawable.ic_product, 0,0);
                binding.fabLove.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                binding.tvAccount.setTextColor(Color.BLACK);
                binding.tvOrders.setTextColor(Color.BLACK);
                binding.tvHome.setTextColor(Color.BLACK);
                binding.tvMyProducts.setTextColor(Color.BLACK);
                binding.tvAddProduct.setTextColor(Color.parseColor("#f44336"));
                break;

        }
    }

    @Override
    public void onBackPressed() {

        MaterialAlertDialogBuilder alertDialogBuilder=new MaterialAlertDialogBuilder(this,R.style.AlertDialog);
        alertDialogBuilder.setTitle("Exit the app")
                .setMessage("Do you want to exit the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }



}