package com.example.fashion_store;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.squareup.picasso.Picasso;

import java.sql.SQLException;

public class CustomCartList extends ArrayAdapter {
    private String[] productName;
    private String[] productPrice;
    private String[] productImage;
    private String[] productQuantity;
    private Activity context;
    ProgressDialog progressDialog;

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

        class updateQuantityTask extends AsyncTask<String, Void, Boolean>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(context, "", "Updating Quantity...");
            }

            @Override
            protected Boolean doInBackground(String... strings) {
                boolean done = false;
                try {
                    done = DBHelper.getInstance().updateItemQuantity(strings[0], strings[1]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return done;
            }

            @Override
            protected void onPostExecute(Boolean done) {
                if(done){
                    Toast.makeText(context, "Updated Quantity!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }

        class deleteCartItemTask extends AsyncTask<String, Void, Boolean>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(context, "", "Deleting Item...");
            }

            @Override
            protected Boolean doInBackground(String... strings) {
                boolean done = false;
                try {
                    done = DBHelper.getInstance().deleteCartItem(strings[0]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return done;
            }

            @Override
            protected void onPostExecute(Boolean done) {
                if(done){
                    Toast.makeText(context, "Item Deleted!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                }
                CartPageFragment cartPageFragment = new CartPageFragment();
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.store_fragment, cartPageFragment).commit();
                progressDialog.dismiss();
            }
        }

        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new deleteCartItemTask().execute(productName[position]);

            }
        });

        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(productQuantity[position]);
                if(quantity < 99){
                    quantity++;
                    new updateQuantityTask().execute(Integer.toString(quantity), productName[position]);
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
                    new updateQuantityTask().execute(Integer.toString(quantity), productName[position]);
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
