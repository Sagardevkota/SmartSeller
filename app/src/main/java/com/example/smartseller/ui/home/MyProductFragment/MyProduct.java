package com.example.smartseller.ui.home.MyProductFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartseller.R;
import com.example.smartseller.data.model.Products;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentMyProductBinding;
import com.example.smartseller.ui.home.adapter.ListAdapter;
import com.example.smartseller.util.session.Session;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProduct extends Fragment {
    private FragmentMyProductBinding binding;
    private Session session;
    private ArrayList<Products> productsArrayList=new ArrayList<>();
    private ListAdapter adapter;


    public MyProduct() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentMyProductBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        session=new Session(getActivity());
        initRecyclerView();
        getMyProducts();
        return view;
    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        adapter=new ListAdapter(productsArrayList,getContext());
        binding.rvMyProducts.setAdapter(adapter);
        binding.rvMyProducts.setLayoutManager(staggeredGridLayoutManager);
        adapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MyProductDetails mpd=new MyProductDetails();
                Bundle args=new Bundle();
                args.putParcelable("productObj",productsArrayList.get(position));
                mpd.setArguments(args);
           getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mpd).commit();
            }
        });


    }

    private void getMyProducts() {
        Call<List<Products>> getproduct= SmartAPI.getApiService().getProducts(session.getJWT(),session.getUserId());
        getproduct.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful())
                {
                    productsArrayList.clear();
                    for (Products p:response.body()){
                        productsArrayList.add(new Products(p));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {

            }
        });
    }
}