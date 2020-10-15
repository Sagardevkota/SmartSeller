package com.example.smartseller.ui.home.adapter;

import android.content.Context;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartseller.R;
import com.example.smartseller.data.model.Products;
import com.example.smartseller.databinding.LayoutProductListsBinding;
import com.example.smartseller.ui.home.MyProductFragment.MyProductDetails;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Myviewholder> {


    public ArrayList<Products> productItems;
    Context context;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public ListAdapter(ArrayList<Products> productItems, Context context) {
        this.productItems=productItems;
        this.context=context;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_lists, parent, false);
        Myviewholder evh = new Myviewholder(v, mListener);
        return evh;

    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        final Products currentItem=productItems.get(position);
        String product_name=currentItem.getProductName();
        holder.tvProductName.setText(product_name);

        try{
            String url=currentItem.getPicture_path();
            Picasso.get()
                    .load(url)
                    .fit()
                    .into(holder.ivImge, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Load","Successfull");

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Load",e.getMessage());
                        }
                    });}
        catch (Exception e){
            Log.d("error",e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }
    public static class Myviewholder extends RecyclerView.ViewHolder {

        TextView tvProductName;
        ImageView ivImge;



        public Myviewholder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvProductName=itemView.findViewById(R.id.tvProduct_Name);
            ivImge=itemView.findViewById(R.id.ivImg);

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