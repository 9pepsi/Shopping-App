package com.example.fashion_store;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.sql.SQLException;

public class StoreSearchProductsActivity extends AppCompatActivity {
    TextView searchQuery;
    String categoryFromIntent;
    ListView productList;
    ProgressDialog progressDialog;
    String[][] productsData;
    int productsCount;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_search_products);

        searchQuery = findViewById(R.id.store_search_query);
        productList = findViewById(R.id.search_product_list);

        //show category products task
        class showCategoryProductsTask extends AsyncTask<String, Void, String[][]>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(StoreSearchProductsActivity.this, "", "Loading Products...");
            }

            @Override
            protected String[][] doInBackground(String... category) {
                productsCount = 0;
                try {
                    productsCount = DBHelper.getInstance().getCategoryProductsCount(category[0]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String[][] categoryProducts = new String[productsCount][5];
                try {
                    categoryProducts = DBHelper.getInstance().getCategoryProducts(category[0]);
                    productsData = new String[categoryProducts.length][5];

                    for (int i = 0; i < categoryProducts.length; i++){
                        for (int j = 0; j< categoryProducts[i].length;j++){
                            productsData[i][j] = categoryProducts[i][j];
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return categoryProducts;
            }

            @Override
            protected void onPostExecute(String[][] categoryProducts) {

                String[] productsName = new String[categoryProducts.length];
                String[] productPrice = new String[categoryProducts.length];
                String[] productImage = new String[categoryProducts.length];

                for(int i = 0; i < categoryProducts.length; i++){
                    productsName[i] = categoryProducts[i][0];
                    productPrice[i] = categoryProducts[i][1];
                    productImage[i] = categoryProducts[i][2];
                }

                CustomProductsList cpl = new CustomProductsList(StoreSearchProductsActivity.this, productsName,
                        productPrice, productImage);
                productList.setAdapter(cpl);

                progressDialog.dismiss();
            }
        }

        class showProductSearchTask extends AsyncTask<String, Void, String[][]>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(StoreSearchProductsActivity.this, "", "Loading Products...");
            }

            @Override
            protected String[][] doInBackground(String... query) {
                productsCount = 0;
                try {
                    productsCount = DBHelper.getInstance().getSearchProductsCount(query[0]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String[][] productsQueryData = new String[productsCount][5];
                try {
                    productsQueryData = DBHelper.getInstance().getSearchProducts(query[0]);
                    productsData = new String[productsQueryData.length][5];

                    for (int i = 0; i < productsQueryData.length; i++){
                        for (int j = 0; j< productsQueryData[i].length;j++){
                            productsData[i][j] = productsQueryData[i][j];
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return productsQueryData;
            }

            @Override
            protected void onPostExecute(String[][] productsQueryData) {
                String[] productsName = new String[productsQueryData.length];
                String[] productPrice = new String[productsQueryData.length];
                String[] productImage = new String[productsQueryData.length];

                for(int i = 0; i < productsQueryData.length; i++){
                    productsName[i] = productsQueryData[i][0];
                    productPrice[i] = productsQueryData[i][1];
                    productImage[i] = productsQueryData[i][2];
                }

                CustomProductsList cpl = new CustomProductsList(StoreSearchProductsActivity.this, productsName,
                        productPrice, productImage);
                productList.setAdapter(cpl);

                progressDialog.dismiss();

            }
        }

        //get query
         //query from category page
        Intent i = getIntent();
        if(i.hasExtra("category")){
            categoryFromIntent = i.getStringExtra("category");
            searchQuery.setText(categoryFromIntent);
            new showCategoryProductsTask().execute(categoryFromIntent);
        }
         //query from search button
        if (Intent.ACTION_SEARCH.equals(i.getAction())) {
            String query = i.getStringExtra(SearchManager.QUERY);
            searchQuery.setText(query);
            new showProductSearchTask().execute(query);
        }

        productList.setOnItemClickListener((adapterView, view, position, l) -> {
            String[] productData = new String[5];
            for (int d = 0; d < productData.length; d++){
                productData[d] = productsData[position][d];
            }
            Intent intent = new Intent(StoreSearchProductsActivity.this, ProductPageActivity.class);
            intent.putExtra("product_data", productData);
            startActivity(intent);

        });

        //search widget
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = findViewById(R.id.search_page_widget_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));




    }
}