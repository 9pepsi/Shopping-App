package com.example.fashion_store;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeaturedPageFragment extends Fragment {
    View view;
    ImageSlider imageSlider;
    //product 1
    ImageView productImg1;
    TextView productName1;
    TextView productPrice1;
    CardView p1CardView;
    String[] p1Data;
    //product 2
    ImageView productImg2;
    TextView productName2;
    TextView productPrice2;
    CardView p2CardView;
    String[] p2Data;
    //product 3
    ImageView productImg3;
    TextView productName3;
    TextView productPrice3;
    CardView p3CardView;
    String[] p3Data;

    public FeaturedPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_left));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_featured_page, container, false);
        //vars
        imageSlider = view.findViewById(R.id.slider);
        productImg1 = view.findViewById(R.id.fp_img_1);
        productName1 = view.findViewById(R.id.fp_name_1);
        productPrice1 = view.findViewById(R.id.fp_price_1);
        productImg2 = view.findViewById(R.id.fp_img_2);
        productName2 = view.findViewById(R.id.fp_name_2);
        productPrice2 = view.findViewById(R.id.fp_price_2);
        productImg3 = view.findViewById(R.id.fp_img_3);
        productName3 = view.findViewById(R.id.fp_name_3);
        productPrice3 = view.findViewById(R.id.fp_price_3);
        //image slider
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://picjumbo.com/wp-content/uploads/woman-holding-many-shopping-bags-free-photo-2210x1473.jpg"));
        slideModels.add(new SlideModel("https://i.pinimg.com/564x/50/98/3e/50983e55f86fb3b75138fcd785b574ae.jpg"));
        slideModels.add(new SlideModel("https://itsuitsfashion.com/wp-content/uploads/2020/04/placeholder-header.jpg"));
        imageSlider.setImageList(slideModels, true);
        //featured products
            //product1
        try {
            p1Data = ShopHelper.displayFeaturedPageProducts("Apple Watch", productName1, productPrice1, productImg1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
            //product 1 product page
        p1CardView = view.findViewById(R.id.card_fp1);
        p1CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProductPageActivity.class);
                i.putExtra("product_data", p1Data);
                startActivity(i);
            }
        });
            //product 2
        try {
            p2Data = ShopHelper.displayFeaturedPageProducts("Placeholder Shirt", productName2, productPrice2, productImg2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
            //product 2 product page
        p2CardView = view.findViewById(R.id.card_fp2);
        p2CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProductPageActivity.class);
                i.putExtra("product_data", p2Data);
                startActivity(i);
            }
        });
        //product 3
        try {
            p3Data = ShopHelper.displayFeaturedPageProducts("Timberland Boots", productName3, productPrice3, productImg3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //product 2 product page
        p3CardView = view.findViewById(R.id.card_fp3);
        p3CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProductPageActivity.class);
                i.putExtra("product_data", p3Data);
                startActivity(i);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}