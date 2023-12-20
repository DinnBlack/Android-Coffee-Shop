package com.example.thefutuscoffeeversion13.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thefutuscoffeeversion13.Domain.CardModel;
import com.example.thefutuscoffeeversion13.Domain.CardVoucherModel;
import com.example.thefutuscoffeeversion13.R;

import java.util.List;

public class CardVoucherAdapter extends RecyclerView.Adapter<CardVoucherAdapter.ViewHolder> {

    private Context context;
    private List<CardVoucherModel> list;

    public CardVoucherAdapter(Context context, List<CardVoucherModel> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvTitle;
        TextView tvCondition;
        TextView tvExpiry;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
        }
    }

    @NonNull
    @Override
    public CardVoucherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_list_voucher, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardVoucherAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.ivImage);
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvCondition.setText(list.get(position).getCondition());
        holder.tvExpiry.setText(list.get(position).getExpiry());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
