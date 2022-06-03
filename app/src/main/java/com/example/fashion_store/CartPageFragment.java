package com.example.fashion_store;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.sql.SQLException;

public class CartPageFragment extends Fragment {

    ListView cartList;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    Button checkoutButton;
    View view;

    String[] productNames;
    String[] productPrices;
    String[] productImages;
    String[] productQuantities;

    @SuppressLint("StaticFieldLeak")
    class checkUserCart extends AsyncTask<Void, Void, String[][]>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(view.getContext(),"", "Checking Cart...");
        }

        @Override
        protected String[][] doInBackground(Void... voids) {
            String[][] cartItems = null;
            try {
                cartItems = DBHelper.getInstance().getUserCart();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return cartItems;
        }

        @Override
        protected void onPostExecute(String[][] productCart) {
            if(!(productCart == null) && productCart.length > 0) {
                 productNames = new String[productCart.length];
                 productPrices = new String[productCart.length];
                 productImages = new String[productCart.length];
                 productQuantities = new String[productCart.length];

                for (int i = 0; i < productCart.length; i++) {
                    productNames[i] = productCart[i][2];
                    productPrices[i] = productCart[i][3];
                    productImages[i] = productCart[i][4];
                    productQuantities[i] = productCart[i][5];
                }

                CustomCartList customCartList = new CustomCartList(getActivity(), productNames, productPrices,
                        productImages, productQuantities);
                cartList.setAdapter(customCartList);
            }else {
                EmptyCartPageFragment emptyCartPageFragment = new EmptyCartPageFragment();
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.store_fragment, emptyCartPageFragment).commit();
            }


            swipeRefreshLayout.setRefreshing(false);
            progressDialog.dismiss();
        }
    }


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
        view = inflater.inflate(R.layout.fragment_cart_page, container, false);

        cartList = view.findViewById(R.id.cart_list);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        checkoutButton = view.findViewById(R.id.cart_checkout_bt);

        checkoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CheckoutActivity.class);
            intent.putExtra("order_product_names", productNames);
            intent.putExtra("order_product_prices", productPrices);
            intent.putExtra("order_product_quantities", productQuantities);
            startActivity(intent);
        });

        onRefreshListener = () -> new checkUserCart().execute();
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);



        new checkUserCart().execute();

        return view;
    }


}