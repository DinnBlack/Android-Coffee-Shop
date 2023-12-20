package com.example.thefutuscoffeeversion13.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thefutuscoffeeversion13.Domain.CardModel;
import com.example.thefutuscoffeeversion13.R;

import java.util.ArrayList;
import java.util.List;

public class CartDialogAdapter extends RecyclerView.Adapter<CartDialogAdapter.ViewHolder>{

    private Context context;
    private List<CardModel> list;

    public CartDialogAdapter(Context context, List<CardModel> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cartQuantity, cartProductName, cartProductSize, cartProductTopping, cartProductTotalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartQuantity = itemView.findViewById(R.id.cartQuantity);
            cartProductName = itemView.findViewById(R.id.cartProductName);
            cartProductSize = itemView.findViewById(R.id.cartProductSize);
            cartProductTopping = itemView.findViewById(R.id.cartProductTopping);
            cartProductTotalPrice = itemView.findViewById(R.id.cartProductTotalPrice);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_selected_items, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cartQuantity.setText(list.get(position).getQuantity());
        holder.cartProductName.setText(list.get(position).getTitle());
        holder.cartProductSize.setText(list.get(position).getSize());
        holder.cartProductTopping.setText(list.get(position).getTopping());
        holder.cartProductTotalPrice.setText(formatCurrency(list.get(position).getTotalPrice()) + "đ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private static String formatCurrency(String originalNumber) {
        int length = originalNumber.length();
        StringBuilder formattedNumber = new StringBuilder(originalNumber);
        if (length >= 4) {
            formattedNumber.insert(length - 3, '.');
            if (length >= 7) {
                formattedNumber.insert(length - 6, '.');
            }
            if (length >= 10) {
                formattedNumber.insert(length - 9, '.');
            }
            return formattedNumber.toString();
        }
        return "";
    }


}
