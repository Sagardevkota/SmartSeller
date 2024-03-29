package com.example.smartseller.ui.home.MessageFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartseller.R;
import com.example.smartseller.data.model.MessageResponse;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentMessageBinding;
import com.example.smartseller.ui.home.AddProductFragment.addProduct;
import com.example.smartseller.ui.home.HomeActivity;
import com.example.smartseller.ui.home.adapter.MessageAdapter;
import com.example.smartseller.ui.home.homeFragment.home;
import com.example.smartseller.util.session.Session;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessageFragment extends Fragment {

    private FragmentMessageBinding messageBinding;
    private ArrayList<MessageResponse> messageResponses = new ArrayList<>();
    private MessageAdapter messageAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        messageBinding = FragmentMessageBinding.inflate(getLayoutInflater());
        View view = messageBinding.getRoot();
        getConversations();
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        messageAdapter = new MessageAdapter(messageResponses, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        messageBinding.rvMessage.setAdapter(messageAdapter);
        messageBinding.rvMessage.setLayoutManager(layoutManager);
        messageBinding.ivBack.setOnClickListener(view -> {
            getActivity().finish();
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        });
        messageAdapter.setOnItemClickListener(position -> {
            MessageResponse messageResponse = messageResponses.get(position);
            Integer productId = messageResponse.getProduct_id();
            String productName = messageResponse.getProduct_name();
            Bundle b = new Bundle();
            MessageDetails messageDetails = new MessageDetails();
            b.putInt("product_id", productId);
            b.putString("product_name", productName);
            messageDetails.setArguments(b);
            getActivity().getSupportFragmentManager()
                    .beginTransaction().replace(R.id.fragment_message, messageDetails).commit();

        });
    }

    private void getConversations() {
        Session session = new Session(getContext());
        Call<List<MessageResponse>> call = SmartAPI.getApiService().getConversations(session.getJWT());
        call.enqueue(new Callback<List<MessageResponse>>() {
            @Override
            public void onResponse(Call<List<MessageResponse>> call, Response<List<MessageResponse>> response) {
                if (response.isSuccessful()) {
                    messageResponses.addAll(response.body());
                    messageAdapter.notifyItemRangeInserted(0,response.body().size());
                }
            }

            @Override
            public void onFailure(Call<List<MessageResponse>> call, Throwable t) {

            }
        });
    }
}