package com.example.fashion_store;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

public class StoreDialogs {

    public AlertDialog backToLoginDialog(Context context, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Back to login", (dialog, which) -> {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
        });

        return builder.create();
    }

}