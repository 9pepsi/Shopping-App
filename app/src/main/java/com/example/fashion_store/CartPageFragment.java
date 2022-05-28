package com.example.fashion_store;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.apache.commons.lang3.ArrayUtils;
import java.sql.SQLException;

public class CartPageFragment extends Fragment {

    ListView cartList;
    ProgressDialog progressDialog;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade));
        setEnterTransition(inflater.inflateTransition(R.transition.slide_bottom));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View empty_cart_view = inflater.inflate(R.layout.fragment_empty_cart_page, container, false);
        view = inflater.inflate(R.layout.fragment_cart_page, container, false);

        cartList = view.findViewById(R.id.cart_list);

        class checkUserCart extends AsyncTask<Void, Void, String[][]>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(view.getContext(),"", "Checking Cart...");
            }

            @Override
            protected String[][] doInBackground(Void... voids) {
                int cartItemCount = 0;
                String[][] cartItems = null;
                try {
                    cartItemCount = new DBHelper().getUserCartCount();
                    cartItems = new String[cartItemCount][6];
                    cartItems = new DBHelper().getUserCart();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return cartItems;
            }

            @Override
            protected void onPostExecute(String[][] productCart) {

                if(!ArrayUtils.isEmpty(productCart)) {

                    view = inflater.inflate(R.layout.fragment_cart_page, container, false);

                    String[] productNames = new String[productCart.length];
                    String[] productPrices = new String[productCart.length];
                    String[] productImages = new String[productCart.length];
                    String[] productQuantities = new String[productCart.length];

                    for (int i = 0; i < productCart.length; i++) {
                        productNames[i] = productCart[i][2];
                        productPrices[i] = productCart[i][3];
                        productImages[i] = productCart[i][4];
                        productQuantities[i] = productCart[i][5];
                    }

                    CustomCartList customCartList = new CustomCartList(getActivity(), productNames, productPrices,
                            productImages, productQuantities);
                    cartList.setAdapter(customCartList);
                }else{
                    view = inflater.inflate(R.layout.fragment_empty_cart_page, container, false);
                }

                progressDialog.dismiss();
            }
        }

        new checkUserCart().execute();

        return view;
    }
}