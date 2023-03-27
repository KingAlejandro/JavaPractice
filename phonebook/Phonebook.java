import java.sql.*;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class Phonebook {
    private Connection connection;

/**
 * Constructor for the Phonebook class.
 * Connects to the phonebook database and creates the phonebook table if it doesn't exist.
 */
public Phonebook() {
    try {
        // Load the SQLite JDBC driver
        Class.forName("org.sqlite.JDBC");
        
        // URL of the phonebook database
        String dbURL = "jdbc:sqlite:phonebook.sqlite";
        
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

            // SQL command to create the phonebook table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS phonebook (id TEXT PRIMARY KEY, name TEXT, phoneNumber TEXT, email TEXT)";
            
            // Create a PreparedStatement to execute the SQL command
            PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL);
            
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

/**
 * Searches for contacts in the phonebook database.
 *
 * @param query the search query
 * @return an ArrayList of Contact objects that match the search query
 */
public ArrayList<Contact> searchContacts(String query) {
    try {
        // SQL command to search for contacts in the phonebook table
        String searchSQL = "SELECT * FROM phonebook WHERE name LIKE ? OR phoneNumber LIKE ?";
        
        // Create a PreparedStatement to execute the SQL command
        PreparedStatement preparedStatement = connection.prepareStatement(searchSQL);
        
        // Set the values of the parameters to the search query
        preparedStatement.setString(1, "%" + query + "%");
        preparedStatement.setString(2, "%" + query + "%");
        
        // Execute the SQL command and get the results
        ResultSet resultSet = preparedStatement.executeQuery();
        
        // Create an ArrayList to store the contacts
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        
        // Loop through the results and create Contact objects for each row
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            String phoneNumber = resultSet.getString("phoneNumber");
            String email = resultSet.getString("email");
            contacts.add(new Contact(id, name, phoneNumber, email));
        }
        
        // Return the contacts or null if no contacts were found
        if (contacts.size() == 0) {
            return null;
        } else {
            return contacts;
        }
    } catch (SQLException e) {
        // Print the stack trace if an SQLException occurs
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

/**
 * Updates an entry in the phonebook database.
 *
 * @param contact the contact to be updated
 * @param newName the new name for the contact
 * @param newPhoneNumber the new phone number for the contact
 * @param newEmail the new email for the contact
 */
public void updateEntry(Contact contact, String newName, String newPhoneNumber, String newEmail) {
    try {
        // SQL command to update a row in the phonebook table
        String updateEntrySQL = "UPDATE phonebook SET name = ?, phoneNumber = ?, email = ? WHERE id = ?";
        
        // Create a PreparedStatement to execute the SQL command
        PreparedStatement preparedStatement = connection.prepareStatement(updateEntrySQL);
        
        // Set the values of the parameters to the new values or the current values if no new value is provided
        preparedStatement.setString(1, newName == null ? contact.getName() : newName);
        preparedStatement.setString(2, newPhoneNumber == null ? contact.getPhoneNumber() : newPhoneNumber);
        preparedStatement.setString(3, newEmail == null ? contact.getEmail() : newEmail);
        preparedStatement.setString(4, contact.getUuid().toString());
        
        // Execute the SQL command
        preparedStatement.execute();
    } catch (SQLException e) {
        // Print the stack trace if an SQLException occurs
        e.printStackTrace();
    }
}

/**
 * Displays all contacts in the phonebook database.
 *
 * @return an ArrayList of Contact objects representing all contacts in the phonebook
 */
public ArrayList<Contact> displayAllContacts() {
    try {
        // SQL command to select all rows from the phonebook table
        String selectAllSQL = "SELECT * FROM phonebook";
        
        // Create a PreparedStatement to execute the SQL command
        PreparedStatement preparedStatement = connection.prepareStatement(selectAllSQL);
        
        // Execute the SQL command and get the results
        ResultSet resultSet = preparedStatement.executeQuery();
        
        // Create an ArrayList to store the contacts
        ArrayList<Contact> results = new ArrayList<>();
        
        // Loop through the results and create Contact objects for each row
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            String phoneNumber = resultSet.getString("phoneNumber");
            String email = resultSet.getString("email");
            results.add(new Contact(id, name, phoneNumber, email));
        }
        
        // Return the contacts or null if no contacts were found
        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    } catch (SQLException e) {
        // Print the stack trace if an SQLException occurs
        e.printStackTrace();
        return null;
    }
}


/**
 * Exports the phonebook database to a CSV file.
 *
 * @param fileName the name of the CSV file
 */
public void exportToCsv(String fileName) {
    try {
        // Create a FileWriter to write to the CSV file
        FileWriter writer = new FileWriter(fileName);
        
        // SQL command to select all rows from the phonebook table
        String selectAllSQL = "SELECT * FROM phonebook";
        
        // Create a Statement to execute the SQL command
        Statement statement = connection.createStatement();
        
        // Execute the SQL command and get the results
        ResultSet resultSet = statement.executeQuery(selectAllSQL);
        
        // Get metadata about the results
        ResultSetMetaData metaData = resultSet.getMetaData();
        
        // Get the number of columns in the results
        int columnCount = metaData.getColumnCount();
        
        // Loop through the results and write each row to the CSV file
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                if (i > 1) {
                    // Add a comma separator between values
                    writer.append(",");
                }
                // Get the value of the current column and write it to the CSV file
                String columnValue = resultSet.getString(i);
                writer.append(columnValue);
            }
            // Add a newline character after each row
            writer.append("\n");
        }
        
        // Flush and close the FileWriter
        writer.flush();
        writer.close();
    } catch (IOException | SQLException e) {
        // Print the stack trace if an IOException or SQLException occurs
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
