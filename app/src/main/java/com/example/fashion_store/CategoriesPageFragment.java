package com.example.fashion_store;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CategoriesPageFragment extends Fragment {

    View view;
    ListView categoryList;
    String[] categoryName = {"Shirts", "Shoes", "Pants", "Jackets", "Watches", "Accessories"};
    Integer[] categoryImages = {R.drawable.category_shirt,R.drawable.category_shoes,R.drawable.category_pants,
            R.drawable.category_jackets,R.drawable.category_watches,R.drawable.category_acc};

    public CategoriesPageFragment() {
        // Required empty public constructor
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

        view = inflater.inflate(R.layout.fragment_categories_page, container, false);

        categoryList = view.findViewById(R.id.category_list);
        //populate category list
        CustomCategoryList ccl = new CustomCategoryList(getActivity(), categoryName, categoryImages);
        categoryList.setAdapter(ccl);
        //on category click
        categoryList.setOnItemClickListener((adapterView, view, position, l) -> {
            Intent i = new Intent(view.getContext(), StoreSearchProductsActivity.class);
            i.putExtra("category", categoryName[position]);
            startActivity(i);
        });
        // Inflate the layout for this fragment
        return view;
    }
}