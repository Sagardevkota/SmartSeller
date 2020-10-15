package com.example.smartseller.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.smartseller.R;
import com.example.smartseller.data.model.ConversationResponse;

import java.util.ArrayList;


public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MyViewholder> {

    private ArrayList<ConversationResponse> conversations;
    private Context context;

    public ConversationAdapter(ArrayList<ConversationResponse> conversations, Context context) {
        this.conversations = conversations;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_q_and_a,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        final ConversationResponse conversation=conversations.get(position);
        holder.tvMessage.setText(conversation.getMessage());
        holder.tvAsker.setText(conversation.getUser_name());
        holder.tvDate.setText(conversation.getDate());

    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvAsker;
        TextView tvDate;

    public MyViewholder(@NonNull View itemView) {
        super(itemView);
        tvMessage=itemView.findViewById(R.id.tvMessage);
        tvAsker=itemView.findViewById(R.id.tvUser);
        tvDate=itemView.findViewById(R.id.tvAskedDate);
    }
}
}
