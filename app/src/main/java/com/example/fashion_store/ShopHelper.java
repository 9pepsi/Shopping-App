package com.example.fashion_store;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.sql.SQLException;

public class ShopHelper {

    public static String[] displayFeaturedPageProducts(String productName, TextView productNameView,
                                                   TextView productPriceView, ImageView productImgView) throws SQLException {

        String[] productData = new DBHelper().displayProductData(productName);
        productNameView.setText(productData[0]);
        productPriceView.setText(productData[1].concat("$"));
        Picasso.get().load(Uri.parse(productData[2])).into(productImgView);

        return productData;
    }
}
