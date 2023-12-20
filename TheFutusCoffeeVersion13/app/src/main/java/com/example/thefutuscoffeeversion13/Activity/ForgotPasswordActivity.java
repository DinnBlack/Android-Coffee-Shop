package com.example.thefutuscoffeeversion13.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thefutuscoffeeversion13.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmailForgotPassword;
    private Button btResetPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etEmailForgotPassword = findViewById(R.id.etEmailForgotPassword);
        btResetPassword = findViewById(R.id.btResetPassword);
        mAuth = FirebaseAuth.getInstance();

        btResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPassword();
            }
        });
    }

    private void ResetPassword() {
        String StrEmail = etEmailForgotPassword.getText().toString();
        if (!TextUtils.isEmpty(StrEmail)) {
            mAuth.sendPasswordResetEmail(StrEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(ForgotPasswordActivity.this, "Link đặt lại mật khẩu đã được gửi đến email của bạn!", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ForgotPasswordActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}