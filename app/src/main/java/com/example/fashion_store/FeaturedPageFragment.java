package com.example.fashion_store;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

    ProgressDialog progressDialog;


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
            //display products
        class showFeaturedProductsTask extends AsyncTask<String, Void, String[][]> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(view.getContext(), "", "Loading Products...");

            }

            @Override
            protected String[][] doInBackground(String... productName) {
                String[][] pData = new String[3][5];
                try {
                    pData = new DBHelper().getFeaturedProducts(productName[0], productName[1], productName[2]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return pData;
            }

            @Override
            protected void onPostExecute(String[][] productData) {
                if(productData[0][0] == null){
                    Toast.makeText(getContext(), "Failed to fetch data.", Toast.LENGTH_LONG).show();
                }else {
                    String price1 = productPrice1.getText().toString().replace("{PLACEHOLDER PRICE}", productData[0][1]);
                    String price2 = productPrice2.getText().toString().replace("{PLACEHOLDER PRICE}", productData[1][1]);
                    String price3 = productPrice3.getText().toString().replace("{PLACEHOLDER PRICE}", productData[2][1]);

                    productName1.setText(productData[0][0]);
                    productPrice1.setText(price1);
                    Picasso.get().load(Uri.parse(productData[0][2])).into(productImg1);
                    p1Data = Arrays.copyOf(productData[0], 5);

                    productName2.setText(productData[1][0]);
                    productPrice2.setText(price2);
                    Picasso.get().load(Uri.parse(productData[1][2])).into(productImg2);
                    p2Data = Arrays.copyOf(productData[1], 5);

                    productName3.setText(productData[2][0]);
                    productPrice3.setText(price3);
                    Picasso.get().load(Uri.parse(productData[2][2])).into(productImg3);
                    p3Data = Arrays.copyOf(productData[2], 5);
                }

                progressDialog.dismiss();
            }
        }
        new showFeaturedProductsTask().execute("Apple Watch","Placeholder Shirt","Timberland Boots");


            //product 1 product page
        p1CardView = view.findViewById(R.id.card_fp1);
        p2CardView = view.findViewById(R.id.card_fp2);
        p3CardView = view.findViewById(R.id.card_fp3);

        p1CardView.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), ProductPageActivity.class);
            i.putExtra("product_data", p1Data);
            startActivity(i);
        });

            //product 2 product page
        p2CardView.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), ProductPageActivity.class);
            i.putExtra("product_data", p2Data);
            startActivity(i);
        });
        //product 3 product page
        p3CardView.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), ProductPageActivity.class);
            i.putExtra("product_data", p3Data);
            startActivity(i);
        });
        // Inflate the layout for this fragment
        return view;
    }
}