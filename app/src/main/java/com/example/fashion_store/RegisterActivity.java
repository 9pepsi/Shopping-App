package com.example.fashion_store;

import android.app.DatePickerDialog;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            boolean registerComplete = false;
            //submit to database
            try {
                DBHelper db = new DBHelper();
                registerComplete = db.registerUser(Name, Email, Password, DOB);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            //check if registration went through
            if(registerComplete){
                new StoreDialogs().backToLoginDialog(this,"Registration Complete!").show();
            }else{
                Toast.makeText(RegisterActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });
    }
}