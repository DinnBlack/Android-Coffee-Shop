package com.example.thefutuscoffeeversion13.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.thefutuscoffeeversion13.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class AdminActivity extends AppCompatActivity {
    private TextView currentTimeOfDay, tvCurrentAccountName;
    private String role;
    private CardView btListProducts, btListAccounts, btListRevenue, cardViewOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        currentTimeOfDay = findViewById(R.id.currentTimeOfDay);
        tvCurrentAccountName = findViewById(R.id.tvCurrentAccountName);
        btListProducts = findViewById(R.id.btListProducts);
        btListAccounts = findViewById(R.id.btListAccounts);
        btListRevenue = findViewById(R.id.btListRevenue);
        cardViewOrder = findViewById(R.id.cardViewOrder);

        cardViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, AdminOrderActivity.class));
            }
        });
        currentTime();
        checkRole();
    }

    private void currentTime () {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (hourOfDay >= 0 && hourOfDay < 12) {
            currentTimeOfDay.setText("Chào buổi sáng");
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            currentTimeOfDay.setText("Chào buổi trưa");
        } else {
            currentTimeOfDay.setText("Chào buổi tối");
        }
    }

    private void checkRole() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userEmail = currentUser.getEmail();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users").document(userEmail).collection("Information").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        role = document.getString("role");
                    }
                    if (role.equals("admin")) {
                        tvCurrentAccountName.setText("quản trị viên");
                    } else {
                        tvCurrentAccountName.setText("nhân viên");
                        btListProducts.setVisibility(View.INVISIBLE);
                        btListAccounts.setVisibility(View.INVISIBLE);
                        btListRevenue.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}