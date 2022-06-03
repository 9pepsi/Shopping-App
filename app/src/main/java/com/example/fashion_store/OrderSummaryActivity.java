package com.example.fashion_store;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class OrderSummaryActivity extends AppCompatActivity {

    String orderID;
    String orderQuantity;
    String orderTotalPrice;

    ListView orderSummaryList;
    TextView orderSummaryItemNo;
    TextView orderSummaryPriceTotal;
    Button trackOrderBT;

    ProgressDialog progressDialog;

    String userAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        Intent orderData = getIntent();
        orderID = orderData.getStringExtra("order_id");

        orderSummaryList = findViewById(R.id.order_summary_list_view);
        orderSummaryItemNo = findViewById(R.id.order_summary_no_of_items);
        orderSummaryPriceTotal = findViewById(R.id.order_summary_total);
        trackOrderBT = findViewById(R.id.order_summary_track_bt);

        class showOrderDetailsTask extends AsyncTask<String, Void, String[][]>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(OrderSummaryActivity.this, "", "Loading order details...");


                orderQuantity = orderData.getStringExtra("order_quantity");
                orderTotalPrice = orderData.getStringExtra("order_total_price");


                orderSummaryItemNo.setText(orderQuantity);
                orderSummaryPriceTotal.setText(orderTotalPrice.concat("$"));
            }

            @Override
            protected String[][] doInBackground(String... orderID) {
                String[][] orderDetails = null;
                try {
                    orderDetails = DBHelper.getInstance().getOrderDetailsItem(orderID[0]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return orderDetails;
            }

            @Override
            protected void onPostExecute(String[][] orderDetails) {

                if(orderDetails.length > 0){

                String[] productQuantities = new String[orderDetails.length];
                String[] productNames = new String[orderDetails.length];
                String[] productPrices = new String[orderDetails.length];

                for (int i = 0; i < orderDetails.length; i++){
                    productQuantities[i] = orderDetails[i][5];
                    productNames[i] = orderDetails[i][3];
                    productPrices[i] = orderDetails[i][4];
                }

                CustomOrderSummaryList customOrderSummaryList = new CustomOrderSummaryList(
                  OrderSummaryActivity.this, productNames,  productPrices,   productQuantities
                );
                orderSummaryList.setAdapter(customOrderSummaryList);

                }else{
                    Toast.makeText(OrderSummaryActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
            }
        }

        class getUserAddressTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    userAddress = DBHelper.getInstance().getUserAddress();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        new showOrderDetailsTask().execute(orderID);
        new getUserAddressTask().execute();

        trackOrderBT.setOnClickListener(view -> {
            Intent i = new Intent(OrderSummaryActivity.this, MapsActivity.class);
            i.putExtra("address", userAddress);
            startActivity(i);
        });
    }
}