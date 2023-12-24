package com.example.thefutuscoffeeversion13.Fragment;

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
import com.example.thefutuscoffeeversion13.Dialog.LoadingDialog;
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
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminProcessingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminProcessingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LoadingDialog loadingDialog;


    public AdminProcessingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminProcessingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminProcessingFragment newInstance(String param1, String param2) {
        AdminProcessingFragment fragment = new AdminProcessingFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_processing, container, false);
        loadingDialog = new LoadingDialog(getActivity());
//        loadingDialog.show();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        String userEmail = currentUser.getEmail();

        //load data
        RecyclerView rvAdminProcessing = view.findViewById(R.id.rvAdminProcessing);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        rvAdminProcessing.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        List<OrderModel> orderModelList = new ArrayList<>();
        CardOrderAdapter cardOrderAdapter = new CardOrderAdapter(getActivity(), orderModelList);
        rvAdminProcessing.setAdapter(cardOrderAdapter);
        loadOrderHistoryForAllUser(db, userEmail, orderModelList, cardOrderAdapter);


        return view;
    }

    private void loadOrderHistoryForAllUser(FirebaseFirestore db, String userEmail, List<OrderModel> orderModelList, CardOrderAdapter cardOrderAdapter) {
        db.collection("Users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    String email = document.getString("email");


                    db.collection("Users").document(email).collection("Order")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot orderDocument : task.getResult()) {
                                            if (orderDocument.getString("status").equals("Đang xử lý")) {
                                                OrderModel cardModel = orderDocument.toObject(OrderModel.class);
                                                orderModelList.add(cardModel);
                                                cardOrderAdapter.notifyDataSetChanged();
                                            }
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
}