package com.example.thefutuscoffeeversion13.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thefutuscoffeeversion13.Adapter.CardOrderAdapter;
import com.example.thefutuscoffeeversion13.Domain.OrderModel;
import com.example.thefutuscoffeeversion13.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminRevenueActivity extends AppCompatActivity {
    private int revenue = 0;
    private int count = 0;
    private TextView doanhThuNgay, donHangNgay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_revenue);
        doanhThuNgay = findViewById(R.id.doanhThuNgay);
        donHangNgay = findViewById(R.id.donHangNgay);
        loadData();
    }

    private void loadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestore dbUser = FirebaseFirestore.getInstance();
        db.collection("Users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    String email = document.getString("email");
                    dbUser.collection("Users").document(email).collection("Order")
                            .get()
                            .addOnCompleteListener(userOrderTaskResult -> {
                                if (userOrderTaskResult.isSuccessful()) {
                                    for (QueryDocumentSnapshot orderDocument : Objects.requireNonNull(userOrderTaskResult.getResult())) {
                                        if (orderDocument.getString("status").equals("Đã giao")) {
                                            revenue += Integer.parseInt(removeLastCharacter(removeCurrencyFormat(orderDocument.getString("price"))));
                                            count++;
                                            doanhThuNgay.setText(formatCurrency(String.valueOf(revenue)) + "đ");
                                            donHangNgay.setText(String.valueOf(count) + " Đơn");
                                        }
                                    }

                                }
                            });
                }


            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });


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

    public static String removeLastCharacter(String str) {
        if (str != null && str.length() > 0) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    private static String removeCurrencyFormat(String formattedNumber) {
        return formattedNumber.replace(".", "");
    }
}