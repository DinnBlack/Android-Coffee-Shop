package com.example.thefutuscoffeeversion13.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.thefutuscoffeeversion13.Adapter.Card1ColAdapter;
import com.example.thefutuscoffeeversion13.Adapter.Card2ColAdapter;
import com.example.thefutuscoffeeversion13.Adapter.CardAdapter;
import com.example.thefutuscoffeeversion13.Dialog.LoadingDialog;
import com.example.thefutuscoffeeversion13.Domain.CardModel;
import com.example.thefutuscoffeeversion13.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ScrollView scrollView;
    private LoadingDialog loadingDialog;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.show();
        setupCardRecyclerViews(view);
        scrollView = view.findViewById(R.id.scrollView);
        setupScrollToPositionListeners(view);
        return view;
    }

    private void setupCardRecyclerViews(View view) {
        // Setup Card Offer RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvOrderCardOffer, "Products/Offer/Offer");

        // Setup New Drink RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvOrderCardNewDrink, "Products/NewDrink/NewDrink");

        // Setup Coffee RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvOrderCardCoffee, "Products/Coffee/Coffee");

        // Setup HiTea RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvOrderCardHiTea, "Products/HiTea/HiTea");

        // Setup MilkTea RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvOrderCardMilkTea, "Products/MilkTea/MilkTea");

        // Setup GreenTea RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvOrderCardGreenTea, "Products/GreenTea/GreenTea");

        // Setup HotDrink RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvOrderCardHotDrink, "Products/HotDrink/HotDrink");

        // Setup SaltineCrackers RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvOrderCardSaltineCrackers, "Products/SaltineCrackers/SaltineCrackers");

        // Setup Cake RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvOrderCardCake, "Products/Cake/Cake");
    }

    private void setupCard2ColRecyclerView(View view, int recyclerViewId, String collectionPath) {
        RecyclerView recyclerView = view.findViewById(recyclerViewId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        List<CardModel> cardModelList = new ArrayList<>();
        Card1ColAdapter card1ColAdapter = new Card1ColAdapter(getActivity(), cardModelList);
        recyclerView.setAdapter(card1ColAdapter);

        db.collection(collectionPath)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CardModel cardModel = document.toObject(CardModel.class);
                            cardModelList.add(cardModel);
                        }
                        card1ColAdapter.notifyDataSetChanged();
                        loadingDialog.dismiss();
                    }
                });
    }

    private void setupScrollToPositionListeners(View view) {
        // New Drink
        setupScrollToPositionListener(view, R.id.ivNewDrink, R.id.tvNewDrink);

        // Coffee
        setupScrollToPositionListener(view, R.id.ivCoffee, R.id.tvCoffee);

        // HiTea
        setupScrollToPositionListener(view, R.id.ivHiTea, R.id.tvHiTea);

        // MilkTea
        setupScrollToPositionListener(view, R.id.ivMilkTea, R.id.tvMilkTea);

        // GreenTea
        setupScrollToPositionListener(view, R.id.ivGreenTea, R.id.tvGreenTea);

        // HotDrink
        setupScrollToPositionListener(view, R.id.ivHotDrink, R.id.tvHotDrink);

        // SaltineCrackers
        setupScrollToPositionListener(view, R.id.ivSaltineCrackers, R.id.tvSaltineCrackers);

        // Cake
        setupScrollToPositionListener(view, R.id.ivCake, R.id.tvCake);
    }

    private void setupScrollToPositionListener(View view, int imageViewId, int textViewId) {
        ImageView imageView = view.findViewById(imageViewId);
        TextView textView = view.findViewById(textViewId);

        imageView.setOnClickListener(v -> scrollToViewPosition(textView));
    }

    private void scrollToViewPosition(View view) {
        int viewPosition = view.getTop();
        scrollView.smoothScrollTo(0, viewPosition);
    }
}