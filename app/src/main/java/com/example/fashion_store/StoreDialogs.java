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

    public AlertDialog orderConfirmedDialog(Context context, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Back to Store", (dialog, which) -> {
            Intent i = new Intent(context, StoreMainPageActivity.class);
            context.startActivity(i);
        });

        return builder.create();
    }

    public AlertDialog signOutDialog(Context context, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);
        builder.setCancelable(true);
        builder.setPositiveButton("Sign out", (dialog, which) -> {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        });

        return builder.create();
    }

    public AlertDialog addressUpdatedDialog(Context context, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Back to Store", (dialog, which) -> {
            Intent i = new Intent(context, StoreMainPageActivity.class);
            context.startActivity(i);
        });

        return builder.create();
    }

}