package com.example.fashion_store;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StoreMainPageActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    SearchView mainPageSearch;

    FeaturedPageFragment featuredPF = new FeaturedPageFragment();
    CategoriesPageFragment categoriesPF = new CategoriesPageFragment();
    CartPageFragment cartPF = new CartPageFragment();
    ProfilePageFragment profilePF = new ProfilePageFragment();
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_main_page);
        //fragment select
        bottomNavigationView =findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.main_page:
                    getSupportFragmentManager().beginTransaction().replace(R.id.store_fragment, featuredPF).commit();
                    return true;
                case R.id.categories_page:
                    getSupportFragmentManager().beginTransaction().replace(R.id.store_fragment, categoriesPF).commit();
                    return true;
                case R.id.cart_page:
                    getSupportFragmentManager().beginTransaction().replace(R.id.store_fragment, cartPF).commit();
                    return true;
                case R.id.profile_page:
                    getSupportFragmentManager().beginTransaction().replace(R.id.store_fragment, profilePF).commit();
                    return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.main_page);
        //search bar
        mainPageSearch = findViewById(R.id.main_page_search);


    }
}