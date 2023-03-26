import java.sql.*;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class Phonebook {
    private Connection connection;

    public Phonebook() {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:phonebook.sqlite";
            connection = DriverManager.getConnection(dbURL);
            if (connection != null) {
                System.out.println("Connected to the database");
                DatabaseMetaData dm = (DatabaseMetaData) connection.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());

                String createTableSQL = "CREATE TABLE IF NOT EXISTS phonebook (name TEXT, phoneNumber TEXT)";
                PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL);
                preparedStatement.execute();

            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addEntry(Contact contact) {
        try {
            String addEntrySQL = "INSERT INTO phonebook (name, phoneNumber) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(addEntrySQL);
            preparedStatement.setString(1, contact.getName());
            preparedStatement.setString(2, contact.getPhoneNumber());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getPhoneNumber(String name) {
        try {
            String getPhoneNumberSQL = "SELECT phoneNumber FROM phonebook WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(getPhoneNumberSQL);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String phoneNumber = resultSet.getString("phoneNumber");
                System.out.println(name + ": " + phoneNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getName(String phoneNumber) {
        try {
            String getNameSQL = "SELECT name FROM phonebook WHERE phoneNumber = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(getNameSQL);
            preparedStatement.setString(1, phoneNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                System.out.println(name + ": " + phoneNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> searchContacts(String query) {
        try {
            String searchSQL = "SELECT * FROM phonebook WHERE name LIKE ? or phoneNumber LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(searchSQL);
            preparedStatement.setString(1, "%" + query + "%");
            preparedStatement.setString(2, "%" + query + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> contacts = new ArrayList<String>();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phoneNumber");
                contacts.add(name + ": " + phoneNumber);
            }
            if (contacts.size() == 0) {
                return null;
            } else {
                return contacts;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteEntry(String name) {
        try {
            String deleteEntrySQL = "DELETE FROM phonebook WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteEntrySQL);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEntry(String name, String newPhoneNumber) {
        try {
            String updateEntrySQL = "UPDATE phonebook SET phoneNumber = ? WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateEntrySQL);
            preparedStatement.setString(1, newPhoneNumber);
            preparedStatement.setString(2, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> displayAllContacts() {
        try {
            String selectAllSQL = "SELECT * FROM phonebook";
            PreparedStatement preparedStatement = connection.prepareStatement(selectAllSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> results = new ArrayList<>();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phoneNumber");
                results.add(name + ": " + phoneNumber);
            }
            if (results.isEmpty()) {
                return null;
            } else {
                return results;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void exportToCsv(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);

            String selectAllSQL = "SELECT * FROM phonebook";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAllSQL);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // for (int i = 1; i <= columnCount; i++) {
            //     if (i > 1) {
            //         writer.append(",");
            //     }
            //     String columnName = metaData.getColumnLabel(i);
            //     writer.append(columnName);
            // }
            // writer.append("\n");

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) {
                        writer.append(",");
                    }
                    String columnValue = resultSet.getString(i);
                    writer.append(columnValue);
                }
                writer.append("\n");
            }

            writer.flush();
            writer.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void importFromCsv(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
    
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                String name = values[0];
                String phoneNumber = values[1];
                // addEntry(name, phoneNumber);
            }
    
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}
