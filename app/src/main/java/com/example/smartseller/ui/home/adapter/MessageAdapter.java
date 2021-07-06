package com.example.smartseller.ui.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartseller.R;
import com.example.smartseller.data.model.MessageResponse;
import com.example.smartseller.data.model.Products;
import com.example.smartseller.data.network.SmartAPI;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Myviewholder> {


    public ArrayList<MessageResponse> messageResponses;
    Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public MessageAdapter(ArrayList<MessageResponse> messageResponses, Context context) {
        this.messageResponses = messageResponses;
        this.context = context;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation_list, parent, false);
        Myviewholder evh = new Myviewholder(v, mListener);
        return evh;

    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        final MessageResponse message = messageResponses.get(position);
        String product_name = message.getProduct_name();
        holder.tvProductName.setText(product_name);
        holder.tvMessage.setText(message.getMessage().substring(0, message.getMessage().length() / 3) + "...");
        holder.tvDate.setText(message.getDate());


        try {
            String url = SmartAPI.IMG_BASE_URL + message.getPicture_path();

            Picasso.get()
                    .load(url)
                    .fit()
                    .into(holder.ivImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Load", "Successfull");

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Load", e.getMessage());
                        }
                    });
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messageResponses.size();
    }

    public static class Myviewholder extends RecyclerView.ViewHolder {

        TextView tvProductName, tvMessage, tvDate;
        ImageView ivImage;


        public Myviewholder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProduct_Name);
            ivImage = itemView.findViewById(R.id.product_image);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvDate = itemView.findViewById(R.id.tvDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }


}