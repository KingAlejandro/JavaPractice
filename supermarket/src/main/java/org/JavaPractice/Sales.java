package org.JavaPractice;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Sales {
    private Connection connection;

    public Sales(Connection connection) {
        this.connection = connection;
    }

    public List<Sale> getAllSales() throws SQLException {
        List<Sale> saleList = new ArrayList<>();

        String query = "SELECT * FROM sales";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String saleID = resultSet.getString("saleID");
                String userID = resultSet.getString("userID");
                String productID = resultSet.getString("productID");
                int quantitySold = resultSet.getInt("quantitySold");
                double priceAtSale = resultSet.getDouble("priceAtSale");
                Date saleDate = resultSet.getDate("saleDate");

                Sale sale = new Sale(saleID, userID, productID, quantitySold, priceAtSale, saleDate);
                saleList.add(sale);
            }
        }

        return saleList;
    }

    public List<Sale> getSalesWithinPeriod(Date startDate, Date endDate) throws SQLException {
        List<Sale> saleList = new ArrayList<>();

        String query = "SELECT * FROM sales WHERE saleDate BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new java.sql.Date(startDate.getTime()));
            statement.setDate(2, new java.sql.Date(endDate.getTime()));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String saleID = resultSet.getString("saleID");
                String userID = resultSet.getString("userID");
                String productID = resultSet.getString("productID");
                int quantitySold = resultSet.getInt("quantitySold");
                double priceAtSale = resultSet.getDouble("priceAtSale");
                Date saleDate = resultSet.getDate("saleDate");

                Sale sale = new Sale(saleID, userID, productID, quantitySold, priceAtSale, saleDate);
                saleList.add(sale);
            }
        }

        return saleList;
    }

    public boolean addSale(String userID, String productID, int quantitySold, double priceAtSale, Date saleDate) throws SQLException {
        String query = "INSERT INTO sales (saleID, userID, productID, quantitySold, priceAtSale, saleDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            String saleID = UUID.randomUUID().toString();
            statement.setString(1, saleID);
            statement.setString(2, userID);
            statement.setString(3, productID);
            statement.setInt(4, quantitySold);
            statement.setDouble(5, priceAtSale);
            statement.setDate(6, new java.sql.Date(saleDate.getTime()));

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }
}
