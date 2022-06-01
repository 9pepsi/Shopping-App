package com.example.fashion_store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.SQLException;

public class ProductPageActivity extends AppCompatActivity {
    String[] productData;
    TextView productName;
    ImageButton backButton;
    ImageView productImage;
    TextView productCategory;
    TextView productPrice;
    TextView productDescription;
    ImageButton decQuantity;
    ImageButton incQuantity;
    TextView productQuantity;
    Button addToCartBT;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        //add to cart task
        class addToCartTask extends AsyncTask<ProductCart, Void, Boolean>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(ProductPageActivity.this, "", "Adding to cart...");
            }

            @Override
            protected Boolean doInBackground(ProductCart... productCarts) {
                boolean done = false;
                try {
                    done = DBHelper.getInstance().addOrUpdateCart(productCarts[0]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return done;
            }

            @Override
            protected void onPostExecute(Boolean done) {
                if(done){
                    Toast.makeText(ProductPageActivity.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ProductPageActivity.this, "Failed to add product", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        }
        //get product data
        Intent fromFP = getIntent();
        productData = fromFP.getStringArrayExtra("product_data");
        //set product name
        productName = findViewById(R.id.product_page_name);
        productName.setText(productData[0]);
        //back button func
        backButton = findViewById(R.id.product_page_back_bt);
        backButton.setOnClickListener(view -> {
            Intent i = new Intent(ProductPageActivity.this, StoreMainPageActivity.class);
            NavUtils.navigateUpTo(ProductPageActivity.this, i);
        });
        //set product image
        productImage = findViewById(R.id.product_page_image);
        Picasso.get().load(Uri.parse(productData[2])).into(productImage);
        //set product category
        productCategory = findViewById(R.id.product_page_category);
        String categoryPlaceholder = productCategory.getText().toString();
        productCategory.setText(categoryPlaceholder.replace("{Category Name}", productData[4]));
        //set product price
        productPrice = findViewById(R.id.product_page_price);
        String pricePlaceholder = productPrice.getText().toString();
        productPrice.setText(pricePlaceholder.replace("{Price}", productData[1]).concat("$"));
        //set product description
        productDescription = findViewById(R.id.product_page_description);
        String descriptionPlaceholder = productDescription.getText().toString();
        productDescription.setText(descriptionPlaceholder.replace("{Description}", productData[3]));
        //product quantity
        productQuantity = findViewById(R.id.product_page_quantity);
        incQuantity = findViewById(R.id.product_page_inc_quantity);
        decQuantity = findViewById(R.id.product_page_dec_quantity);

            //quantity button listeners
        incQuantity.setOnClickListener(view -> {
            int quantity = Integer.parseInt(productQuantity.getText().toString());
            if(quantity == 99){;}
            else{
                quantity++;
                productQuantity.setText(Integer.toString(quantity));
            }
        });
        decQuantity.setOnClickListener(view -> {
            int quantity = Integer.parseInt(productQuantity.getText().toString());
            if (quantity == 1){;}
            else {
                quantity--;
                productQuantity.setText(Integer.toString(quantity));
            }
        });
        //add to cart
        addToCartBT = findViewById(R.id.product_page_add_to_cart);
        addToCartBT.setOnClickListener(view -> {
            ProductCart productCart = new ProductCart();
            productCart.product_name = productData[0];
            productCart.product_price = productData[1];
            productCart.product_image = productData[2];
            productCart.product_quantity = productQuantity.getText().toString();

            new addToCartTask().execute(productCart);

        });

    }
}