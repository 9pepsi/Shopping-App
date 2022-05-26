package com.example.fashion_store;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import org.apache.commons.lang3.ArrayUtils;

public class CustomCartList extends ArrayAdapter {
    private String[] productName;
    private String[] productPrice;
    private String[] productImage;
    private String[] productQuantity;
    private Activity context;

    public CustomCartList(Activity context, String[] productName, String[] productPrice, String[] productImage,
                          String[]productQuantity) {
        super(context, R.layout.cart_page_list_view, productName);
        this.context = context;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productQuantity = productQuantity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.cart_page_list_view, null, true);
        TextView productTextView = row.findViewById(R.id.cart_product_name);
        TextView priceTextView = row.findViewById(R.id.cart_product_price);
        ImageView imageProduct = row.findViewById(R.id.cart_product_image);
        TextView productQuantityView = row.findViewById(R.id.cart_page_quantity);
        ImageButton deleteProduct = row.findViewById(R.id.cart_page_delete_bt);
        ImageButton increaseQuantity = row.findViewById(R.id.cart_page_add_quantity_bt);
        ImageButton decreaseQuantity = row.findViewById(R.id.cart_page_dec_quantity);

        productTextView.setText(productName[position]);
        priceTextView.setText(productPrice[position].concat("$"));
        productQuantityView.setText(productQuantity[position]);
        Picasso.get().load(Uri.parse(productImage[position])).into(imageProduct);

        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayUtils.remove(productName,position);
                ArrayUtils.remove(productPrice,position);
                ArrayUtils.remove(productQuantity,position);
                ArrayUtils.remove(productImage,position);
                notifyDataSetChanged();
            }
        });

        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(productQuantity[position]);
                if(quantity < 99){
                    quantity++;
                }
                String newQuantity = Integer.toString(quantity);
                productQuantity[position] = newQuantity;
                productQuantityView.setText(productQuantity[position]);
                notifyDataSetChanged();
            }
        });

        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(productQuantity[position]);
                if(quantity > 1){
                    quantity--;
                }
                String newQuantity = Integer.toString(quantity);
                productQuantity[position] = newQuantity;
                productQuantityView.setText(productQuantity[position]);
                notifyDataSetChanged();
            }
        });

        return  row;
    }

}
