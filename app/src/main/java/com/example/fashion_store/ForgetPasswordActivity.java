package com.example.fashion_store;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText resetEmail;
    EditText resetPassword;
    EditText retypePassword;
    Button ResetPwBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        //vars
        resetEmail = findViewById(R.id.reset_email);
        resetPassword = findViewById(R.id.reset_password);
        retypePassword = findViewById(R.id.reset_retype_password);
        ResetPwBT = findViewById(R.id.reset_bt);
        //reset password
        ResetPwBT.setOnClickListener(view -> {
            String email = resetEmail.getText().toString();
            String newPass = resetPassword.getText().toString();
            String retypePass = retypePassword.getText().toString();
            if (!newPass.equals(retypePass)){
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    boolean isReset = new MySQLHelper().resetPassword(email, newPass);
                    if(isReset){
                        new StoreDialogs().backToLoginDialog(this,"Password Reset Successfully!");
                    }else {Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });
    }
}