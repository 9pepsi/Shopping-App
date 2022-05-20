package com.example.fashion_store;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class StoreMainPageActivity extends AppCompatActivity {
    ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_main_page);

//        imageSlider = findViewById(R.id.slider);
//        List<SlideModel> slideModels = new ArrayList<>();
//        slideModels.add(new SlideModel("https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/fbaf991a78bc4896a3e9ad7800abcec6_9366/Ultraboost_22_Shoes_Black_GZ0127_01_standard.jpg"));
//        slideModels.add(new SlideModel("https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/ccfbacc1e64547029d9cad7800abfd6f_9366/Ultraboost_22_Shoes_Black_GZ0127_41_detail.jpg"));
//        slideModels.add(new SlideModel("https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/8d63a3ef05e746a4abc2ad7800abd65f_9366/Ultraboost_22_Shoes_Black_GZ0127_06_standard.jpg"));
//        imageSlider.setImageList(slideModels, true);
    }
}