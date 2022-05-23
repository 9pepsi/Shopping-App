package com.example.fashion_store;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class StoreSearchProductsActivity extends AppCompatActivity {
    TextView searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_search_products);

        searchQuery = findViewById(R.id.store_search_query);
        //get query
        Intent i = getIntent();
        if(i.hasExtra("category")){
            searchQuery.setText(i.getStringExtra("category"));
        }




    }
}