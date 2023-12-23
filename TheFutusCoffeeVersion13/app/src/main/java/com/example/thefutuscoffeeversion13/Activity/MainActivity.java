package com.example.thefutuscoffeeversion13.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thefutuscoffeeversion13.Adapter.Card2ColAdapter;
import com.example.thefutuscoffeeversion13.Adapter.CartDialogAdapter;
import com.example.thefutuscoffeeversion13.Api.CreateOrder;
import com.example.thefutuscoffeeversion13.Domain.CardModel;
import com.example.thefutuscoffeeversion13.Domain.ToppingModel;
import com.example.thefutuscoffeeversion13.Fragment.DiscountFragment;
import com.example.thefutuscoffeeversion13.Fragment.HomeFragment;
import com.example.thefutuscoffeeversion13.Fragment.OrderFragment;
import com.example.thefutuscoffeeversion13.Fragment.OtherFragment;
import com.example.thefutuscoffeeversion13.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import nl.joery.animatedbottombar.AnimatedBottomBar;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class MainActivity extends AppCompatActivity {
    private HomeFragment homeFragment = new HomeFragment();
    private OrderFragment orderFragment = new OrderFragment();
    private DiscountFragment discountFragment = new DiscountFragment();
    private OtherFragment otherFragment = new OtherFragment();

    //Momo
    private String amount = "10000";
    private String fee = "0";
    int environment = 0;
    private String merchantName = "Thanh toán đơn hàng";
    private String merchantCode = "SCB01";
    private String merchantNameLabel = "The Futus Coffee";
    private String description = "Thanh toán đơn hàng tại The Futus Coffee";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        Button showCart = findViewById(R.id.showCart);
        showCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCartDialog(Gravity.CENTER);
            }
        });

        AnimatedBottomBar bottomBar = findViewById(R.id.botnavBar);
        replaceFragment(new HomeFragment());
        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                if (tab1.getId() == R.id.action_home) {
                    replaceFragment(new HomeFragment());
                } else if (tab1.getId() == R.id.action_order) {
                    replaceFragment(new OrderFragment());
                } else if (tab1.getId() == R.id.action_discount) {
                    replaceFragment(new DiscountFragment());
                } else if (tab1.getId() == R.id.action_other) {
                    replaceFragment(new OtherFragment());
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }

        });
    }

    private void requestZalo() {
        CreateOrder orderApi = new CreateOrder();

        try {
            JSONObject data = orderApi.createOrder("10000");
            String code = data.getString("return_code");
            Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();

            if (code.equals("1")) {

                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(MainActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Payment Success")
                                        .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setNegativeButton("Cancel", null).show();
                            }

                        });
                    }

                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("User Cancel Payment")
                                .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Cancel", null).show();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Payment Fail")
                                .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Cancel", null).show();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void showCartDialog(int gravity) {

        //Setup show dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_cart);
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

        //find item
        EditText nameReceiver = dialog.findViewById(R.id.nameReceiver);
        EditText phoneNumberReceiver = dialog.findViewById(R.id.phoneNumberReceiver);
        RecyclerView listProductsSelected = dialog.findViewById(R.id.listProductsSelected);
        TextView totalPriceAllProductsSelected = dialog.findViewById(R.id.totalPriceAllProductsSelected);
        TextView deliveryCharges = dialog.findViewById(R.id.deliveryCharges);
        TextView paymentAmount = dialog.findViewById(R.id.paymentAmount);
        Spinner payment = dialog.findViewById(R.id.payment);
        ImageView closeDialog = dialog.findViewById(R.id.closeDialog);
        CardView clearCart = dialog.findViewById(R.id.clearCart);
        CardView addProducts = dialog.findViewById(R.id.addProducts);
        CardView btPay = dialog.findViewById(R.id.btPay);
        EditText addressReceiver = dialog.findViewById(R.id.addressReceiver);

        //Close Dialog
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        //Add Products
        addProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        //Set Spinner payment
        String[] valueSpinner = {"Thanh toán khi nhận hàng", "Momo"};
        ArrayList<String> arrayListSpinner = new ArrayList<>(Arrays.asList(valueSpinner));
        ArrayAdapter<String> arrayAdapterSpinner = new ArrayAdapter<>(this, R.layout.style_spinner, arrayListSpinner);
        payment.setAdapter(arrayAdapterSpinner);

        //Current User
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        String userEmail = currentUser.getEmail();

        //fill data user
        FirebaseFirestore dbUser = FirebaseFirestore.getInstance();
        assert userEmail != null;
        dbUser.collection("Users").document(userEmail).collection("Information").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        nameReceiver.setText(document.getString("name"));
                        phoneNumberReceiver.setText(document.getString("phoneNumber"));
                        addressReceiver.setText(document.getString("address"));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        //fill data product
        int[] totalPrice = {0};
        FirebaseFirestore dbSelectedProducts = FirebaseFirestore.getInstance();
        listProductsSelected.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        List<CardModel> cardModelList = new ArrayList<>();
        CartDialogAdapter cartDialogAdapter = new CartDialogAdapter(MainActivity.this, cardModelList);
        listProductsSelected.setAdapter(cartDialogAdapter);
        dbSelectedProducts
                .collection("Users").document(userEmail).collection("Card")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CardModel cardModel = document.toObject(CardModel.class);
                            cardModelList.add(cardModel);
                            int itemTotalPrice = Integer.parseInt(document.getString("totalprice"));
                            totalPrice[0] += itemTotalPrice;
                        }

                        if (cardModelList.isEmpty()) {
                            totalPriceAllProductsSelected.setText("0đ");
                            paymentAmount.setText("0đ");
                        } else {
                            updateTotalPrice(totalPrice[0], totalPriceAllProductsSelected);
                            String deliveryChargesText = removeLastCharacter(removeCurrencyFormat(deliveryCharges.getText().toString()));
                            int deliveryChargesValue = Integer.parseInt(deliveryChargesText);
                            int totalPriceAllProductsSelectedValue = 0;
                            if (!totalPriceAllProductsSelected.getText().toString().equals("")) {
                                String totalPriceText = removeLastCharacter(removeCurrencyFormat(totalPriceAllProductsSelected.getText().toString()));
                                totalPriceAllProductsSelectedValue = Integer.parseInt(totalPriceText);
                            }
                            paymentAmount.setText(formatCurrency(String.valueOf(deliveryChargesValue + totalPriceAllProductsSelectedValue)) + "đ");
                        }

                        cartDialogAdapter.notifyDataSetChanged();
                    }
                });

        //Clear Cart
        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(userEmail);
            }
        });

        //Pay
        btPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmail;
                String phone = phoneNumberReceiver.getText().toString();
                requestZalo();
                order(dialog, userEmail, paymentAmount, nameReceiver, phoneNumberReceiver, addressReceiver);
                deleteAllDataInCardCollection(userEmail);
            }
        });

    }

    private void order (Dialog dialog, String userEmail, TextView paymentAmount, EditText nameReceiver, EditText phoneNumberReceiver, EditText addressReceiver) {
        //Current time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date currentDate = calendar.getTime();
        String formattedDateTime = dateFormat.format(currentDate).replaceAll("[/:\\s]", "");
        String idOrder = userEmail + formattedDateTime;

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dayFormat.format(currentDate);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = timeFormat.format(currentDate);

        //check
        if (TextUtils.isEmpty(nameReceiver.getText().toString())) {
            Toast.makeText(this, "Chưa nhập tên người nhận", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phoneNumberReceiver.getText().toString())) {
            Toast.makeText(this, "Chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(addressReceiver.getText().toString())) {
            Toast.makeText(this, "Chưa nhập địa chỉ nhận hàng", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("0đ".equals(paymentAmount.getText().toString().trim())) {
            Toast.makeText(this, "Chưa có sản phẩm nào được thêm", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore dbCart = FirebaseFirestore.getInstance();
        dbCart.collection("Users").document(userEmail).collection("Card")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> cartItems = new HashMap<>();
                            cartItems.put("title", document.getString("title"));
                            cartItems.put("size", document.getString("size"));
                            cartItems.put("quantity", document.getString("quantity"));
                            cartItems.put("topping", document.getString("topping"));
                            cartItems.put("totalprice", document.getString("totalprice"));
                            db.collection("Users").document(userEmail).collection("Order").document(idOrder).collection("Cart").document()
                                    .set(cartItems)
                                    .addOnCompleteListener(innerTask -> {
                                    });
                        }
                    }
                });

        FirebaseFirestore dbOrder = FirebaseFirestore.getInstance();
        Map<String, Object> items = new HashMap<>();
        items.put("idOrder", idOrder);
        items.put("price", paymentAmount.getText().toString());
        items.put("name", nameReceiver.getText().toString());
        items.put("phoneNumber", phoneNumberReceiver.getText().toString());
        items.put("address", addressReceiver.getText().toString());
        items.put("day", formattedDate);
        items.put("time", formattedTime);
        items.put("daytime", formattedDateTime);
        items.put("status", "Đang xử lý");
        items.put("paymentstatus", "Chưa thanh toán");
        dbOrder.collection("Users").document(userEmail).collection("Order").document(idOrder)
                .set(items)
                .addOnCompleteListener(innerTask -> {
                    Toast.makeText(this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                });
    }

    public void deleteAllDataInCardCollection(String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cardCollection = db.collection("Users").document(userEmail).collection("Card");
        cardCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        cardCollection.document(document.getId()).delete();
                    }
                } else {
                }
            }
        });
    }

    private void updateTotalPrice(int total, TextView totalPriceAllProductsSelected) {
        totalPriceAllProductsSelected.setText(formatCurrency(String.valueOf(total)) + "đ");
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

    public static String removeLastCharacter(String str) {
        if (str != null && str.length() > 0) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    private static String removeCurrencyFormat(String formattedNumber) {
        return formattedNumber.replace(".", "");
    }

    private void showDeleteConfirmationDialog(String userEmail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xoá đơn hàng")
                .setMessage("Bạn có chắc chắn muốn xoá đơn hàng không?")
                .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore dbSelectedProducts = FirebaseFirestore.getInstance();
                        CollectionReference cardCollection = dbSelectedProducts.collection("Users").document(userEmail).collection("Card");
                        cardCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().delete();
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.cancel();
                                        }
                                    });;
                                } else {
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    //Get token through MoMo app
//    private void requestPayment(String idDonHang) {
//        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
//        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
//
//        Map<String, Object> eventValue = new HashMap<>();
//        //client Required
//        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
//        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
//        eventValue.put("amount", amount); //Kiểu integer
//        eventValue.put("orderId", idDonHang); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
//        eventValue.put("orderLabel", idDonHang); //gán nhãn
//
//        //client Optional - bill info
//        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
//        eventValue.put("fee", "0"); //Kiểu integer
//        eventValue.put("description", description); //mô tả đơn hàng - short description
//
//        //client extra data
//        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
//        eventValue.put("partnerCode", merchantCode);
//        //Example extra data
//        JSONObject objExtraData = new JSONObject();
//        try {
//            objExtraData.put("site_code", "008");
//            objExtraData.put("site_name", "CGV Cresent Mall");
//            objExtraData.put("screen_code", 0);
//            objExtraData.put("screen_name", "Special");
//            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
//            objExtraData.put("movie_format", "2D");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        eventValue.put("extraData", objExtraData.toString());
//
//        eventValue.put("extra", "");
//        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
//
//
//    }
//    //Get token callback from MoMo app an submit to server side
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
//            if(data != null) {
//                if(data.getIntExtra("status", -1) == 0) {
//                    //TOKEN IS AVAILABLE
//                    Log.d("thangcong", data.getStringExtra("message"));
//                    String token = data.getStringExtra("data"); //Token response
//                    String phoneNumber = data.getStringExtra("phonenumber");
//                    String env = data.getStringExtra("env");
//                    if(env == null){
//                        env = "app";
//                    }
//
//                    if(token != null && !token.equals("")) {
//                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
//                        // IF Momo topup success, continue to process your order
//                    } else {
//                        Log.d("thanhcong", data.getStringExtra("Không thành công"));
//                    }
//                } else if(data.getIntExtra("status", -1) == 1) {
//                    //TOKEN FAIL
//                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
//                    Log.d("thanhcong", data.getStringExtra("Không thành công"));
//                } else if(data.getIntExtra("status", -1) == 2) {
//                    //TOKEN FAIL
//                    Log.d("thanhcong", data.getStringExtra("Không thành công"));
//                } else {
//                    //TOKEN FAIL
//                    Log.d("thanhcong", data.getStringExtra("Không thành công"));
//                }
//            } else {
//                Log.d("thanhcong", data.getStringExtra("Không thành công"));
//            }
//        } else {
//            Log.d("thanhcong", data.getStringExtra("Không thành công"));
//        }
//    }

}