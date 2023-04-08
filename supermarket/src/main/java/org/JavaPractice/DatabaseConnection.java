package org.JavaPractice;

import java.sql.*;

public class DatabaseConnection {
    private Connection connection;

    public DatabaseConnection(String dbURL) {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Connect to the database
            connection = DriverManager.getConnection(dbURL);

            if (connection != null) {
                // Print information about the database connection
                System.out.println("Connected to the database");
                DatabaseMetaData dm = (DatabaseMetaData) connection.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());

                // SQL command to create the users table if it doesn't exist
                String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (userID TEXT PRIMARY KEY, name TEXT, email TEXT UNIQUE, password TEXT, balance REAL, type TEXT)";

                // Create a PreparedStatement to execute the SQL command
                PreparedStatement preparedStatement = connection.prepareStatement(createUsersTableSQL);

                // Execute the SQL command
                preparedStatement.execute();

                // SQL command to create the products table if it doesn't exist
                String createProductsTableSQL = "CREATE TABLE IF NOT EXISTS products (productID TEXT PRIMARY KEY, name TEXT, price REAL, cost REAL, weight REAL, quantity INTEGER)";

                // Create a PreparedStatement to execute the SQL command
                preparedStatement = connection.prepareStatement(createProductsTableSQL);

                // Execute the SQL command
                preparedStatement.execute();

                // SQL command to create the sales table if it doesn't exist
                String createSalesTableSQL = "CREATE TABLE IF NOT EXISTS sales (saleID TEXT PRIMARY KEY, userID TEXT, productID TEXT, quantitySold INTEGER, priceAtSale REAL, costAtSale REAL, saleDate TIMESTAMP, FOREIGN KEY(userID) REFERENCES users(userID), FOREIGN KEY(productID) REFERENCES products(productID))";

                // Create a PreparedStatement to execute the SQL command
                preparedStatement = connection.prepareStatement(createSalesTableSQL);

                // Execute the SQL command
                preparedStatement.execute();
            }
        } catch (ClassNotFoundException ex) {
            // Print the stack trace if a ClassNotFoundException occurs
            ex.printStackTrace();
        } catch (SQLException ex) {
            // Print the stack trace if an SQLException occurs
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
