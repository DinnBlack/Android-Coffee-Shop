package com.example.thefutuscoffeeversion13.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.thefutuscoffeeversion13.R;

public class FeedbackActivity extends AppCompatActivity {
    Toolbar tbFeedback;
    LinearLayout btGoiDien, ln2;
    TextView sdt;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //find item
        tbFeedback = findViewById(R.id.tbFeedback);
        btGoiDien = findViewById(R.id.btGoiDien);
        sdt = findViewById(R.id.sdt);
        ln2 = findViewById(R.id.ln2);

        //setup toolbar
        setSupportActionBar(tbFeedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set goi dien
        btGoiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        //set link facebook
        ln2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebookPage();
            }
        });
    }

    private void openFacebookPage() {
        try {
            Uri facebookPageUri = Uri.parse("https://www.facebook.com/profile.php?id=100018913423970");

            Intent intent = new Intent(Intent.ACTION_VIEW, facebookPageUri);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {Toast.makeText(FeedbackActivity.this, "No app can handle this action", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận cuộc gọi");
        builder.setMessage("Bạn có muốn thực hiện cuộc gọi đến số điện thoại này không?");

        builder.setPositiveButton("Gọi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makePhoneCall();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled the call
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void makePhoneCall() {
        // Add "tel:" scheme to the phone number
        String phoneNumber = "tel:" + sdt.getText().toString().trim();

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));

        // Check if there is an activity that can handle the call intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Handle the case where there is no activity to handle the call
            Toast.makeText(FeedbackActivity.this, "No app can handle this action", Toast.LENGTH_SHORT).show();
        }
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
}