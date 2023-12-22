package com.example.thefutuscoffeeversion13.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thefutuscoffeeversion13.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmailSignup, etPasswordSignup, etPasswordConfirmSignup;
    private Button btSignup;
    private FirebaseAuth mAuth;
    private ImageView ivPasswordVisibility;
    private boolean isPasswordVisible = true;
    private ImageView ivConfirmPasswordVisibility;
    private boolean isConfirmPasswordVisible = true;
    private TextView tvBackLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etEmailSignup = findViewById(R.id.etEmailSignup);
        etPasswordSignup = findViewById(R.id.etPasswordSignup);
        etPasswordConfirmSignup = findViewById(R.id.etPasswordConfirmSignup);
        btSignup = findViewById(R.id.btSignup);
        mAuth = FirebaseAuth.getInstance();
        ivPasswordVisibility = findViewById(R.id.ivPasswordVisibility);
        ivConfirmPasswordVisibility = findViewById(R.id.ivConfirmPasswordVisibility);
        tvBackLogin = findViewById(R.id.tvBackLogin);

        ivPasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
        ivConfirmPasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleConfirmPasswordVisibility();
            }
        });

        tvBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            // Show password
            etPasswordSignup.setTransformationMethod(null);
            ivPasswordVisibility.setImageResource(R.drawable.round_visibility_24);
        } else {
            // Hide password
            etPasswordSignup.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivPasswordVisibility.setImageResource(R.drawable.round_visibility_off_24);
        }
    }

    private void toggleConfirmPasswordVisibility() {
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
        if (isConfirmPasswordVisible) {
            // Show password
            etPasswordConfirmSignup.setTransformationMethod(null);
            ivConfirmPasswordVisibility.setImageResource(R.drawable.round_visibility_24);
        } else {
            // Hide password
            etPasswordConfirmSignup.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivConfirmPasswordVisibility.setImageResource(R.drawable.round_visibility_off_24);
        }
    }

    private void Register () {
        String StrEmail = etEmailSignup.getText().toString();
        String StrPassword = etPasswordSignup.getText().toString();
        String StrPasswordConfirm = etPasswordConfirmSignup.getText().toString();

        if (TextUtils.isEmpty(StrEmail)) {
            Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(StrPassword)) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(StrPasswordConfirm)) {
            Toast.makeText(this, "Vui lòng xác nhận lại mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StrPassword.equals(StrPasswordConfirm)) {
            Toast.makeText(this, "Xác nhận mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(StrEmail, StrPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> items = new HashMap<>();
                    items.put("role", "customer");
                    db.collection("Users").document(etEmailSignup.getText().toString().trim()).collection("Information").document("information")
                            .set(items);
                    Toast.makeText(getApplicationContext(), "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Không thể đăng kí tài khoản do email đã được đăng kí trước đó!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}