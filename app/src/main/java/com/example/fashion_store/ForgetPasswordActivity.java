package com.example.fashion_store;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        class userPasswordReset extends AsyncTask<String, Void, Boolean>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(ForgetPasswordActivity.this, "", "Resetting Password...");
            }

            @Override
            protected Boolean doInBackground(String... userData) {
                boolean isReset = false;
                try {
                     isReset = DBHelper.getInstance().resetPassword(userData[0], userData[1]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return isReset;
            }

            @Override
            protected void onPostExecute(Boolean isReset) {
                progressDialog.dismiss();

                if(isReset){
                    new StoreDialogs().backToLoginDialog(ForgetPasswordActivity.this,"Password Reset Successfully!").show();
                }else {
                    Toast.makeText(ForgetPasswordActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
                new userPasswordReset().execute(email, newPass);
            }
        });
    }
}