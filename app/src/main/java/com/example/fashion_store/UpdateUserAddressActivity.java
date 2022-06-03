package com.example.fashion_store;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public class UpdateUserAddressActivity extends AppCompatActivity {

    EditText addressText;
    Button updateAddressBT;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_address);

        addressText = findViewById(R.id.enter_address_text);
        updateAddressBT = findViewById(R.id.update_address_bt);

        //update address task
        class updateAddressTask extends AsyncTask<String, Void, Boolean>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(UpdateUserAddressActivity.this, "", "Updating Address...");
            }

            @Override
            protected Boolean doInBackground(String... address) {
                boolean isUpdated = false;
                try {
                    isUpdated = DBHelper.getInstance().setUserAddress(address[0]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return isUpdated;
            }

            @Override
            protected void onPostExecute(Boolean isUpdated) {
                progressDialog.dismiss();
                if(isUpdated){
                    new StoreDialogs().addressUpdatedDialog(UpdateUserAddressActivity.this, "Address Updated").show();
                }else{
                    Toast.makeText(UpdateUserAddressActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        updateAddressBT.setOnClickListener(view -> {
            String address = addressText.getText().toString();
            new updateAddressTask().execute(address);
        });
    }
}