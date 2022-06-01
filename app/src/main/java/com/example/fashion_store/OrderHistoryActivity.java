package com.example.fashion_store;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.ArrayUtils;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.sql.SQLException;
import java.util.Arrays;

public class OrderHistoryActivity extends AppCompatActivity {

    TextView monthlyTextView;
    ListView monthlyListView;
    String currentDate;
    String lastMonthDate;

    ProgressDialog progressDialog;

    PieChart pieChart;
    TextView orangeDate;
    TextView greenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        monthlyTextView = findViewById(R.id.month_text_view);
        monthlyListView = findViewById(R.id.order_history_listview);
        pieChart = findViewById(R.id.order_history_pie_chart);

        orangeDate = findViewById(R.id.orange_text_view);
        greenDate = findViewById(R.id.green_text_view);

        class getMonthlyOrderHistory extends AsyncTask<Void, Void, String[][]>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(OrderHistoryActivity.this, "", "Loading Order History");
            }

            @Override
            protected String[][] doInBackground(Void... voids) {
                String[][] userOrders = null;
                try {
                    userOrders = DBHelper.getInstance().getUserOrdersThisMonth();
                    currentDate = DBHelper.getInstance().getCurrentDate();
                    lastMonthDate = DBHelper.getInstance().getLastMonthDate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return userOrders;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void onPostExecute(String[][] userOrders) {
                //get order history
                String[] ordersID = new String[userOrders.length];
                String[] ordersDate = new String[userOrders.length];
                String[] ordersQuantity = new String[userOrders.length];
                String[] ordersPrice = new String[userOrders.length];

                for (int i = 0; i < userOrders.length; i++){
                    ordersID[i] = userOrders[i][2];
                    ordersDate[i] = userOrders[i][5];
                    ordersQuantity[i] = userOrders[i][3];
                    ordersPrice[i] = userOrders[i][4];
                }

                CustomTrackOrdersList customTrackOrdersLists = new CustomTrackOrdersList(
                        OrderHistoryActivity.this, ordersID, ordersDate, ordersQuantity, ordersPrice
                );
                monthlyListView.setAdapter(customTrackOrdersLists);
                //get date
                String dateR = monthlyTextView.getText().toString().replace("{DATE}", currentDate);
                String date = dateR.replace("{DATE_LAST_MONTH}", lastMonthDate);
                monthlyTextView.setText(date);
                //pie chart
                //getting pie chart data
                String[] uniqueDates = Arrays.stream(ordersDate).distinct().toArray(String[]::new);
                String[] uniquePrices = new String[uniqueDates.length];
                Float[] ordersPriceFloat = Arrays.stream(ordersPrice).map(Float::valueOf).toArray(Float[]::new);
                Float[] uniquePricesFloat = new Float[uniqueDates.length];
                Float overallPaid = 0f;
                int[] color = new int[2];
                color[0] = Color.parseColor("#FFA726");
                color[1] = Color.parseColor("#66BB6A");
                for (int i = 0; i < ordersDate.length; i++){
                    for (int j = 0; j < uniqueDates.length; j++){
                        if(ordersDate[i].equals(uniqueDates[j])){
                            if(uniquePricesFloat[j] != null){
                                uniquePricesFloat[j] += ordersPriceFloat[i];
                            }else if(uniquePricesFloat[j] == null){
                                uniquePricesFloat[j] = ordersPriceFloat[i];
                            }
                        }
                    }
                }
                for (int i = 0; i < uniquePricesFloat.length; i++){
                    overallPaid += uniquePricesFloat[i];
                    uniquePrices[i] = Float.toString(uniquePricesFloat[i]);
                }
                for (int i = 0; i < uniqueDates.length; i++){
                    pieChart.addPieSlice(
                            new PieModel(
                                    ordersDate[i],
                                    getPercentage(uniquePricesFloat[i], overallPaid),
                                    color[i]
                            )
                    );
                }
                orangeDate.setText(ordersDate[0]);
                greenDate.setText(ordersDate[1]);

                Toast.makeText(OrderHistoryActivity.this, Float.toString(overallPaid), Toast.LENGTH_LONG).show();
                pieChart.startAnimation();

                progressDialog.dismiss();
            }
        }

        new getMonthlyOrderHistory().execute();
    }

    public float getPercentage(float number, float overall){
        return number/overall*100;
    }
}