package com.example.fashion_store;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public class TrackOrderActivity extends AppCompatActivity {

    ListView trackOrdersList;
    ProgressDialog progressDialog;

    String[] ordersID;
    String[] ordersDate;
    String[] ordersQuantity;
    String[] ordersPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        trackOrdersList = findViewById(R.id.track_orders_list_view);

        class getUserOrdersTask extends AsyncTask<Void, Void, String[][]>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(TrackOrderActivity.this, "", "Loading Orders...");
            }

            @Override
            protected String[][] doInBackground(Void... voids) {
                String[][] userOrders = null;
                try {
                    userOrders = DBHelper.getInstance().getUserOrders();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return userOrders;
            }

            @Override
            protected void onPostExecute(String[][] userOrders) {
                 ordersID = new String[userOrders.length];
                 ordersDate = new String[userOrders.length];
                 ordersQuantity = new String[userOrders.length];
                 ordersPrice = new String[userOrders.length];

                for (int i = 0; i < userOrders.length; i++){
                    ordersID[i] = userOrders[i][2];
                    ordersDate[i] = userOrders[i][5];
                    ordersQuantity[i] = userOrders[i][3];
                    ordersPrice[i] = userOrders[i][4];
                }

                CustomTrackOrdersList customTrackOrdersLists = new CustomTrackOrdersList(
                        TrackOrderActivity.this, ordersID, ordersDate, ordersQuantity, ordersPrice
                );
                trackOrdersList.setAdapter(customTrackOrdersLists);

                progressDialog.dismiss();
            }
        }

        new getUserOrdersTask().execute();

        trackOrdersList.setOnItemClickListener((adapterView, view, position, l) -> {
            Intent i = new Intent(TrackOrderActivity.this, OrderSummaryActivity.class);
            i.putExtra("order_id", ordersID[position]);
            i.putExtra("order_quantity", ordersQuantity[position]);
            i.putExtra("order_total_price", ordersPrice[position]);
            startActivity(i);
        });
    }
}