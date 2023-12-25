package com.example.thefutuscoffeeversion13.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thefutuscoffeeversion13.Activity.MainActivity;
import com.example.thefutuscoffeeversion13.Domain.CardModel;
import com.example.thefutuscoffeeversion13.Domain.OrderModel;
import com.example.thefutuscoffeeversion13.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardOrderAdapter extends RecyclerView.Adapter<CardOrderAdapter.ViewHolder>{

    private Context context;
    private List<OrderModel> list;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CardOrderAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }


    public void updateProcessingStatus(int position) {
        OrderModel orderModel = list.get(position);
        db.collection("Users").document(orderModel.getUser()).collection("Order").document(orderModel.getIdOrder()).update("status", "Đang giao hàng")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        notifyRemovedItem(position);
                    }
                }).addOnFailureListener(e -> {
            Toast.makeText(context, "C", Toast.LENGTH_SHORT).show();
                }
        );
    }

    public void updateDeliveringStatus(int position) {
        OrderModel orderModel = list.get(position);
        db.collection("Users").document(orderModel.getUser()).collection("Order").document(orderModel.getIdOrder()).update("status", "Đã giao")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        notifyRemovedItem(position);
                    }
                }).addOnFailureListener(e -> {
                            Toast.makeText(context, "C", Toast.LENGTH_SHORT).show();
                        }
                );
    }


    private void notifyRemovedItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView idOrder, dayOrder, totalPriceOrder, statusOrder, user;
        ImageView ivImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            idOrder = itemView.findViewById(R.id.idOrder);
            dayOrder = itemView.findViewById(R.id.dayOrder);
            totalPriceOrder = itemView.findViewById(R.id.totalPriceOrder);
            statusOrder = itemView.findViewById(R.id.statusOrder);
            user = itemView.findViewById(R.id.user);
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
        holder.user.setText(list.get(position).getUser());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
