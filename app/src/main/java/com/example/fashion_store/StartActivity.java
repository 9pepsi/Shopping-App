package com.example.fashion_store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.sql.SQLException;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //create db instance
        try {
            DBHelper.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Splash Screen duration
        int msDelay = 1000;
        new Handler().postDelayed(() -> {
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
            finish();
        }, msDelay);
    }
}