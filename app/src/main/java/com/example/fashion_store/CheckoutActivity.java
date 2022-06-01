package com.example.fashion_store;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class CheckoutActivity extends AppCompatActivity {

    String[] productNames;
    String[] productPrices;
    String[] productQuantities;

    ListView checkoutList;
    TextView totalQuantityView;
    TextView totalPriceView;
    Button confirmOrder;

    ProgressDialog progressDialog;

    String storeEmail = "fashion.store.noreply@gmail.com";
    String storePassword = "Tetn2o10";
    String subjectEmail = "Order Confirmation";
    String bodyEmail = "Hello, \n Your Fashion Store order is Confirmed!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        //get order from cart page
        Intent fromCart = getIntent();
        productNames = fromCart.getStringArrayExtra("order_product_names");
        productPrices = fromCart.getStringArrayExtra("order_product_prices");
        productQuantities = fromCart.getStringArrayExtra("order_product_quantities");
        //populate order list
        checkoutList = findViewById(R.id.checkout_list_view);
        CustomCheckoutList customCheckoutList =
                new CustomCheckoutList(CheckoutActivity.this, productNames, productPrices, productQuantities);
        checkoutList.setAdapter(customCheckoutList);
        //calculate order total
        totalQuantityView = findViewById(R.id.no_of_items);
        totalPriceView = findViewById(R.id.order_total);

        int totalQuantity = toIntegerArrayGetTotalQuantity(productQuantities);
        float totalOrder = calculateTotalOrderPrice(productQuantities, productPrices);
        String totalQuantityText = Integer.toString(totalQuantity);
        String totalOrderText = roundOffTo2DecPlaces(totalOrder);


        totalQuantityView.setText(totalQuantityText);
        totalPriceView.setText(totalOrderText.concat("$"));
        //confirm order task
        class ConfirmOrderTask extends AsyncTask<String, Void, Boolean>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(CheckoutActivity.this, "",  "Confirming Order");
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Boolean doInBackground(String... orderData) {
                boolean isConfirmed = false;
                try {
                    isConfirmed = DBHelper.getInstance().confirmOrder(orderData[0],orderData[1]);
                    String userEmail = DBHelper.getInstance().getUserEmail();
                    if(isConfirmed){
                        GMailSender sender = new GMailSender(storeEmail, storePassword);
                        sender.sendMail(subjectEmail, bodyEmail , storeEmail, userEmail);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return isConfirmed;
            }

            @Override
            protected void onPostExecute(Boolean isConfirmed) {
                progressDialog.dismiss();

                if(isConfirmed){
                    new StoreDialogs().orderConfirmedDialog(CheckoutActivity.this, "Order Confirmed!").show();

                }else {
                    Toast.makeText(CheckoutActivity.this, "Something went wrong...", Toast.LENGTH_LONG).show();
                }
            }
        }
        //submit button
        confirmOrder = findViewById(R.id.checkout_submit_bt);
        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConfirmOrderTask().execute(totalQuantityText, totalOrderText);
            }
        });
    }

    int toIntegerArrayGetTotalQuantity(String[] array){
        int[] newArray = new int[array.length];
        int total = 0;
        for (int i = 0; i < array.length; i++){
            newArray[i] = Integer.parseInt(array[i]);
            total = total + newArray[i];
        }
        return total;
    }

    float calculateTotalOrderPrice(String[] productQuantities, String[] productPrices){
        float[] quantity = new float[productQuantities.length];
        float[] prices = new float[productPrices.length];
        float[] itemPrice = new float[productQuantities.length];
        float totalOrderPrice = 0;

        for (int i = 0; i < productPrices.length; i++){
            quantity[i] = Float.parseFloat(productQuantities[i]);
            prices[i] = Float.parseFloat(productPrices[i]);
            itemPrice[i] = quantity[i] * prices[i];
            totalOrderPrice += itemPrice[i];
        }

        return totalOrderPrice;
    }

    String roundOffTo2DecPlaces(float val) {return String.format("%.2f", val);}
}