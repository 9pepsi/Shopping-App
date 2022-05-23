package com.example.fashion_store;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {
    private static final String URL = "jdbc:mysql://d7mxitbtvngw.eu-central-1.psdb.cloud/fashion?sslMode=VERIFY_IDENTITY";
    private static final String USER = "hvdzcjfk0lpx";
    private static final String PASSWORD = "pscale_pw_D6V_mzODGFJ80_9XgOZkjJ0fCIZXjbwSD9JFZ1OLr6M";
    private Connection conn;
    private PreparedStatement preparedStmt;

    DBHelper() throws SQLException {
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public boolean registerUser(String name, String email, String password, String dob) throws SQLException {
        //query
        String query = "INSERT INTO auth_login(user_name, user_email, user_password, user_dob)" +
                "VALUES(?,?,?,?)";
        //insert values
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1,name);
        preparedStmt.setString(2,email);
        preparedStmt.setString(3,password);
        preparedStmt.setString(4,dob);
        //execute
        preparedStmt.execute();
        //close
        this.closeConnection();

        return true;
    }

    public boolean loginUser(String email, String password) throws SQLException {
        //query
        String query = "SELECT EXISTS(SELECT user_email, user_password FROM fashion_store.auth_login" +
                " WHERE user_email = ? AND user_password = ?)";
        //insert values
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, email);
        preparedStmt.setString(2, password);
        //execute
        preparedStmt.execute();
        //get result
        ResultSet rs = preparedStmt.getResultSet();
        rs.next();
        //check if exists
        boolean isUser = rs.getBoolean(1);
        //close connection
        this.closeConnection();

        return isUser;
    }

    public boolean resetPassword(String email, String password) throws SQLException {
        //query
        String query = "UPDATE auth_login SET user_password = ? WHERE user_email = ?";
        //insert values
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1,password);
        preparedStmt.setString(2,email);
        //execute
        int rows = preparedStmt.executeUpdate();
        //check update
        boolean isReset;
        isReset = rows != 0;
        //close connection
        this.closeConnection();

        return isReset;
    }

    public String[] displayProductData(String productName) throws SQLException {
        //query
        String query = "SELECT * " +
                "FROM products " +
                "WHERE product_name = ?";
        //prepare statement
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, productName);
        //execute
        preparedStmt.execute();
        //get result set
        ResultSet rs = preparedStmt.getResultSet();
        //product data
        String[] productData = new String[5];
        while (rs.next()){
            productData[0] = rs.getString("product_name");
            productData[1] = rs.getString("product_price");
            productData[2] = rs.getString("product_img");
            productData[3] = rs.getString("product_desc");
            productData[4] = rs.getString("product_category");
        }
        this.closeConnection();
        return productData;
    }

    public void closeConnection() throws SQLException {
        conn.close();
        preparedStmt.close();
    }
}
