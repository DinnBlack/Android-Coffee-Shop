package com.example.thefutuscoffeeversion13.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.thefutuscoffeeversion13.Dialog.LoadingDialog;
import com.example.thefutuscoffeeversion13.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private Toolbar tbEditProfile;
    private ImageView avatar, save;
    private TextView birthday;
    private EditText name, phoneNumber, address, email;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private LoadingDialog loadingDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        //find item
        tbEditProfile = findViewById(R.id.tbEditProfile);
        avatar = findViewById(R.id.avatar);
        birthday = findViewById(R.id.birthday);
        name = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        save = findViewById(R.id.save);

        //setup Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //setup toolbar
        setSupportActionBar(tbEditProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Current User
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        String userEmail = currentUser.getEmail();

        //fill data
        FirebaseFirestore dbUser = FirebaseFirestore.getInstance();
        assert userEmail != null;
        dbUser.collection("Users").document(userEmail).collection("Information").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Glide.with(getApplicationContext()).load(document.getString("avatar")).into(avatar);
                        name.setText(document.getString("name"));
                        phoneNumber.setText(document.getString("phoneNumber"));
                        address.setText(document.getString("address"));
                        birthday.setText(document.getString("birthday"));
                    }
                    loadingDialog.dismiss();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        email.setText(userEmail);
        email.setEnabled(false);

        //pick image
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(EditProfileActivity.this)
                        .crop()
                        .maxResultSize(120, 120)
                        .start();
            }
        });

        //pick birthday
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        //Save data
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageAndData();
            }
        });
    }

    private void openDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                birthday.setText(String.valueOf(day)+ "/"+String.valueOf(month+1)+ "/"+String.valueOf(year));

            }
        }, 2023, 01, 20);

        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        avatar.setImageURI(imageUri);
    }

    private void uploadImageAndData() {
        if (imageUri != null) {
            StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                updateData(downloadUri);
                            } else {
                                Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            updateDataNoImage();
        }
    }

    private void updateData(Uri downloadUri) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userEmail = currentUser.getEmail();
        FirebaseFirestore dbOrder = FirebaseFirestore.getInstance();
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", name.getText().toString().trim());
        updatedData.put("phoneNumber", phoneNumber.getText().toString().trim());
        updatedData.put("address", address.getText().toString().trim());
        updatedData.put("email", email.getText().toString().trim());
        updatedData.put("birthday", birthday.getText().toString().trim());
        updatedData.put("avatar", downloadUri.toString());
        dbOrder.collection("Users").document(userEmail).collection("Information").document("information")
                .update(updatedData)
                .addOnCompleteListener(innerTask -> {
                    Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(this, MainActivity.class);
                    startActivity(myIntent);
                });
    }

    private void updateDataNoImage() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userEmail = currentUser.getEmail();
        FirebaseFirestore dbOrder = FirebaseFirestore.getInstance();
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", name.getText().toString().trim());
        updatedData.put("phoneNumber", phoneNumber.getText().toString().trim());
        updatedData.put("address", address.getText().toString().trim());
        updatedData.put("email", email.getText().toString().trim());
        updatedData.put("birthday", birthday.getText().toString().trim());
        dbOrder.collection("Users").document(userEmail).collection("Information").document("information")
                .update(updatedData)
                .addOnCompleteListener(innerTask -> {
                    Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(this, MainActivity.class);
                    startActivity(myIntent);
                });
    }
}