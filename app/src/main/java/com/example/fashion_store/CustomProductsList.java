package com.example.fashion_store;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CustomProductsList extends ArrayAdapter {
    private String[] productName;
    private String[] productPrice;
    private String[] productImage;
    private Activity context;

    public CustomProductsList(Activity context, String[] productName, String[] productPrice, String[] productImage) {
        super(context, R.layout.search_products_list_view, productName);
        this.context = context;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.search_products_list_view, null, true);
        TextView productTextView = row.findViewById(R.id.search_product_name);
        TextView priceTextView = row.findViewById(R.id.search_product_price);
        ImageView imageProduct = row.findViewById(R.id.search_product_image);

        productTextView.setText(productName[position]);
        priceTextView.setText(productPrice[position].concat("$"));
        Picasso.get().load(Uri.parse(productImage[position])).into(imageProduct);
        return  row;
    }
}
