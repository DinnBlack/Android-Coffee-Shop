package com.example.thefutuscoffeeversion13.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
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

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Card RecyclerView
    private ScrollView scrollView;
    private TextView btTextDialog;
    private LoadingDialog loadingDialog;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.show();
        scrollView = view.findViewById(R.id.scHome);
        initializeViews(view);
        setupImageSlider(view);
        setupCardRecyclerViews(view);
        setupScrollToPositionListeners(view);
        setupCardDialog(view);

        return view;
    }

    private void initializeViews(View view) {
        // Initialize your views here
    }

    private void setupImageSlider(View view) {
        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slider1, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.slider2, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.slider3, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.slider4, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.slider5, ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(slideModels);
    }

    private void setupCardRecyclerViews(View view) {
        // Setup Card Offer RecyclerView
        setupCardRecyclerView(view, R.id.rvCardOffer, "Products/Offer/Offer");

        // Setup New Drink RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvCardNewDrink, "Products/NewDrink/NewDrink");

        // Setup Coffee RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvCardCoffee, "Products/Coffee/Coffee");

        // Setup HiTea RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvCardHiTea, "Products/HiTea/HiTea");

        // Setup MilkTea RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvCardMilkTea, "Products/MilkTea/MilkTea");

        // Setup GreenTea RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvCardGreenTea, "Products/GreenTea/GreenTea");

        // Setup HotDrink RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvCardHotDrink, "Products/HotDrink/HotDrink");

        // Setup SaltineCrackers RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvCardSaltineCrackers, "Products/SaltineCrackers/SaltineCrackers");

        // Setup Cake RecyclerView
        setupCard2ColRecyclerView(view, R.id.rvCardCake, "Products/Cake/Cake");
    }

    private void setupCardRecyclerView(View view, int recyclerViewId, String collectionPath) {
        RecyclerView recyclerView = view.findViewById(recyclerViewId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        List<CardModel> cardModelList = new ArrayList<>();
        CardAdapter cardAdapter = new CardAdapter(getActivity(), cardModelList);
        recyclerView.setAdapter(cardAdapter);

        db.collection(collectionPath)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CardModel cardModel = document.toObject(CardModel.class);
                            cardModelList.add(cardModel);
                        }
                        cardAdapter.notifyDataSetChanged();
                        loadingDialog.dismiss();
                    }
                });
    }

    private void setupCard2ColRecyclerView(View view, int recyclerViewId, String collectionPath) {
        RecyclerView recyclerView = view.findViewById(recyclerViewId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpaceItemDecoration(dpToPx(8)));

        List<CardModel> cardModelList = new ArrayList<>();
        Card2ColAdapter card2ColAdapter = new Card2ColAdapter(getActivity(), cardModelList);
        recyclerView.setAdapter(card2ColAdapter);

        db.collection(collectionPath)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CardModel cardModel = document.toObject(CardModel.class);
                            cardModelList.add(cardModel);
                        }
                        card2ColAdapter.notifyDataSetChanged();
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

    private void setupCardDialog(View view) {
        btTextDialog = view.findViewById(R.id.btTextDialog);
        btTextDialog.setOnClickListener(v -> showCardDialog(Gravity.CENTER));
    }

    private void showCardDialog(int gravity) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_click_items);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = gravity;
            window.setAttributes(windowAttributes);
        }

        dialog.show();
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % 2;
            if (column == 0) {
                outRect.right = space;
            }
            if (column == 1) {
                outRect.left = space;
            }
        }
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
