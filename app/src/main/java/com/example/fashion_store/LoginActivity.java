package com.example.fashion_store;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    Button signUp;
    Button forgetPW;
    ImageButton signIn;
    EditText loginEmail;
    EditText loginPW;
    CheckBox rememberMe;
    SharedPreferences loginPrefs;
    SharedPreferences.Editor loginPrefsEditor;
    boolean saveLogin;
    boolean isUser = false;
    ProgressDialog progressDialog;
    String[] userData = new String[4];
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //login task
        class userLoginTask extends AsyncTask<String, Void, Boolean>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...");
            }

            @Override
            protected Boolean doInBackground(String... userCredentials) {
                try {
                    isUser = new DBHelper().loginUser(userCredentials[0], userCredentials[1]);
                    userData = new DBHelper().getUserData(userCredentials[0], userCredentials[1]);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return isUser;
            }

            @Override
            protected void onPostExecute(Boolean isUser) {
                String Email = loginEmail.getText().toString();
                String Pass = loginPW.getText().toString();
                if(isUser){
                    if(rememberMe.isChecked()){
                        loginPrefsEditor.putBoolean("saveLogin",true);
                        loginPrefsEditor.putString("loginEmail",Email);
                        loginPrefsEditor.putString("loginPW",Pass);
                    }else{
                        loginPrefsEditor.clear();
                    }
                    loginPrefsEditor.apply();
                    Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, StoreMainPageActivity.class);
                    i.putExtra("user_data", userData);
                    startActivity(i);

                }else{
                    Toast.makeText(LoginActivity.this, "Wrong E-mail or Password.", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
            }
        }
        //vars
      signUp = findViewById(R.id.signup_page_bt);
      forgetPW = findViewById(R.id.forget_pw_page_bt);
      signIn = findViewById(R.id.sign_in_bt);
      loginEmail = findViewById(R.id.login_email);
      loginPW = findViewById(R.id.login_password);
      rememberMe = findViewById(R.id.remember_me);
      //remember me functionality sharedPrefs vars
      loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
      loginPrefsEditor = loginPrefs.edit();
      saveLogin = loginPrefs.getBoolean("saveLogin",false);
        //fill text field if remember me was set to true
        if(saveLogin){
          loginEmail.setText(loginPrefs.getString("loginEmail",""));
          loginPW.setText(loginPrefs.getString("loginPW",""));
          rememberMe.setChecked(true);
      }
        context = getApplicationContext();
      //to sign up
        signUp.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
      });
      //to forget password
        forgetPW.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(i);
        });
        //signing in
        signIn.setOnClickListener(view -> {
            String Email = loginEmail.getText().toString();
            String Pass = loginPW.getText().toString();
            String[] userCredentials = {Email, Pass};
            //check if user
            new userLoginTask().execute(userCredentials);

        });


    }

    public static Context getContext() {
        return context;
    }
}