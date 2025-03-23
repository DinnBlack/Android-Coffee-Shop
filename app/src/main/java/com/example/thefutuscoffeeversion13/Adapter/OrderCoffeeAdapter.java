package com.example.thefutuscoffeeversion13.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thefutuscoffeeversion13.Domain.OrderCoffeeModel;
import com.example.thefutuscoffeeversion13.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderCoffeeAdapter extends RecyclerView.Adapter<OrderCoffeeAdapter.ViewHolder> {

    private ArrayList<OrderCoffeeModel> itemList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Phương thức thiết lập listener cho adapter
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public OrderCoffeeAdapter(ArrayList<OrderCoffeeModel> itemList) {
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivItemOrderCoffeePic;
        public TextView tvItemOrderCoffeeTitle;
        public TextView tvItemOrderCoffeePrice;
        public Button btItemOrderCoffeeChose;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            ivItemOrderCoffeePic = itemView.findViewById(R.id.ivItemOrderCoffeePic);
            tvItemOrderCoffeeTitle = itemView.findViewById(R.id.tvItemOrderCoffeeTitle);
            tvItemOrderCoffeePrice = itemView.findViewById(R.id.tvItemOrderCoffeePrice);
            btItemOrderCoffeeChose = itemView.findViewById(R.id.btItemOrderCoffeeChose);

            // Xử lý sự kiện click
            btItemOrderCoffeeChose.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public OrderCoffeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_order_coffee, parent, false);
        return new OrderCoffeeAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCoffeeAdapter.ViewHolder holder, int position) {
        OrderCoffeeModel item = itemList.get(position);

        holder.tvItemOrderCoffeeTitle.setText(item.getTitle());
        holder.tvItemOrderCoffeePrice.setText(item.getPrice());
        Picasso.get().load(item.getPicture()).into(holder.ivItemOrderCoffeePic);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
