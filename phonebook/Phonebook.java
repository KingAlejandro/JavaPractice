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

                String createTableSQL = "CREATE TABLE IF NOT EXISTS phonebook (id TEXT PRIMARY KEY, name TEXT, phoneNumber TEXT, email TEXT)";
                PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL);
                preparedStatement.execute();

            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

/**
 * Adds an entry to the phonebook database.
 *
 * @param contact the contact to be added
 */
public void addEntry(Contact contact) {
    try {
        // SQL command to insert a row into the phonebook table
        String addEntrySQL = "INSERT INTO phonebook (id, name, phoneNumber, email) VALUES (?, ?, ?, ?)";
        
        // Create a PreparedStatement to execute the SQL command
        PreparedStatement preparedStatement = connection.prepareStatement(addEntrySQL);
        
        // Set the values of the parameters to the corresponding values of the contact
        preparedStatement.setString(1, contact.getUuid().toString());
        preparedStatement.setString(2, contact.getName());
        preparedStatement.setString(3, contact.getPhoneNumber());
        preparedStatement.setString(4, contact.getEmail());
        
        // Execute the SQL command
        preparedStatement.execute();
    } catch (SQLException e) {
        // Print the stack trace if an SQLException occurs
        e.printStackTrace();
    }
}

    public String getPhoneNumber(String name) {
        try {
            String getPhoneNumberSQL = "SELECT phoneNumber FROM phonebook WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(getPhoneNumberSQL);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("phoneNumber");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getName(String phoneNumber) {
        try {
            String getNameSQL = "SELECT name FROM phonebook WHERE phoneNumber = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(getNameSQL);
            preparedStatement.setString(1, phoneNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

public ArrayList<Contact> searchContacts(String query) {
    try {
        String searchSQL = "SELECT * FROM phonebook WHERE name LIKE ? OR phoneNumber LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(searchSQL);
        preparedStatement.setString(1, "%" + query + "%");
        preparedStatement.setString(2, "%" + query + "%");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            String phoneNumber = resultSet.getString("phoneNumber");
            String email = resultSet.getString("email");
            contacts.add(new Contact(id, name, phoneNumber, email));
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


/**
 * Deletes an entry from the phonebook database.
 *
 * @param contact the contact to be deleted
 */
public void deleteEntry(Contact contact) {
    try {
        // SQL command to delete a row from the phonebook table
        String deleteEntrySQL = "DELETE FROM phonebook WHERE uuid = ?";
        
        // Create a PreparedStatement to execute the SQL command
        PreparedStatement preparedStatement = connection.prepareStatement(deleteEntrySQL);
        
        // Set the value of the first parameter to the uuid of the contact
        preparedStatement.setString(1, contact.getUuid().toString());
        
        // Execute the SQL command
        preparedStatement.execute();
    } catch (SQLException e) {
        // Print the stack trace if an SQLException occurs
        e.printStackTrace();
    }
}

    public void updateEntry(Contact contact, String newName, String newPhoneNumber, String newEmail) {
        try {
            String updateEntrySQL = "UPDATE phonebook SET name = ?, phoneNumber = ?, email = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateEntrySQL);
            preparedStatement.setString(1, newName == null ? contact.getName() : newName);
            preparedStatement.setString(2, newPhoneNumber == null ? contact.getPhoneNumber() : newPhoneNumber);
            preparedStatement.setString(3, newEmail == null ? contact.getEmail() : newEmail);
            preparedStatement.setString(4, contact.getUuid().toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Contact> displayAllContacts() {
        try {
            String selectAllSQL = "SELECT * FROM phonebook";
            PreparedStatement preparedStatement = connection.prepareStatement(selectAllSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Contact> results = new ArrayList<>();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phoneNumber");
                String email = resultSet.getString("email");
                results.add(new Contact(id, name, phoneNumber, email));
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
            // Create a BufferedReader to read from the specified file
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
    
            // Read each line of the file
            while ((line = reader.readLine()) != null) {
                // Split the line into values using comma as the delimiter
                String[] values = line.split(",");
                // Check the number of values in the line
                if (values.length == 4) {
                    // If there are 4 values, parse the id and call updateContact
                    String id = values[0];
                    String name = values[1];
                    String phoneNumber = values[2];
                    String email = values[3];
                    updateEntry(new Contact(id, name, phoneNumber, email), name, phoneNumber, email);
                } else if (values.length == 3) {
                    // If there are 3 values, create a new Contact and call addEntry
                    String name = values[0];
                    String phoneNumber = values[1];
                    String email = values[2];
                    Contact contact = new Contact(name, phoneNumber, email);
                    addEntry(contact);
                }
            }
    
            // Close the reader
            reader.close();
        } catch (IOException e) {
            // Print any IOExceptions that occur
            e.printStackTrace();
        }
    }
    

}
