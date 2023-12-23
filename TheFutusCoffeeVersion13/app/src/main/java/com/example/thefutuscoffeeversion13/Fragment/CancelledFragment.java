package com.example.thefutuscoffeeversion13.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CancelledFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CancelledFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LoadingDialog loadingDialog;

    public CancelledFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CancelledFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CancelledFragment newInstance(String param1, String param2) {
        CancelledFragment fragment = new CancelledFragment();
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
        View view = inflater.inflate(R.layout.fragment_cancelled, container, false);
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.show();
        //Current User
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        String userEmail = currentUser.getEmail();

        //load data
        RecyclerView rvCancelled = view.findViewById(R.id.rvCancelled);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        rvCancelled.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        List<OrderModel> orderModelList = new ArrayList<>();
        CardOrderAdapter cardOrderAdapter = new CardOrderAdapter(getActivity(), orderModelList);
        rvCancelled.setAdapter(cardOrderAdapter);

        db.collection("Users").document(userEmail).collection("Order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("status").equals("Đã hủy")) {
                                    OrderModel cardModel = document.toObject(OrderModel.class);
                                    orderModelList.add(cardModel);
                                }
                            }
                            cardOrderAdapter.notifyDataSetChanged();
                            loadingDialog.dismiss();
                        }
                    }
                });

        return view;
    }
}