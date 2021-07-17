package com.example.smartseller.ui.home.MyProductFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartseller.R;
import com.example.smartseller.data.model.Products;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentMyProductBinding;
import com.example.smartseller.ui.home.HomeActivity;
import com.example.smartseller.ui.home.adapter.ListAdapter;
import com.example.smartseller.ui.home.homeFragment.home;
import com.example.smartseller.util.session.Session;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProduct extends Fragment {
    private static final String TAG = "MY_PRODUCT";
    private FragmentMyProductBinding binding;
    private Session session;
    private final ArrayList<Products> productsArrayList = new ArrayList<>();
    private ListAdapter adapter;


    public MyProduct() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyProductBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        session = new Session(requireActivity());
        ((HomeActivity)requireActivity()).fabMsgVisibility(false);

        initRecyclerView();
        getMyProducts();
        return view;
    }



    private void initRecyclerView() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        adapter = new ListAdapter(productsArrayList, getContext());
        binding.rvMyProducts.setAdapter(adapter);
        binding.rvMyProducts.setLayoutManager(staggeredGridLayoutManager);
        adapter.setOnItemClickListener(position -> {
            MyProductDirections.ActionProductToItsDetails productToItsDetails =
                    MyProductDirections.actionProductToItsDetails(productsArrayList.get(position));

            Navigation.findNavController(getView()).navigate(productToItsDetails);
        });


    }

    private void getMyProducts() {
        productsArrayList.clear();
        SmartAPI.getApiService().getProducts(session.getJWT())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(products -> {
                    productsArrayList.addAll(products);
                    adapter.notifyItemRangeInserted(0, products.size());
                }, throwable -> Log.e(TAG, "getMyProducts: " + throwable.getMessage()));

    }
}