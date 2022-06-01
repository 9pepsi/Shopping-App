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

public class CustomTrackOrdersList extends ArrayAdapter<String> {
    private String[] orderID;
    private String[] orderDate;
    private String[] orderQuantity;
    private String[] orderPrice;
    private Activity context;

    public CustomTrackOrdersList(Activity context, String[] orderID, String[] orderDate, String[] orderQuantity, String[] orderPrice) {
        super(context, R.layout.track_order_list_view, orderID);
        this.context = context;
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.orderQuantity = orderQuantity;
        this.orderPrice = orderPrice;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.track_order_list_view, null, true);

        TextView orderIDView = row.findViewById(R.id.order_list_order_id);
        TextView orderDateView = row.findViewById(R.id.order_list_order_date);
        TextView orderQuantityView = row.findViewById(R.id.order_list_order_quantity);
        TextView orderPriceView = row.findViewById(R.id.order_list_order_price);

        String orderIDText = orderIDView.getText().toString().replace("{ORDER_ID}", orderID[position]);
        String orderDateText = orderDateView.getText().toString().replace("{ORDER_DATE}",orderDate[position]);
        String orderQuantityText = orderQuantityView.getText().toString().replace("{NO}",orderQuantity[position]);
        String orderPriceText = orderPriceView.getText().toString().replace("{PRICE}",orderPrice[position].concat("$"));

        orderIDView.setText(orderIDText);
        orderDateView.setText(orderDateText);
        orderQuantityView.setText(orderQuantityText);
        orderPriceView.setText(orderPriceText);

        return  row;
    }
}
