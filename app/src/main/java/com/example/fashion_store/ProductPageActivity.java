package com.example.fashion_store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProductPageActivity extends AppCompatActivity {
    String[] productData;
    TextView productName;
    ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        //get product data
        Intent fromFP = getIntent();
        productData = fromFP.getStringArrayExtra("product_data");
        //set product name
        productName = findViewById(R.id.product_page_name);
        productName.setText(productData[0]);
        //back button func
        backButton = findViewById(R.id.product_page_back_bt);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProductPageActivity.this, StoreMainPageActivity.class);
                NavUtils.navigateUpTo(ProductPageActivity.this, i);
            }
        });
    }
}