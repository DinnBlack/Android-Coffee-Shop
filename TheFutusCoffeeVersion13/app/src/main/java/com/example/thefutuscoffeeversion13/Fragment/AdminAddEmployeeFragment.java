package com.example.thefutuscoffeeversion13.Fragment;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thefutuscoffeeversion13.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminAddEmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAddEmployeeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore dbAccountAdd;
    private ImageView ivAddAccountAvatar;
    private Button btAddAccountAfterFill;
    private TextView tvAddAccountEmail, tvAddAccountPassword, tvAddAccountName, tvAddAccountBirthday, tvAddAccountPhoneNumber;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;

    public AdminAddEmployeeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminAddEmployeeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminAddEmployeeFragment newInstance(String param1, String param2) {
        AdminAddEmployeeFragment fragment = new AdminAddEmployeeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_add_employee, container, false);
        mAuth = FirebaseAuth.getInstance();
        dbAccountAdd = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        ivAddAccountAvatar = view.findViewById(R.id.ivAddAccountAvatar);
        tvAddAccountEmail = view.findViewById(R.id.tvAddAccountEmail);
        tvAddAccountPassword = view.findViewById(R.id.tvAddAccountPassword);
        tvAddAccountName = view.findViewById(R.id.tvAddAccountName);
        tvAddAccountBirthday = view.findViewById(R.id.tvAddAccountBirthday);
        tvAddAccountPhoneNumber = view.findViewById(R.id.tvAddAccountPhoneNumber);
        btAddAccountAfterFill = view.findViewById(R.id.btAddAccountAfterFill);

        ivAddAccountAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(AdminAddEmployeeFragment.this)
                        .crop()
                        .maxResultSize(120, 120)
                        .start();
            }
        });

        tvAddAccountBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        btAddAccountAfterFill.setOnClickListener(v -> {

            String email = tvAddAccountEmail.getText().toString().trim();
            String password = tvAddAccountPassword.getText().toString().trim();
            String name = tvAddAccountName.getText().toString().trim();
            String birthday = tvAddAccountBirthday.getText().toString().trim();
            String phoneNumber = tvAddAccountPhoneNumber.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                tvAddAccountEmail.setError("Chưa nhập email");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                tvAddAccountPassword.setError("Chưa nhập mật khẩu");
                return;
            }
            if (password.length() < 6) {
                tvAddAccountPassword.setError("Mật khẩu phải có từ 6 kí tự trở lên");
                return;
            }
            if (TextUtils.isEmpty(name)) {
                tvAddAccountEmail.setError("Chưa nhập tên");
                return;
            }
            if (TextUtils.isEmpty(birthday)) {
                tvAddAccountPassword.setError("Chưa nhập ngày sinh");
                return;
            }
            if (TextUtils.isEmpty(phoneNumber)) {
                tvAddAccountPassword.setError("Chưa nhập số điện thoại");
                return;
            }

            mAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<String> signInMethods = task.getResult().getSignInMethods();

                            if (signInMethods != null && !signInMethods.isEmpty()) {
                                Toast.makeText(getContext(), "Email address is already in use", Toast.LENGTH_SHORT).show();
                            } else {
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(createTask -> {
                                            if (createTask.isSuccessful()) {
                                                mAuth.signInWithEmailAndPassword("admin@gmail.com", "123456")
                                                        .addOnCompleteListener(signInTask -> {
                                                            if (signInTask.isSuccessful()) {
                                                            } else {
                                                            }
                                                        });
                                                uploadImageAndData();
                                            } else {
                                                Toast.makeText(getContext(), "Không thể đăng kí tài khoản do email đã được đăng kí trước đó!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                        }
                    });
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();
            ivAddAccountAvatar.setImageURI(imageUri);
        }
    }

    private void openDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                //Showing the picked value in the textView
                tvAddAccountBirthday.setText(String.valueOf(day)+ "/"+String.valueOf(month+1)+ "/"+String.valueOf(year));

            }
        }, 2023, 01, 20);

        datePickerDialog.show();
    }

    private void uploadImageAndData() {
        if (imageUri != null) {
            StorageReference imageRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                insertData(downloadUri);
                            } else {
                                Toast.makeText(getContext(), "Failed to get download URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertData(Uri downloadUri) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> items1 = new HashMap<>();
        items1.put("email", tvAddAccountEmail.getText().toString().trim());
        db.collection("Users").document(tvAddAccountEmail.getText().toString().trim())
                .set(items1);
        Map<String, Object> items = new HashMap<>();
        items.put("email", tvAddAccountEmail.getText().toString().trim());
        items.put("name", tvAddAccountName.getText().toString().trim());
        items.put("birthday", tvAddAccountBirthday.getText().toString().trim());
        items.put("phoneNumber", tvAddAccountPhoneNumber.getText().toString().trim());
        items.put("avatar", downloadUri.toString());
        items.put("role", "Nhân viên");

        dbAccountAdd.collection("Users").document(tvAddAccountEmail.getText().toString().trim()).collection("Information").document("information")
                .set(items)
                .addOnCompleteListener(task -> {
                    Toast.makeText(getContext(), "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                });
    }
}