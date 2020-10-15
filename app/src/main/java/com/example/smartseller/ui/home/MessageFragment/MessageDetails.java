package com.example.smartseller.ui.home.MessageFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.smartseller.R;
import com.example.smartseller.data.model.Conversation;
import com.example.smartseller.data.model.ConversationResponse;
import com.example.smartseller.data.network.JsonResponse;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentMessageDetailsBinding;
import com.example.smartseller.ui.home.adapter.ConversationAdapter;
import com.example.smartseller.util.session.Session;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessageDetails extends Fragment {
    
    private FragmentMessageDetailsBinding binding;
    ArrayList<ConversationResponse> conversationResponseList=new ArrayList<>();
    ConversationAdapter conversationAdapter;
    private Session session;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentMessageDetailsBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        session=new Session(getContext());
        Integer productId=getArguments().getInt("product_id");
        binding.tvProductTitle.setText(getArguments().getString("product_name"));


        getConversation(productId);
        binding.buSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=binding.etMessage.getText().toString().trim();
                addConversation(message,productId);
            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MessageFragment()).commit();
            }
        });
        initRecyclerView();
        return view;
    }

    private void getConversation(Integer productId) {
        Session session=new Session(getContext());
        Call<List<ConversationResponse>> getConversation= SmartAPI.getApiService().getConversation(session.getJWT(),productId);
        getConversation.enqueue(new Callback<List<ConversationResponse>>() {
            @Override
            public void onResponse(Call<List<ConversationResponse>> call, Response<List<ConversationResponse>> response) {
                if (response.isSuccessful()){
                    for (ConversationResponse c:response.body()){
                        conversationResponseList.add(new ConversationResponse(c));
                        conversationAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ConversationResponse>> call, Throwable t) {

            }
        });
    }

    private void initRecyclerView() {
        conversationAdapter=new ConversationAdapter(conversationResponseList,getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.rvQuestion.setAdapter(conversationAdapter);
        binding.rvQuestion.setLayoutManager(layoutManager);


    }

    private void addConversation(String message,Integer productId){
        String Instantdate= Instant.now().toString();
        String date=Instantdate.substring(0,10);
        Conversation conversation=new Conversation(message,session.getUserId(),date,productId);
        Call<JsonResponse> addConversation=SmartAPI.getApiService().addConversation(session.getJWT(),conversation);
        addConversation.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equalsIgnoreCase("200 OK"))
                    {
                        conversationResponseList.add(new ConversationResponse(conversation.getMessage(),session.getusername(),date));
                        conversationAdapter.notifyDataSetChanged();
                        binding.etMessage.getText().clear();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });

    }
}