package com.example.smartseller.ui.home.orders.fragments.innerFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewOrders extends Fragment {
    private static final String TAG = "NEW_ORDERS";
    private FragmentNewOrdersBinding binding;
    private Session session;
    private final ArrayList<OrderResponse> orderResponseList = new ArrayList<>();
    private OrderAdapter orderAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewOrdersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        session = new Session(getActivity());
        getOrders();

        initRecyclerView();
        return view;
    }

    private void getOrders() {
        SmartAPI.getApiService().getOrders(session.getJWT(), "waiting")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderResponses -> {
                    orderAdapter.notifyItemRangeRemoved(0,orderAdapter.getItemCount());
                    orderResponseList.clear();
                    orderResponseList.addAll(orderResponses);
                    orderAdapter.notifyItemRangeInserted(0, orderResponses.size());
                }, throwable -> Log.e(TAG, "getOrders: " + throwable.getMessage()));

    }

    private void initRecyclerView() {
        orderAdapter = new OrderAdapter(orderResponseList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvNewOrder.setLayoutManager(layoutManager);
        binding.rvNewOrder.setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener(position -> {
            OrderResponse orderResponse = orderResponseList.get(position);
            Bundle args = new Bundle();
            args.putParcelable("orderObj", orderResponse);
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setArguments(args);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("NEW_ORDERS")
                    .replace(R.id.fragment_container, orderDetails).commit();

        });
    }


}