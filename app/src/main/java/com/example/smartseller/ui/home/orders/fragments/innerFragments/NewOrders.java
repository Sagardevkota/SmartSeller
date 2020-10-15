package com.example.smartseller.ui.home.orders.fragments.innerFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartseller.R;
import com.example.smartseller.data.model.OrderResponse;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentNewOrdersBinding;
import com.example.smartseller.ui.home.adapter.OrderAdapter;
import com.example.smartseller.util.session.Session;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewOrders extends Fragment {
    private FragmentNewOrdersBinding binding;
    private Session session;
    private ArrayList<OrderResponse> orderResponses=new ArrayList<>();
    private OrderAdapter orderAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentNewOrdersBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        session=new Session(getActivity());
        getOrders();

        initRecyclerView();
        return view;
    }

    private void getOrders() {
        Call<List<OrderResponse>> getOrder= SmartAPI.getApiService().getOrders(session.getJWT(),session.getUserId(),"waiting");
        getOrder.enqueue(new Callback<List<OrderResponse>>() {
            @Override
            public void onResponse(Call<List<OrderResponse>> call, Response<List<OrderResponse>> response) {
                if (response.isSuccessful())
                {
                    orderResponses.clear();
                    response.body()
                            .forEach(orderResponse -> orderResponses.add(new OrderResponse(orderResponse)));
                    orderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponse>> call, Throwable t) {

            }
        });
    }

    private void initRecyclerView() {
        orderAdapter=new OrderAdapter(orderResponses,getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.rvNewOrder.setLayoutManager(layoutManager);
        binding.rvNewOrder.setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                OrderResponse orderResponse=orderResponses.get(position);
                Bundle args=new Bundle();
                args.putParcelable("orderObj",orderResponse);
                OrderDetails orderDetails=new OrderDetails();
                orderDetails.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,orderDetails).commit();

            }
        });
    }


}