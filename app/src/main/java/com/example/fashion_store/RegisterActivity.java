package com.example.fashion_store;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    Button login;
    ImageButton executeRegister;
    EditText userName;
    EditText userEmail;
    EditText userPassword;
    EditText userBirthday;
    int mYear, mMonth, mDay;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        class registerUserTask extends AsyncTask<String, Void, Boolean>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(RegisterActivity.this, "", "Signing up...");
            }

            @Override
            protected Boolean doInBackground(String... userData) {
                boolean registerComplete = false;
                try {
                    registerComplete = new DBHelper().registerUser(userData[0], userData[1], userData[2], userData[3]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return registerComplete;
            }

            @Override
            protected void onPostExecute(Boolean registerComplete) {
                progressDialog.dismiss();

                if(registerComplete){
                    new StoreDialogs().backToLoginDialog(RegisterActivity.this,"Registration Complete!").show();
                }else{
                    Toast.makeText(RegisterActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //declare vars
        login = findViewById(R.id.back_to_login);
        executeRegister = findViewById(R.id.sign_up_bt);
        userName = findViewById(R.id.reg_name);
        userEmail = findViewById(R.id.reg_email);
        userPassword = findViewById(R.id.reg_password);
        userBirthday = findViewById(R.id.dob);
        //back to login
        login.setOnClickListener(view -> {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
        });
        //show birthday picker
        userBirthday.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mYear = calendar.get(Calendar.DAY_OF_MONTH);

            //show dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, (view1, year, month, dayOfMonth) ->
                    userBirthday.setText(getString(R.string.birthday_Date, dayOfMonth, month+1, year)),
                    mYear, mMonth, mDay);
            datePickerDialog.show();
        });
        //register user
        executeRegister.setOnClickListener(view -> {
            //get data
            String Name = userName.getText().toString();
            String Email = userEmail.getText().toString();
            String Password = userPassword.getText().toString();
            String DOB = userBirthday.getText().toString();
            //register user
            new registerUserTask().execute(Name, Email, Password, DOB);

        });
    }
}