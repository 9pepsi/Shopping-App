package com.example.fashion_store;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class CustomCheckoutList extends ArrayAdapter<String> {
    private String[] productNames;
    private String[] productPrices;
    private String[] productQuantities;
    private Activity context;

    public CustomCheckoutList(Activity context, String[] productNames, String[] productPrices, String[] productQuantities) {
        super(context, R.layout.checkout_page_list_view, productNames);
        this.context = context;
        this.productNames = productNames;
        this.productPrices = productPrices;
        this.productQuantities = productQuantities;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.checkout_page_list_view, null, true);

        TextView productQuantity = row.findViewById(R.id.list_quantity);
        TextView productName = row.findViewById(R.id.list_product_name);
        TextView productTotalPrice = row.findViewById(R.id.list_total_price);

        productQuantity.setText(productQuantities[position]);
        productName.setText(productNames[position]);

        Float Quantity = Float.parseFloat(productQuantities[position]);
        Float productPrice = Float.parseFloat(productPrices[position]);
        float totalPrice = Quantity * productPrice;

        String totalItemPrice = roundOffTo2DecPlaces(totalPrice);
        productTotalPrice.setText(totalItemPrice.concat("$"));

        return  row;
    }

    String roundOffTo2DecPlaces(float val)
    {
        return String.format("%.2f", val);
    }
}
