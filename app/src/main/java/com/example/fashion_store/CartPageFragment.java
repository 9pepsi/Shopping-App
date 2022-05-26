package com.example.fashion_store;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class CartPageFragment extends Fragment {

    ListView cartList;

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
        View view = inflater.inflate(R.layout.fragment_cart_page, container, false);

        cartList = view.findViewById(R.id.cart_list);

        Intent fromP = requireActivity().getIntent();
        if(fromP.hasExtra("product_data")){
            String[] productData = fromP.getStringArrayExtra("product_data");
            String quantity = fromP.getStringExtra("product_quantity");
            String[] product_name = {productData[0]};
            String[] product_price = {productData[1]};
            String[] product_img = {productData[2]};
            String[] product_quantity = {quantity};
            CustomCartList CCL = new CustomCartList(getActivity(),product_name, product_price, product_img, product_quantity);
            cartList.setAdapter(CCL);
        }else{
            view = empty_cart_view;
        }

        return view;
    }
}