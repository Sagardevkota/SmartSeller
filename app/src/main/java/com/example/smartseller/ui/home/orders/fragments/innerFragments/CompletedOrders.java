package com.example.smartseller.ui.home.orders.fragments.innerFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartseller.R;
import com.example.smartseller.data.model.OrderResponse;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentCompletedOrdersBinding;
import com.example.smartseller.ui.home.adapter.OrderAdapter;
import com.example.smartseller.util.session.Session;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompletedOrders extends Fragment {

    private static final String TAG = "COMPLETED_ORDERS";
    private FragmentCompletedOrdersBinding binding;
    private Session session;
    private final ArrayList<OrderResponse> orderResponseList=new ArrayList<>();
    private OrderAdapter orderAdapter;



    public CompletedOrders() {
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
        binding=FragmentCompletedOrdersBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        session=new Session(getActivity());
        getOrders();
        initRecyclerView(view);
        return view;
         }

    private void getOrders() {
       SmartAPI.getApiService().getOrders(session.getJWT(),"completed")
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(orderResponses -> {
                   orderAdapter.notifyItemRangeRemoved(0,orderAdapter.getItemCount());
                   orderResponseList.clear();
                   orderResponseList.addAll(orderResponses);
                   orderAdapter.notifyItemRangeInserted(0,orderResponses.size());

               },throwable -> Log.e(TAG, "getOrders: "+throwable.getMessage() ));


    }

    private void initRecyclerView(View view) {
        orderAdapter=new OrderAdapter(orderResponseList,getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.rvCompletedOrder.setLayoutManager(layoutManager);
        binding.rvCompletedOrder.setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                OrderResponse orderResponse = orderResponseList.get(position);
                Bundle args=new Bundle();
                args.putParcelable("orderObj",orderResponse);
                OrderDetails orderDetails=new OrderDetails();
                orderDetails.setArguments(args);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("COMPLETED_ORDERS")
                        .replace(R.id.fragment_container,orderDetails).commit();

            }
        });

    }
}