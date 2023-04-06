package org.JavaPractice;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Sales {
    private Connection connection;

    public Sales(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Sale> getAllSales() throws SQLException {
        ArrayList<Sale> saleList = new ArrayList<>();

        String query = "SELECT * FROM sales";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String saleID = resultSet.getString("saleID");
                String userID = resultSet.getString("userID");
                String productID = resultSet.getString("productID");
                int quantitySold = resultSet.getInt("quantitySold");
                double priceAtSale = resultSet.getDouble("priceAtSale");
                LocalDateTime saleDate = resultSet.getTimestamp("saleDate").toLocalDateTime();

                Sale sale = new Sale(saleID, userID, productID, quantitySold, priceAtSale, saleDate);
                saleList.add(sale);
            }
        }

        return saleList;
    }

    public ArrayList<Sale> getSalesWithinPeriod(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        ArrayList<Sale> saleList = new ArrayList<>();

        String query = "SELECT * FROM sales WHERE saleDate BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(startDate));
            statement.setTimestamp(2, Timestamp.valueOf(endDate));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String saleID = resultSet.getString("saleID");
                String userID = resultSet.getString("userID");
                String productID = resultSet.getString("productID");
                int quantitySold = resultSet.getInt("quantitySold");
                double priceAtSale = resultSet.getDouble("priceAtSale");
                LocalDateTime saleDate = resultSet.getTimestamp("saleDate").toLocalDateTime();

                Sale sale = new Sale(saleID, userID, productID, quantitySold, priceAtSale, saleDate);
                saleList.add(sale);
            }
        }

        return saleList;
    }

    public boolean addSale(String userID, String productID, int quantitySold, double priceAtSale, LocalDateTime saleDate) throws SQLException {
        String query = "INSERT INTO sales (saleID, userID, productID, quantitySold, priceAtSale, saleDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            String saleID = UUID.randomUUID().toString();
            statement.setString(1, saleID);
            statement.setString(2, userID);
            statement.setString(3, productID);
            statement.setInt(4, quantitySold);
            statement.setDouble(5, priceAtSale);
            statement.setTimestamp(6, Timestamp.valueOf(saleDate));

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }
}
