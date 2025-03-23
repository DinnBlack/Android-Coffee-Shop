package com.example.thefutuscoffeeversion13.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thefutuscoffeeversion13.Domain.CardModel;
import com.example.thefutuscoffeeversion13.Domain.OrderModel;
import com.example.thefutuscoffeeversion13.Domain.ToppingModel;
import com.example.thefutuscoffeeversion13.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Card2ColAdapter extends RecyclerView.Adapter<Card2ColAdapter.ViewHolder> {

    private Context context;
    private List<CardModel> list;
    private String selectedToppingTitle;
    private ToppingAdapter toppingAdapter;
    private List<ToppingModel> addToppingList = new ArrayList<>();
    private String selectedSize = "Vừa";

    public Card2ColAdapter(Context context, List<CardModel> list) {
        this.context = context;
        this.list = list;
        this.toppingAdapter = new ToppingAdapter(context, new ArrayList<>());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_list_2col, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.ivImage);
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvPrice.setText(formatCurrency(list.get(position).getPrice()) + "đ");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardDialog(Gravity.CENTER, list.get(position));
            }
        });
    }

    private void showCardDialog(int gravity, CardModel cardModel) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_click_items);

        ScrollView scrollView = dialog.findViewById(R.id.scrollView2);
        scrollView.fullScroll(ScrollView.FOCUS_UP);

        ImageView closeDialogItem = dialog.findViewById(R.id.closeDialogItem);
        ImageView imgItemDialog = dialog.findViewById(R.id.imgItemDialog);
        TextView tvTitleItemDialog = dialog.findViewById(R.id.tvTitleItemDialog);
        TextView tvPriceItemDialog = dialog.findViewById(R.id.tvPriceItemDialog);
        TextView tvDescriptionItemDialog = dialog.findViewById(R.id.tvDescriptionItemDialog);
        ImageView removeQuantity = dialog.findViewById(R.id.removeQuantity);
        ImageView addQuantity = dialog.findViewById(R.id.addQuantity);
        TextView quantityOfItemCard = dialog.findViewById(R.id.quantityOfItemCard);
        TextView tvTotalPrice = dialog.findViewById(R.id.tvTotalPrice);
        CardView btAddProductToCard = dialog.findViewById(R.id.btAddProductToCard);

        closeDialogItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        Glide.with(context)
                .load(cardModel.getImg_url())
                .placeholder(R.drawable.maycoffee)
                .into(imgItemDialog);
        tvTitleItemDialog.setText(cardModel.getTitle());
        tvPriceItemDialog.setText(formatCurrency(cardModel.getPrice()) + "đ");
        tvDescriptionItemDialog.setText(cardModel.getDescription());
        tvTotalPrice.setText(tvPriceItemDialog.getText().toString().trim());

        //show data topping
        if (cardModel.getTopping().equals("Coffee")) {
            showToppingDialog(dialog, toppingAdapter, "Coffee");
        }
        if (cardModel.getTopping().equals("GreenTea")) {
            showToppingDialog(dialog, toppingAdapter, "GreenTea");
        }
        if (cardModel.getTopping().equals("HiTea")) {
            showToppingDialog(dialog, toppingAdapter, "HiTea");
        }
        if (cardModel.getTopping().equals("HotDrink")) {
            showToppingDialog(dialog, toppingAdapter, "HotDrink");
        }
        if (cardModel.getTopping().equals("MilkTea")) {
            showToppingDialog(dialog, toppingAdapter, "MilkTea");
        }


        //Set Price Size
        int productPrice = Integer.parseInt(cardModel.getPrice().toString().trim());

        TextView tvPriceSizeLarge = dialog.findViewById(R.id.tvPriceSizeLarge);
        TextView tvPriceSizeMedium = dialog.findViewById(R.id.tvPriceSizeMedium);
        TextView tvPriceSizeSmall = dialog.findViewById(R.id.tvPriceSizeSmall);

        tvPriceSizeLarge.setText(formatCurrency(String.valueOf(productPrice + 10000)));
        tvPriceSizeMedium.setText(formatCurrency(String.valueOf(productPrice)));
        tvPriceSizeSmall.setText(formatCurrency(String.valueOf(productPrice - 10000)));

        //set check radio button
        RadioButton radioLarge = dialog.findViewById(R.id.radioLarge);
        RadioButton radioMedium = dialog.findViewById(R.id.radioMedium);
        RadioButton radioSmall = dialog.findViewById(R.id.radioSmall);
        radioMedium.setChecked(true);

        ToppingModel[] selectedItem = new ToppingModel[1];
        int[] productSizePrice = {0};

        toppingAdapter.setOnItemCheckedListener(item -> {
            updateTotalPrice(productSizePrice, quantityOfItemCard, tvTotalPrice, toppingAdapter);
            selectedItem[0] = item;
        });

        radioLarge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                productSizePrice[0] = Integer.parseInt(removeCurrencyFormat(tvPriceSizeLarge.getText().toString().trim()));
                updateTotalPrice(productSizePrice, quantityOfItemCard, tvTotalPrice, toppingAdapter);
                selectedSize = "Lớn";
            }
        });

        radioMedium.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                productSizePrice[0] = Integer.parseInt(removeCurrencyFormat(tvPriceSizeMedium.getText().toString().trim()));
                updateTotalPrice(productSizePrice, quantityOfItemCard, tvTotalPrice, toppingAdapter);
                selectedSize = "Vừa";
            }
        });

        radioSmall.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                productSizePrice[0] = Integer.parseInt(removeCurrencyFormat(tvPriceSizeSmall.getText().toString().trim()));
                updateTotalPrice(productSizePrice, quantityOfItemCard, tvTotalPrice, toppingAdapter);
                selectedSize = "Nhỏ";
            }
        });

        //Tang giam so luong san pham (Dang loi logic o phan select radio)

        String priceText = removeCurrencyFormat(tvPriceItemDialog.getText().toString().trim());
        priceText = priceText.substring(0, priceText.length() - 1);

        removeQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int quantityValue = Integer.parseInt(quantityOfItemCard.getText().toString());
                if (quantityValue > 1) {
                    quantityValue--;
                    quantityOfItemCard.setText(String.valueOf(quantityValue));
                    updateTotalPrice(productSizePrice, quantityOfItemCard, tvTotalPrice, toppingAdapter);
                } else {
                    removeQuantity.setEnabled(false);
                }
            }
        });

        addQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int quantityValue = Integer.parseInt(quantityOfItemCard.getText().toString());
                quantityValue++;
                quantityOfItemCard.setText(String.valueOf(quantityValue));
                updateTotalPrice(productSizePrice, quantityOfItemCard, tvTotalPrice, toppingAdapter);
                removeQuantity.setEnabled(true);
            }
        });

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        dialog.show();

        FirebaseFirestore dbCard = FirebaseFirestore.getInstance();
        btAddProductToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String userEmail = currentUser.getEmail();

                if (currentUser != null) {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference accountsRef = db.collection("Users");
                    Query query = accountsRef.whereEqualTo("email", userEmail);

                    FirebaseFirestore dbList = FirebaseFirestore.getInstance();
                    CollectionReference listCard = dbList.collection("Users").document(userEmail).collection("Card");
                    String idValue = tvTitleItemDialog.getText().toString() + selectedSize + selectedToppingTitle;
                    Query queryList = listCard.whereEqualTo("id", idValue);
                    queryList.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int newQuantity = Integer.parseInt(quantityOfItemCard.getText().toString().trim());
                                int oldQuantity = 0;
                                int oldTotalPrice = 0;
                                int newTotalPrice = Integer.parseInt(removeCurrencyFormat(tvTotalPrice.getText().toString().trim()).substring(0, removeCurrencyFormat(tvTotalPrice.getText().toString().trim()).length() - 1));
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    oldQuantity = Integer.parseInt(document.getString("quantity"));
                                    oldTotalPrice = Integer.parseInt(document.getString("totalprice"));
                                }
                                String totalQuantity = String.valueOf(newQuantity + oldQuantity);
                                String totalPrice = String.valueOf(newTotalPrice + oldTotalPrice);

                                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        String userEmail = currentUser.getEmail();
                                        String selectedToppingTitle = toppingAdapter.getSelectedToppingTitle();
                                        Map<String, Object> items = new HashMap<>();
                                        items.put("title", tvTitleItemDialog.getText().toString().trim());
                                        items.put("price", tvPriceItemDialog.getText().toString().trim());
                                        items.put("size", selectedSize);
                                        items.put("quantity", totalQuantity.toString());
                                        items.put("topping", selectedToppingTitle);
                                        items.put("totalprice", totalPrice.toString());
                                        items.put("id", idValue);
                                        String documentCardName = tvTitleItemDialog.getText().toString().trim() + selectedSize + selectedToppingTitle;
                                        dbCard.collection("Users").document(userEmail).collection("Card").document(documentCardName)
                                                .set(items)
                                                .addOnCompleteListener(innerTask -> {
                                                    Toast.makeText(context, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                });
                                    }
                                });
                            } else {
                                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        String userEmail = currentUser.getEmail();
                                        String selectedToppingTitle = toppingAdapter.getSelectedToppingTitle();
                                        Map<String, Object> items = new HashMap<>();
                                        items.put("title", tvTitleItemDialog.getText().toString().trim());
                                        items.put("price", tvPriceItemDialog.getText().toString().trim());
                                        items.put("quantity", quantityOfItemCard.getText().toString().trim());
                                        items.put("size", selectedSize);
                                        items.put("topping", selectedToppingTitle);
                                        items.put("id", idValue);
                                        items.put("totalprice", removeCurrencyFormat(tvTotalPrice.getText().toString().trim()).substring(0, removeCurrencyFormat(tvTotalPrice.getText().toString().trim()).length() - 1));
                                        String documentCardName = tvTitleItemDialog.getText().toString().trim() + selectedToppingTitle;
                                        dbCard.collection("Users").document(userEmail).collection("Card").document(documentCardName)
                                                .set(items)
                                                .addOnCompleteListener(innerTask -> {
                                                    Toast.makeText(context, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                });
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateTotalPrice(int[] productSizePrice, TextView quantityOfItemCard, TextView tvTotalPrice, ToppingAdapter toppingAdapter) {
        int quantityValue = Integer.parseInt(quantityOfItemCard.getText().toString().trim());
        int total;

        boolean isToppingSelected = false;
        for (ToppingModel topping : toppingAdapter.getToppingList()) {
            if (topping.isSelected()) {
                isToppingSelected = true;
                break;
            }
        }

        if (isToppingSelected) {
            total = (productSizePrice[0] + 10000) * quantityValue;
        } else {
            total = productSizePrice[0] * quantityValue;
        }

        tvTotalPrice.setText(formatCurrency(String.valueOf(total)) + "đ");
    }

    private static String formatCurrency(String originalNumber) {
        int length = originalNumber.length();
        StringBuilder formattedNumber = new StringBuilder(originalNumber);
        if (length >= 4) {
            formattedNumber.insert(length - 3, '.');
            if (length >= 7) {
                formattedNumber.insert(length - 6, '.');
            }
            if (length >= 10) {
                formattedNumber.insert(length - 9, '.');
            }
            return formattedNumber.toString();
        }
        return "";
    }

    private static String removeCurrencyFormat(String formattedNumber) {
        return formattedNumber.replace(".", "");
    }

    private void showToppingDialog(Dialog dialog, ToppingAdapter toppingAdapter, String collection) {
        ListView toppingList = dialog.findViewById(R.id.toppingList);
        toppingAdapter = new ToppingAdapter(context, addToppingList);
        toppingList.setAdapter(toppingAdapter);

        FirebaseFirestore dbTopping = FirebaseFirestore.getInstance();
        ToppingAdapter finalToppingAdapter = toppingAdapter;
        dbTopping.collection("Products").document("Topping").collection(collection)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        addToppingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String price = document.getString("price");

                            addToppingList.add(new ToppingModel(title, price));
                        }
                        finalToppingAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("Card2ColAdapter", "Error getting topping data", task.getException());
                    }
                });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
