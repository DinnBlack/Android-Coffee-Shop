package com.example.thefutuscoffeeversion13.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.List;

public class CardUserOrderAdapter extends RecyclerView.Adapter<CardUserOrderAdapter.ViewHolder>{

    private Context context;
    private List<OrderModel> list;

    public CardUserOrderAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
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
    public CardUserOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardUserOrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardUserOrderAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(R.drawable.delivery).into(holder.ivImage);
        holder.idOrder.setText(list.get(position).getDaytime());
        holder.dayOrder.setText(list.get(position).getDay());
        holder.totalPriceOrder.setText(list.get(position).getPrice());
        holder.statusOrder.setText(list.get(position).getStatus());
        holder.user.setText(list.get(position).getUser());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCartDialog(Gravity.CENTER, list.get(position).getIdOrder(), list.get(position).getName(), list.get(position).getPhoneNumber(), list.get(position).getAddress(), list.get(position).getPrice(), list.get(position).getDay(), list.get(position).getTime(), list.get(position).getStatus(), list.get(position).getPaymentStatus());
            }
        });
    }

    private void showCartDialog(int gravity, String idOrder, String name, String phoneNumber, String address, String price, String day, String time, String status, String paymentStatus) {

        //Setup show dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_detailed_order);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        dialog.show();

        //find item
        EditText nameReceiver = dialog.findViewById(R.id.nameReceiver);
        EditText phoneNumberReceiver = dialog.findViewById(R.id.phoneNumberReceiver);
        RecyclerView listProductsSelected = dialog.findViewById(R.id.listProductsSelected);
        TextView paymentAmount = dialog.findViewById(R.id.paymentAmount);
        TextView statusOrder = dialog.findViewById(R.id.statusOrder);
        TextView paymentStatusOrder = dialog.findViewById(R.id.paymentStatusOrder);
        TextView dayOrder = dialog.findViewById(R.id.dayOrder);
        TextView timeOrder = dialog.findViewById(R.id.timeOrder);
        ImageView closeDialog = dialog.findViewById(R.id.closeDialog);
        EditText addressReceiver = dialog.findViewById(R.id.addressReceiver);

        //Close Dialog
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        //Current User
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        String userEmail = currentUser.getEmail();

        //fill data user
        assert userEmail != null;

        nameReceiver.setText(name);
        nameReceiver.setEnabled(false);
        phoneNumberReceiver.setText(phoneNumber);
        phoneNumberReceiver.setEnabled(false);
        addressReceiver.setText(address);
        addressReceiver.setEnabled(false);
        paymentAmount.setText(price);
        statusOrder.setText(status);
        paymentStatusOrder.setText(paymentStatus);
        dayOrder.setText(day);
        timeOrder.setText(time);

        //fill data product
        FirebaseFirestore dbSelectedProducts = FirebaseFirestore.getInstance();
        listProductsSelected.setLayoutManager(new GridLayoutManager(context, 1));
        List<CardModel> cardModelList = new ArrayList<>();
        CartDialogAdapter cartDialogAdapter = new CartDialogAdapter(context, cardModelList);
        listProductsSelected.setAdapter(cartDialogAdapter);
        dbSelectedProducts
                .collection("Users").document(userEmail).collection("Order").document(idOrder).collection("Cart")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CardModel cardModel = document.toObject(CardModel.class);
                            cardModelList.add(cardModel);
                        }
                        cartDialogAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void notifyRemovedItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public void updateProcessingStatus(int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        OrderModel orderModel = list.get(position);
        db.collection("Users").document(orderModel.getUser()).collection("Order").document(orderModel.getIdOrder()).update("status", "Đã hủy")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Đơn hàng đã được hủy", Toast.LENGTH_SHORT).show();
                        notifyRemovedItem(position);
                    }
                }).addOnFailureListener(e -> {
                            Toast.makeText(context, "C", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
