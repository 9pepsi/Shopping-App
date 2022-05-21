package com.example.fashion_store;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CategoriesPageFragment extends Fragment {

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

        View view = inflater.inflate(R.layout.fragment_categories_page, container, false);

        HashMap<String, Uri> categoryMap = new HashMap<>();
        categoryMap.put("Technology",Uri.parse("https://www.pngkey.com/png/detail/233-2332677_image-500580-placeholder-transparent.png"));
        categoryMap.put("Shoes",Uri.parse("https://www.pngkey.com/png/detail/233-2332677_image-500580-placeholder-transparent.png"));
        categoryMap.put("Shirts",Uri.parse("https://www.pngkey.com/png/detail/233-2332677_image-500580-placeholder-transparent.png"));
        categoryMap.put("Pants",Uri.parse("https://www.pngkey.com/png/detail/233-2332677_image-500580-placeholder-transparent.png"));
        categoryMap.put("Jackets",Uri.parse("https://www.pngkey.com/png/detail/233-2332677_image-500580-placeholder-transparent.png"));

        // Inflate the layout for this fragment
        return view;
    }
}