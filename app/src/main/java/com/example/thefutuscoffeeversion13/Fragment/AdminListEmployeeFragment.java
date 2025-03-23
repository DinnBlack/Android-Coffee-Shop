package com.example.thefutuscoffeeversion13.Fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.thefutuscoffeeversion13.Adapter.CardOrderAdapter;
import com.example.thefutuscoffeeversion13.Adapter.EmployeeAdapter;
import com.example.thefutuscoffeeversion13.Dialog.LoadingDialog;
import com.example.thefutuscoffeeversion13.Domain.CardModel;
import com.example.thefutuscoffeeversion13.Domain.OrderModel;
import com.example.thefutuscoffeeversion13.Domain.UserModel;
import com.example.thefutuscoffeeversion13.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminListEmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminListEmployeeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LoadingDialog loadingDialog;
    private String role;

    public AdminListEmployeeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminListEmployeeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminListEmployeeFragment newInstance(String param1, String param2) {
        AdminListEmployeeFragment fragment = new AdminListEmployeeFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_list_employee, container, false);
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.show();

        //load data
        RecyclerView rvAdminListEmployee = view.findViewById(R.id.rvAdminListEmployee);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        rvAdminListEmployee.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        List<UserModel> userModelList = new ArrayList<>();
        EmployeeAdapter employeeAdapter = new EmployeeAdapter(getActivity(), userModelList);
        rvAdminListEmployee.setAdapter(employeeAdapter);
        FirebaseFirestore dbUser = FirebaseFirestore.getInstance();
        db.collection("Users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    String email = document.getString("email");
                    dbUser.collection("Users").document(email).collection("Information").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getString("role").equals("Nhân viên")) {
                                        UserModel userModel = document.toObject(UserModel.class);
                                        userModelList.add(userModel);
                                    }
                                }

                            } else {
                                loadingDialog.dismiss();
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                            employeeAdapter.notifyDataSetChanged();
                        }
                    });
                }
                loadingDialog.dismiss();
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
        return view;
    }
}