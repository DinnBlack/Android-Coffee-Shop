package com.example.thefutuscoffeeversion13.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.thefutuscoffeeversion13.Fragment.AdminAddEmployeeFragment;
import com.example.thefutuscoffeeversion13.Fragment.AdminDeliveringFragment;
import com.example.thefutuscoffeeversion13.Fragment.AdminListEmployeeFragment;
import com.example.thefutuscoffeeversion13.Fragment.AdminProcessingFragment;
import com.example.thefutuscoffeeversion13.Fragment.CancelledFragment;
import com.example.thefutuscoffeeversion13.Fragment.DeliveredFragment;
import com.example.thefutuscoffeeversion13.R;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class AdminEmployeeActivity extends AppCompatActivity {
    private Toolbar tbOrderHistory;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_employee);

        tbOrderHistory = findViewById(R.id.tbOrderHistory);
        frameLayout = findViewById(R.id.frameLayout);

        //setup toolbar
        setSupportActionBar(tbOrderHistory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setup Nav
        AnimatedBottomBar bottomBar = findViewById(R.id.navOrderHistory);
        replaceFragment(new AdminListEmployeeFragment());
        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                if (tab1.getId() == R.id.action_List_Employee) {
                    replaceFragment(new AdminListEmployeeFragment());
                } else if (tab1.getId() == R.id.action_Add_Employee) {
                    replaceFragment(new AdminAddEmployeeFragment());
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }

        });
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

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}