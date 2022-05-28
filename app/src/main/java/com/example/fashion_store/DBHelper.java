package com.example.fashion_store;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {
    private static final String URL = "jdbc:mysql://b0w4c2e3ott0.eu-central-1.psdb.cloud/fashion?sslMode=VERIFY_IDENTITY";
    private static final String USER = "hpcov5w7asni";
    private static final String PASSWORD = "pscale_pw_WwCDT5EcjXvawkEzQJtq4hNIPoBPvM65X3CSh05OSrc";
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

    public int getLoggedUserID() throws SQLException {
        //get logged in user DB id
        int user_id = -1;
        SharedPreferences sharedPreferences = LoginActivity.getContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String user_email = sharedPreferences.getString("loginEmail","");
        //get id query
        String query = "SELECT id " +
                "FROM auth_login " +
                "WHERE user_email = ?";
        //prepare statement
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, user_email);
        //execute and get id
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();
        rs.next();
        user_id = rs.getInt("id");
        //close connection
        rs.close();
        this.closeConnection();
        //return id
        return user_id;
    }

    public void addOrUpdateCart(ProductCart productCart) throws SQLException{
        int user_id = new DBHelper().getLoggedUserID();
        String product_name = productCart.product_name;
        String product_price = productCart.product_price;
        String product_img = productCart.product_image;
        String product_quantity = productCart.product_quantity;
        boolean doesExist = false;
        //check if already exists in cart
        String query = "SELECT EXISTS(SELECT * FROM cart_data " +
                "WHERE user_id= ? AND product_name=? )";
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, Integer.toString(user_id));
        preparedStmt.setString(2, product_name);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();
        rs.next();
        doesExist = rs.getBoolean(1);

        //clean up
        rs.close();


        //update quantity or add to cart if doesn't exist
        if (doesExist){
            //if exists update quantity
            String query2 = "UPDATE cart_data " +
                    "SET product_quantity = ? " +
                    "WHERE user_id = ? AND product_name = ?";

            preparedStmt = conn.prepareStatement(query2);
            preparedStmt.setString(1, product_quantity);
            preparedStmt.setString(2, Integer.toString(user_id));
            preparedStmt.setString(3, product_name);

            preparedStmt.execute();
            this.closeConnection();
        }else {
            //if doesn't exists, add to cart data
            String query3 = "INSERT INTO cart_data (user_id, product_name, product_price, " +
                    "product_img, product_quantity) VALUES (?,?,?,?,?)";

            preparedStmt = conn.prepareStatement(query3);
            preparedStmt.setString(1, Integer.toString(user_id));
            preparedStmt.setString(2, product_name);
            preparedStmt.setString(3, product_price);
            preparedStmt.setString(4, product_img);
            preparedStmt.setString(5, product_quantity);

            preparedStmt.execute();
            this.closeConnection();
        }
    }

    public int getUserCartCount() throws SQLException {
        int user_id = new DBHelper().getLoggedUserID();
        String query = "SELECT count(*) FROM cart_data WHERE user_id = ?";

        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, Integer.toString(user_id));

        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();
        rs.next();
        int userCartCount = rs.getInt(1);

        rs.close();
        this.closeConnection();

        return userCartCount;
    }

    public String[][] getUserCart() throws SQLException {
        int user_id = new DBHelper().getLoggedUserID();
        int cartCount = new DBHelper().getUserCartCount();
        String query = "SELECT * FROM cart_data WHERE user_id = ?";

        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, Integer.toString(user_id));

        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();

        String[][] userCart = new String[cartCount][6];
        int i = 0;
        while (rs.next()){
            userCart[i][0] = rs.getString("id");
            userCart[i][1] = rs.getString("user_id");
            userCart[i][2] = rs.getString("product_name");
            userCart[i][3] = rs.getString("product_price");
            userCart[i][4] = rs.getString("product_img");
            userCart[i][5] = rs.getString("product_quantity");
            i++;
        }
        rs.close();
        this.closeConnection();

        return userCart;


    }

    public void updateItemQuantity(String newQuantity, String productName) throws SQLException {
         int user_id = new DBHelper().getLoggedUserID();
         String query = "UPDATE cart_data " +
                 "SET product_quantity = ? " +
                 "WHERE user_id = ? AND product_name = ?";

         preparedStmt = conn.prepareStatement(query);
         preparedStmt.setString(1, newQuantity);
         preparedStmt.setString(2, Integer.toString(user_id));
         preparedStmt.setString(3, productName);

         preparedStmt.execute();

         this.closeConnection();
    }

    public void deleteCartItem (String productName) throws SQLException {
         int user_id = new DBHelper().getLoggedUserID();
         String query = "DELETE FROM cart_data WHERE user_id = ? AND product_name = ?";

         preparedStmt = conn.prepareStatement(query);
         preparedStmt.setString(1, Integer.toString(user_id));
         preparedStmt.setString(2, productName);

         preparedStmt.execute();

         this.closeConnection();
    }

    public void closeConnection() throws SQLException {
        conn.close();
        preparedStmt.close();
    }
}
