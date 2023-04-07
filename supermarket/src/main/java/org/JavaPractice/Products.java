package org.JavaPractice;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class Products {
    private Connection connection;

    public Products(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Product> getAllProducts() throws SQLException {
        ArrayList<Product> productList = new ArrayList<>();

        String query = "SELECT * FROM products";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String productID = resultSet.getString("productID");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                double cost = resultSet.getDouble("cost");
                double weight = resultSet.getDouble("weight");
                int quantity = resultSet.getInt("quantity");

                Product product = new Product(productID, name, price, cost, weight, quantity);
                productList.add(product);
            }
        }

        return productList;
    }

    public boolean addProduct(String name, double price, double cost, double weight, int quantity) throws SQLException {
        String query = "INSERT INTO products (productID, name, price, cost, weight, quantity) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            String productID = UUID.randomUUID().toString();
            statement.setString(1, productID);
            statement.setString(2, name);
            statement.setDouble(3, price);
            statement.setDouble(4, cost);
            statement.setDouble(5, weight);
            statement.setInt(6, quantity);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }
    public boolean updateProduct(Product product) throws SQLException {
        String query = "UPDATE products SET name = ?, price = ?, cost = ?, weight = ?, quantity = ? WHERE productID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setDouble(3, product.getCost());
            statement.setDouble(4, product.getWeight());
            statement.setInt(5, product.getQuantity());
            statement.setString(6, product.getProductID());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        }
    }
}