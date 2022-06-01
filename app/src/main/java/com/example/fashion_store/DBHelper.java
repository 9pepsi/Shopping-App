package com.example.fashion_store;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DBHelper {
    private static DBHelper instance;
    private Connection conn;
    private PreparedStatement preparedStmt;

    private DBHelper() throws SQLException {
        String URL = "jdbc:mysql://6fog8ps1vhie.eu-central-1.psdb.cloud/fashion?sslMode=VERIFY_IDENTITY";
        String USER = "976lue5559j9";
        String PASS = "pscale_pw_DBcIA6lGiGx3dHUNcWljDhnpXqKvcYYVog8iDUEweMM";
        this.conn = DriverManager.getConnection(URL, USER, PASS);
    }

    public Connection getConnection() {
        return conn;
    }

    public static DBHelper getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBHelper();
        } else if (instance.getConnection().isClosed()) {
            instance = new DBHelper();
        }
        return instance;
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

        //return user data
        return userData;
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


        return count;
    }

    public String[][] getCategoryProducts(String productCategory) throws SQLException {
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
        int count = DBHelper.getInstance().getCategoryProductsCount(productCategory);
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

        return count;
    }

    public String[][] getSearchProducts(String productQuery) throws SQLException {
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
        int count = DBHelper.getInstance().getSearchProductsCount(productQuery);
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
        //return id
        return user_id;
    }

    public boolean addOrUpdateCart(ProductCart productCart) throws SQLException{
        int user_id = DBHelper.getInstance().getLoggedUserID();
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
        }
        return true;
    }

    public int getUserCartCount() throws SQLException {
        int user_id = DBHelper.getInstance().getLoggedUserID();
        String query = "SELECT count(*) FROM cart_data WHERE user_id = ?";

        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, Integer.toString(user_id));

        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();
        rs.next();
        int userCartCount = rs.getInt(1);

        rs.close();

        return userCartCount;
    }

    public String[][] getUserCart() throws SQLException {
        int user_id = DBHelper.getInstance().getLoggedUserID();
        int cartCount = DBHelper.getInstance().getUserCartCount();
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

        return userCart;


    }

    public boolean updateItemQuantity(String newQuantity, String productName) throws SQLException {
        int user_id = DBHelper.getInstance().getLoggedUserID();
        String query = "UPDATE cart_data " +
                "SET product_quantity = ? " +
                "WHERE user_id = ? AND product_name = ?";

        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, newQuantity);
        preparedStmt.setString(2, Integer.toString(user_id));
        preparedStmt.setString(3, productName);

        preparedStmt.execute();

        return true;
    }

    public boolean deleteCartItem (String productName) throws SQLException {
        int user_id = DBHelper.getInstance().getLoggedUserID();
        String query = "DELETE FROM cart_data WHERE user_id = ? AND product_name = ?";

        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, Integer.toString(user_id));
        preparedStmt.setString(2, productName);

        preparedStmt.execute();

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean confirmOrder(String orderQuantity, String orderPrice) throws SQLException {
        //create order
            int user_id = DBHelper.getInstance().getLoggedUserID();
            int order_id = Math.abs(LocalDateTime.now().hashCode());
            String order_date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

            //query
            String query = "INSERT INTO orders (user_id, order_id, order_quantity, order_total_price, order_date) " +
                "VALUES (?,?,?,?,?)";

            //prepare statement
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, Integer.toString(user_id));
            preparedStmt.setString(2, Integer.toString(order_id));
            preparedStmt.setString(3, orderQuantity);
            preparedStmt.setString(4, orderPrice);
            preparedStmt.setString(5, order_date);

            //execute
            preparedStmt.execute();
            //close
            preparedStmt.close();

          //create order data
        //query
        String query2 = "INSERT INTO order_data (user_id, order_id, product_name, product_price, product_quantity, order_date) " +
                "SELECT user_id, ?, product_name, product_price, product_quantity, ? " +
                "FROM cart_data " +
                "WHERE user_id = ?";
        //prepare Statement
        preparedStmt = conn.prepareStatement(query2);
        preparedStmt.setString(1, Integer.toString(order_id));
        preparedStmt.setString(2, order_date);
        preparedStmt.setString(3, Integer.toString(user_id));
        //execute
        preparedStmt.execute();
        //close
        preparedStmt.close();

        //delete cart data
        String query3 = "DELETE FROM cart_data WHERE user_id = ?";
        //prepare statement
        preparedStmt = conn.prepareStatement(query3);
        preparedStmt.setString(1, Integer.toString(user_id));
        //execute
        preparedStmt.execute();
        //close
        preparedStmt.close();

        return true;
    }

    public String getUserEmail() throws  SQLException {
        int user_id = DBHelper.getInstance().getLoggedUserID();
        String user_email;
        //query
        String query = "SELECT user_email FROM auth_login WHERE id = ?";

        //prepare statement
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, Integer.toString(user_id));

        //execute
        preparedStmt.execute();

        //get result
        ResultSet rs = preparedStmt.getResultSet();
        rs.next();

        user_email = rs.getString("user_email");

        //close
        preparedStmt.close();

        return user_email;
    }

    public String getUserName() throws  SQLException {
        int user_id = DBHelper.getInstance().getLoggedUserID();
        String user_name;
        //query
        String query = "SELECT user_name FROM auth_login WHERE id = ?";

        //prepare statement
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, Integer.toString(user_id));

        //execute
        preparedStmt.execute();

        //get result
        ResultSet rs = preparedStmt.getResultSet();
        rs.next();

        user_name = rs.getString("user_name");

        //close
        preparedStmt.close();

        return user_name;
    }

    public boolean setUserAddress(String address) throws SQLException {
        int user_id = DBHelper.getInstance().getLoggedUserID();
        //query
        String query = "UPDATE auth_login " +
                "SET user_address = ? " +
                "WHERE id = ?";

        //prepare statement
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, address);
        preparedStmt.setString(2, Integer.toString(user_id));

        //execute
        preparedStmt.execute();

        //close
        preparedStmt.close();

        return true;
    }

    public String getUserAddress() throws SQLException{
        String user_id = Integer.toString(DBHelper.getInstance().getLoggedUserID());

        String query = "SELECT user_address FROM auth_login WHERE id = ?";

        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, user_id);

        preparedStmt.execute();

        ResultSet rs = preparedStmt.getResultSet();
        rs.next();

        return rs.getString("user_address");
    }

    public  int getUserOrdersCount() throws SQLException{
        int user_id = DBHelper.getInstance().getLoggedUserID();
        //query
        String query = "SELECT count(*) FROM orders WHERE user_id = ?";

        //prepare statement
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, Integer.toString(user_id));

        //execute
        preparedStmt.execute();

        ResultSet rs = preparedStmt.getResultSet();
        rs.next();

        return rs.getInt(1);
    }

    public String[][] getUserOrders() throws SQLException{
        int user_id = DBHelper.getInstance().getLoggedUserID();
        int count = DBHelper.getInstance().getUserOrdersCount();
        String[][] orders = new String[count][6];
        //query
        String query = "SELECT * FROM orders WHERE user_id = ?";

        //prepare statement
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, Integer.toString(user_id));

        //execute
        preparedStmt.execute();

        ResultSet rs = preparedStmt.getResultSet();

        int i = 0;
        while (rs.next()){
            orders[i][0] = rs.getString("id");
            orders[i][1] = rs.getString("user_id");
            orders[i][2] = rs.getString("order_id");
            orders[i][3] = rs.getString("order_quantity");
            orders[i][4] = rs.getString("order_total_price");
            orders[i][5] = rs.getString("order_date");
            i++;
        }
        preparedStmt.close();

        return orders;
    }

    public  int getUserOrdersCountThisMonth() throws SQLException{
        int user_id = DBHelper.getInstance().getLoggedUserID();
        //query
        String query = "SELECT count(*) FROM orders " +
                "WHERE order_date <= CURDATE() AND order_date >= (CURDATE() - INTERVAL 1 MONTH)" +
                "AND user_id = ? " +
                "ORDER BY order_date DESC";

        //prepare statement
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, Integer.toString(user_id));

        //execute
        preparedStmt.execute();

        ResultSet rs = preparedStmt.getResultSet();
        rs.next();

        return rs.getInt(1);
    }

    public String[][] getUserOrdersThisMonth() throws SQLException{
        int user_id = DBHelper.getInstance().getLoggedUserID();
        int count = DBHelper.getInstance().getUserOrdersCountThisMonth();
        String[][] orders = new String[count][6];
        //query
        String query = "SELECT * FROM orders " +
                "WHERE order_date <= CURDATE() AND order_date >= (CURDATE() - INTERVAL 1 MONTH) " +
                "AND user_id = ? " +
                "ORDER BY order_date DESC";

        //prepare statement
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, Integer.toString(user_id));

        //execute
        preparedStmt.execute();

        ResultSet rs = preparedStmt.getResultSet();

        int i = 0;
        while (rs.next()){
            orders[i][0] = rs.getString("id");
            orders[i][1] = rs.getString("user_id");
            orders[i][2] = rs.getString("order_id");
            orders[i][3] = rs.getString("order_quantity");
            orders[i][4] = rs.getString("order_total_price");
            orders[i][5] = rs.getString("order_date");
            i++;
        }
        preparedStmt.close();

        return orders;
    }

    public String getCurrentDate() throws SQLException{
        String query = "SELECT CURDATE()";
        preparedStmt = conn.prepareStatement(query);

        preparedStmt.execute();

        ResultSet rs = preparedStmt.getResultSet();
        rs.next();

        return rs.getString(1);
    }

    public String getLastMonthDate() throws SQLException{
        String query = "SELECT CURDATE() - INTERVAL 1 MONTH";
        preparedStmt = conn.prepareStatement(query);

        preparedStmt.execute();

        ResultSet rs = preparedStmt.getResultSet();
        rs.next();

        return rs.getString(1);
    }

    public int getOrderDetailsItemCount(String order_id) throws SQLException{
        int user_id = DBHelper.getInstance().getLoggedUserID();

        String query = "SELECT count(*) FROM order_data WHERE order_id = ? AND user_id = ?";

        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, order_id);
        preparedStmt.setString(2, Integer.toString(user_id));

        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();
        rs.next();

        return rs.getInt(1);
    }

    public String[][] getOrderDetailsItem(String order_id) throws SQLException{
        int user_id = DBHelper.getInstance().getLoggedUserID();
        int count = DBHelper.getInstance().getOrderDetailsItemCount(order_id);
        String[][] orderItems = new String[count][7];

        String query = "SELECT * FROM order_data WHERE order_id = ? AND user_id = ?";

        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, order_id);
        preparedStmt.setString(2, Integer.toString(user_id));

        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();

        int i = 0;
        while (rs.next()){
            orderItems[i][0] = rs.getString("id");
            orderItems[i][1] = rs.getString("user_id");
            orderItems[i][2] = rs.getString("order_id");
            orderItems[i][3] = rs.getString("product_name");
            orderItems[i][4] = rs.getString("product_price");
            orderItems[i][5] = rs.getString("product_quantity");
            orderItems[i][6] = rs.getString("order_date");
            i++;
        }
        preparedStmt.close();

        return orderItems;
    }
}
