package com.example.thefutuscoffeeversion13.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.example.thefutuscoffeeversion13.R;

public class FeedbackActivity extends AppCompatActivity {
    Toolbar tbFeedback;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //find item
        tbFeedback = findViewById(R.id.tbFeedback);

        //setup toolbar
        setSupportActionBar(tbFeedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}