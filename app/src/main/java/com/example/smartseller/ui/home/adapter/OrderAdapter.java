package com.example.smartseller.ui.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartseller.R;
import com.example.smartseller.data.model.OrderResponse;
import com.example.smartseller.data.model.Products;
import com.example.smartseller.data.network.SmartAPI;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewholder> {

    private ArrayList<OrderResponse> orderResponses = new ArrayList<>();
    private OrderAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OrderAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public OrderAdapter(ArrayList<OrderResponse> orderResponses, Context context) {
        this.orderResponses = orderResponses;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_list, parent, false);
        return new MyViewholder(v, mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        final OrderResponse orderResponse = orderResponses.get(position);
        holder.tvProductName.setText(orderResponse.getProductName());
        holder.tvOrderedId.setText("Order id: " + orderResponse.getOrderId());
        holder.tvOrderedDate.setText("Order date: " + orderResponse.getOrderedDate());
        holder.tvDiscount.setText("Discount: " + orderResponse.getDiscount() + "%");
        holder.tvQty.setText("Qty: " + orderResponse.getQuantity());
        holder.tvPrice.setText("Rs. " + orderResponse.getPrice());
        holder.tvDeliveredDate.setText("Delivered date: " + orderResponse.getDeliveredDate());
        holder.tvDeliveryAddress.setText("Delivery address: " + orderResponse.getDeliveryAddress());
        String color = orderResponse.getProductColor();
        Float size = orderResponse.getProductSize();
        if (color == null) holder.tvColor.setText("Color: No color option available");
        else holder.tvColor.setText("Color: " + color);
        if (size == null) holder.tvSize.setText("Size: No size option available");
        else holder.tvSize.setText("Size:" + size);


        try {
            String url = SmartAPI.IMG_BASE_URL + orderResponse.getPicturePath();

            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(holder.ivImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Load", "Successful");
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
        return orderResponses.size();
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {
        private TextView tvProductName;
        TextView tvOrderedId;
        TextView tvOrderedDate;
        TextView tvDiscount;
        TextView tvQty;
        TextView tvPrice;
        TextView tvStatus;
        ImageView ivImg;
        TextView tvFullDetails;
        LinearLayout hiddenLayout;
        TextView tvDeliveredDate;
        TextView tvDeliveryAddress;
        TextView tvColor;
        TextView tvSize;

        public MyViewholder(@NonNull View itemView, final OrderAdapter.OnItemClickListener listener) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProduct_Name);
            tvOrderedId = itemView.findViewById(R.id.tvOrderedID);
            tvOrderedDate = itemView.findViewById(R.id.tvOrderedDate);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            ivImg = itemView.findViewById(R.id.ivImg);
            tvFullDetails = itemView.findViewById(R.id.tvFullDetails);
            hiddenLayout = itemView.findViewById(R.id.hiddenLayout);
            tvDeliveredDate = itemView.findViewById(R.id.tvDeliveredDate);
            tvDeliveryAddress = itemView.findViewById(R.id.tvDeliveryAddress);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvSize = itemView.findViewById(R.id.tvSize);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });


        }
    }
}
