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
import com.example.thefutuscoffeeversion13.Domain.OrderModel;
import com.example.thefutuscoffeeversion13.Domain.UserModel;
import com.example.thefutuscoffeeversion13.R;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder>{

    private Context context;
    private List<UserModel> list;

    public EmployeeAdapter(Context context, List<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView employeeName, employeeEmail, employeePhoneNumber, employeeBirthday;
        ImageView employeeAvatar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeAvatar = itemView.findViewById(R.id.employeeAvatar);
            employeeName = itemView.findViewById(R.id.employeeName);
            employeeEmail = itemView.findViewById(R.id.employeeEmail);
            employeePhoneNumber = itemView.findViewById(R.id.employeePhoneNumber);
            employeeBirthday = itemView.findViewById(R.id.employeeBirthday);
        }
    }

    @NonNull
    @Override
    public EmployeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmployeeAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_employee, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getAvatar()).into(holder.employeeAvatar);
        holder.employeeName.setText(list.get(position).getName());
        holder.employeeEmail.setText(list.get(position).getEmail());
        holder.employeePhoneNumber.setText(list.get(position).getPhoneNumber());
        holder.employeeBirthday.setText(list.get(position).getBirthday());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
