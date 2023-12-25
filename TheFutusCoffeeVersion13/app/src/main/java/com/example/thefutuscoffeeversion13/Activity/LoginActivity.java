package com.example.thefutuscoffeeversion13.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.thefutuscoffeeversion13.Dialog.LoadingDialog;
import com.example.thefutuscoffeeversion13.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmailLogin, etPasswordLogin;
    private Button btLogin;
    private TextView tvFormSignup;
    private TextView tvFormForgotPassword;
    private FirebaseAuth mAuth;
    private String role;
    private ImageView ivPasswordVisibility;
    private boolean isPasswordVisible = false;
    private LoadingDialog loadingDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btLogin = findViewById(R.id.btLogin);
        tvFormSignup = findViewById(R.id.tvFormSignup);
        tvFormForgotPassword = findViewById(R.id.tvFormForgotPassword);
        ivPasswordVisibility = findViewById(R.id.ivPasswordVisibility);
        loadingDialog = new LoadingDialog(LoginActivity.this);
        mAuth = FirebaseAuth.getInstance();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        tvFormSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(myIntent);
            }
        });
        tvFormForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(myIntent);
            }
        });
        ivPasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
    }

    private void Login() {
        String StrEmail = etEmailLogin.getText().toString();
        String StrPassword = etPasswordLogin.getText().toString();

        if (TextUtils.isEmpty(StrEmail)) {
            Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(StrPassword)) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog.show();

        mAuth.signInWithEmailAndPassword(StrEmail, StrPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //current user
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    String userEmail = currentUser.getEmail();

                    FirebaseFirestore dbUser = FirebaseFirestore.getInstance();
                    dbUser.collection("Users").document(userEmail).collection("Information").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    role = document.getString("role");
                                }
                                checkRole(role);
                            } else {
                                loadingDialog.dismiss();
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            // Show password
            etPasswordLogin.setTransformationMethod(null);
            ivPasswordVisibility.setImageResource(R.drawable.round_visibility_24);
        } else {
            // Hide password
            etPasswordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivPasswordVisibility.setImageResource(R.drawable.round_visibility_off_24);
        }
    }

    private void checkRole(String role) {
        if (role.equals("admin") || role.equals("Nhân viên")) {
            loadingDialog.dismiss();
            Intent myIntent = new Intent(LoginActivity.this, AdminActivity.class);
            startActivity(myIntent);
        } else {
            loadingDialog.dismiss();
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(myIntent);
        }
    }
}