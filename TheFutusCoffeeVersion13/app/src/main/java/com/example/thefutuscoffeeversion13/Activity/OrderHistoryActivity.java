package com.example.thefutuscoffeeversion13.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thefutuscoffeeversion13.Fragment.CancelledFragment;
import com.example.thefutuscoffeeversion13.Fragment.DeliveredFragment;
import com.example.thefutuscoffeeversion13.Fragment.DeliveringFragment;
import com.example.thefutuscoffeeversion13.Fragment.DiscountFragment;
import com.example.thefutuscoffeeversion13.Fragment.HomeFragment;
import com.example.thefutuscoffeeversion13.Fragment.OrderFragment;
import com.example.thefutuscoffeeversion13.Fragment.OtherFragment;
import com.example.thefutuscoffeeversion13.Fragment.ProcessingFragment;
import com.example.thefutuscoffeeversion13.R;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class OrderHistoryActivity extends AppCompatActivity {
    private Toolbar tbOrderHistory;
    private FrameLayout frameLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        //find item
        tbOrderHistory = findViewById(R.id.tbOrderHistory);
        frameLayout = findViewById(R.id.frameLayout);

        //setup toolbar
        setSupportActionBar(tbOrderHistory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setup Nav
        AnimatedBottomBar bottomBar = findViewById(R.id.navOrderHistory);
        replaceFragment(new ProcessingFragment());
        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                if (tab1.getId() == R.id.action_Processing) {
                    replaceFragment(new ProcessingFragment());
                } else if (tab1.getId() == R.id.action_Delivering) {
                    replaceFragment(new DeliveringFragment());
                } else if (tab1.getId() == R.id.action_Delivered) {
                    replaceFragment(new DeliveredFragment());
                } else if (tab1.getId() == R.id.action_cancelled) {
                    replaceFragment(new CancelledFragment());
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }

        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
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