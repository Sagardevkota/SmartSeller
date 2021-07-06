package com.example.smartseller.ui.home.orders.fragments.innerFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.smartseller.R;
import com.example.smartseller.data.model.OrderResponse;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentDispatchedOrdersBinding;
import com.example.smartseller.ui.home.adapter.OrderAdapter;
import com.example.smartseller.util.session.Session;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class DispatchedOrders extends Fragment {

    private static final String TAG = "DISPATCHED_ORDERS";
    private FragmentDispatchedOrdersBinding binding;
    private Session session;
    private final ArrayList<OrderResponse> orderResponseList = new ArrayList<>();
    private OrderAdapter orderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDispatchedOrdersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        session = new Session(getActivity());
        getOrders();
        initRecyclerView();
        return view;
    }

    private void getOrders() {
        SmartAPI.getApiService().getOrders(session.getJWT(), "dispatched")
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
        binding.rvDispatchedOrder.setLayoutManager(layoutManager);
        binding.rvDispatchedOrder.setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener(position -> {
            OrderResponse orderResponse = orderResponseList.get(position);
            Bundle args = new Bundle();
            args.putParcelable("orderObj", orderResponse);
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setArguments(args);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("DISPATCHED_ORDERS")
                    .replace(R.id.fragment_container, orderDetails).commit();

        });

    }
}