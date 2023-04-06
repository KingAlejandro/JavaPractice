package org.JavaPractice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Products {
    private Connection connection;

    public Products(Connection connection) {
        this.connection = connection;
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> productList = new ArrayList<>();

        String query = "SELECT * FROM products";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String productID = resultSet.getString("productID");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                double weight = resultSet.getDouble("weight");
                int quantity = resultSet.getInt("quantity");

                Product product = new Product(productID, name, price, weight, quantity);
                productList.add(product);
            }
        }

        return productList;
    }

    public boolean addProduct(String name, double price, double weight, int quantity) throws SQLException {
        String query = "INSERT INTO products (productID, name, price, weight, quantity) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            String productID = UUID.randomUUID().toString();
            statement.setString(1, productID);
            statement.setString(2, name);
            statement.setDouble(3, price);
            statement.setDouble(4, weight);
            statement.setInt(5, quantity);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }
}