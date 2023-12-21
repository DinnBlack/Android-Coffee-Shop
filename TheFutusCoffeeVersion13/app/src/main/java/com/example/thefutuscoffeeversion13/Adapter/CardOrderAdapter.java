package com.example.thefutuscoffeeversion13.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thefutuscoffeeversion13.Domain.CardModel;
import com.example.thefutuscoffeeversion13.Domain.OrderModel;
import com.example.thefutuscoffeeversion13.R;

import java.util.List;

public class CardOrderAdapter extends RecyclerView.Adapter<CardOrderAdapter.ViewHolder>{

    private Context context;
    private List<OrderModel> list;

    public CardOrderAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView idOrder, dayOrder, totalPriceOrder, statusOrder;
        ImageView ivImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            idOrder = itemView.findViewById(R.id.idOrder);
            dayOrder = itemView.findViewById(R.id.dayOrder);
            totalPriceOrder = itemView.findViewById(R.id.totalPriceOrder);
            statusOrder = itemView.findViewById(R.id.statusOrder);
        }
    }
    @NonNull
    @Override
    public CardOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardOrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardOrderAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(R.drawable.delivery).into(holder.ivImage);
        holder.idOrder.setText(list.get(position).getDaytime());
        holder.dayOrder.setText(list.get(position).getDay());
        holder.totalPriceOrder.setText(list.get(position).getPrice());
        holder.statusOrder.setText(list.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
