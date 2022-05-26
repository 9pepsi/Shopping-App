package com.example.fashion_store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {
    private static final String URL = "jdbc:mysql://0twdk0tywrk3.eu-central-2.psdb.cloud/fashion?sslMode=VERIFY_IDENTITY";
    private static final String USER = "woerlhlpnkt9";
    private static final String PASSWORD = "pscale_pw_hLozsoJu8cB5Go-mTHG5D2ifNn4vZiVQdaAzLaIPjpU";
    private Connection conn;
    private PreparedStatement preparedStmt;

    DBHelper() throws SQLException {
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
        String query = "SELECT EXISTS(SELECT user_email, user_password FROM auth_login" +
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

    public String[] getUserData(String email, String password) throws SQLException {
        //query
        String query = "SELECT * " +
                "FROM auth_login " +
                "WHERE user_email = ? AND user_password = ?";
        //prepare Statement
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, email);
        preparedStmt.setString(2, password);
        //execute
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();
        //get data
        String[] userData = new String[4];
        while (rs.next()){
            userData[0] = rs.getString("id");
            userData[1] = rs.getString("user_email");
            userData[2] = rs.getString("user_name");
            userData[3] = rs.getString("user_dob");
        }
        rs.close();
        //close connection
        this.closeConnection();
        //return user data
        return userData;
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
        rs.close();
        this.closeConnection();
        return productData;
    }

    public String[][] getFeaturedProducts(String productOne, String productTwo, String productThree) throws SQLException{
        //query
        String query = "SELECT * " +
                "FROM products " +
                "WHERE product_name = ? OR " +
                "product_name = ? OR " +
                "product_name = ?";
        //prepare statement
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, productOne);
        preparedStmt.setString(2, productTwo);
        preparedStmt.setString(3, productThree);
        //execute
        preparedStmt.execute();
        //get products
        ResultSet rs = preparedStmt.getResultSet();
        String[][] featuredProducts = new String[3][5];
        int count = 0;
        while (rs.next()){
            featuredProducts[count][0] = rs.getString("product_name");
            featuredProducts[count][1] = rs.getString("product_price");
            featuredProducts[count][2] = rs.getString("product_img");
            featuredProducts[count][3] = rs.getString("product_desc");
            featuredProducts[count][4] = rs.getString("product_category");
            count++;
        }
        rs.close();
        //close connection
        this.closeConnection();
        //return products
        return featuredProducts;
    }

    public int getCategoryProductsCount(String productCategory) throws  SQLException {
        //query
        String sql = "SELECT count(*) " +
                "FROM products " +
                "WHERE product_category = ? " ;
        //prepare statement
        preparedStmt = conn.prepareStatement(sql);
        preparedStmt.setString(1, productCategory);
        //execute
        preparedStmt.execute();
        //get result set
        ResultSet rs = preparedStmt.getResultSet();
        rs.next();
        //get product count
        int count = rs.getInt(1);
        //close
        rs.close();
        this.closeConnection();

        return count;
    }

    public String[][] displayCategoryProducts(String productCategory) throws SQLException {
        //query
        String sql = "SELECT * " +
                "FROM products " +
                "WHERE product_category = ? " +
                "ORDER BY product_name ASC";
        //prepare statement
        preparedStmt = conn.prepareStatement(sql);
        preparedStmt.setString(1, productCategory);
        //execute
        preparedStmt.execute();
        //get result set
        ResultSet rs = preparedStmt.getResultSet();
        //get product count
        int count = new DBHelper().getCategoryProductsCount(productCategory);
        //get products data
        String[][] productData = new String[count][5];
        int i = 0;
        while (rs.next()) {
            productData[i][0] = rs.getString("product_name");
            productData[i][1] = rs.getString("product_price");
            productData[i][2] = rs.getString("product_img");
            productData[i][3] = rs.getString("product_desc");
            productData[i][4] = rs.getString("product_category");
            i++;
        }

        rs.close();
        this.closeConnection();
        return productData;

    }

    public int getSearchProductsCount(String productQuery) throws  SQLException {
        //query
        String sql = "SELECT count(*) " +
                "FROM products " +
                "WHERE product_name LIKE CONCAT('%',?,'%') " ;
        //prepare statement
        preparedStmt = conn.prepareStatement(sql);
        preparedStmt.setString(1, productQuery);
        //execute
        preparedStmt.execute();
        //get result set
        ResultSet rs = preparedStmt.getResultSet();
        rs.next();
        //get product count
        int count = rs.getInt(1);
        //close
        rs.close();
        this.closeConnection();

        return count;
    }

    public String[][] displaySearchProducts(String productQuery) throws SQLException {
        //query
        String sql = "SELECT * " +
                "FROM products " +
                "WHERE product_name LIKE CONCAT('%',?,'%') " +
                "ORDER BY product_name ASC";
        //prepare statement
        preparedStmt = conn.prepareStatement(sql);
        preparedStmt.setString(1, productQuery);
        //execute
        preparedStmt.execute();
        //get result set
        ResultSet rs = preparedStmt.getResultSet();
        //get product count
        int count = new DBHelper().getSearchProductsCount(productQuery);
        //get products data
        String[][] productData = new String[count][5];
        int i = 0;
        while (rs.next()) {
            productData[i][0] = rs.getString("product_name");
            productData[i][1] = rs.getString("product_price");
            productData[i][2] = rs.getString("product_img");
            productData[i][3] = rs.getString("product_desc");
            productData[i][4] = rs.getString("product_category");
            i++;
        }

        rs.close();
        this.closeConnection();
        return productData;

    }


    public void closeConnection() throws SQLException {
        conn.close();
        preparedStmt.close();
    }
}
