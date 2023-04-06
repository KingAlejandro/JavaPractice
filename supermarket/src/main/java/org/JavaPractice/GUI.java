package org.JavaPractice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import java.lang.reflect.Array;
import java.util.List;

public class GUI extends JFrame {
    private Users users;
    private Products products;
    private Sales sales;
    private User activeUser = null;

    public GUI(Users users, Products products, Sales sales) {
        this.users = users;
        this.products = products;
        this.sales = sales;

        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridy++;
        JButton registerButton = new JButton("Register");
        panel.add(registerButton, gbc);
        System.out.println("ActiveUser is null");
        registerButton.setVisible(true);
        panel.add(registerButton, gbc);

        gbc.gridy++;
        JButton loginButton = new JButton("Log In");
        loginButton.setVisible(true);
        panel.add(loginButton, gbc);

        gbc.gridy++;
        JButton updateBalanceButton = new JButton("Update Balance");
        updateBalanceButton.setVisible(false);
        panel.add(updateBalanceButton, gbc);

        gbc.gridy++;
        JButton productButton = new JButton("Product");
        productButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Get all products from the database
                ArrayList<Product> productList = null;
                    try {
                        productList = products.getAllProducts();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Couldn't load products", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                if (productList != null) {
                    JFrame productFrame = new JFrame("Product List");
                    productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    final DefaultListModel<Product> listModel = new DefaultListModel<>();
                    for (Product product : productList) {
                        listModel.addElement(product);
                    }

                    final JList<Product> list = new JList<>(listModel);
                    list.setCellRenderer(new ProductListRenderer());
                    JScrollPane scrollPane = new JScrollPane(list);
                    scrollPane.setPreferredSize(new Dimension(400, 300));

                    // Create Buy and Edit buttons
                    JButton buyButton = new JButton("Buy");
                    JButton editButton = new JButton("Edit");



                // Add action listeners to Buy and Edit buttons
                buyButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Handle Buy button click
                        // ...
                    }
                });
                editButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Handle Edit button click
                        // ...
                    }
                });

                    JButton addProductButton = new JButton("Add Product");
                    addProductButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // Create a JPanel to hold the form fields
                            JPanel formPanel = new JPanel(new GridLayout(0, 2));

                            // Create the form fields and add them to the form panel
                            JLabel nameLabel = new JLabel("Product Name:");
                            JTextField nameField = new JTextField();
                            JLabel priceLabel = new JLabel("Price:");
                            JTextField priceField = new JTextField();
                            JLabel quantityLabel = new JLabel("Quantity:");
                            JTextField quantityField = new JTextField();
                            JLabel weightLabel = new JLabel("Weight:");
                            JTextField weightField = new JTextField();
                            formPanel.add(nameLabel);
                            formPanel.add(nameField);
                            formPanel.add(priceLabel);
                            formPanel.add(priceField);
                            formPanel.add(quantityLabel);
                            formPanel.add(quantityField);
                            formPanel.add(weightLabel);
                            formPanel.add(weightField);

                            // Show the form panel in a dialog box
                            int result = JOptionPane.showConfirmDialog(null, formPanel, "Add Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                            // If the user clicked OK, create a new Product object and add it to the database
                            if (result == JOptionPane.OK_OPTION) {
                                // Get the values from the form fields
                                String name = nameField.getText();
                                double price = Double.parseDouble(priceField.getText());
                                int quantity = Integer.parseInt(quantityField.getText());
                                double weight = Double.parseDouble(weightField.getText());

                                try {
                                    products.addProduct(name, price, weight, quantity);
                                } catch (SQLException ex) {
                                    JOptionPane.showMessageDialog(null, "Couldn't add product", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    });

                // Create a JPanel to hold the JList and buttons
                    // Add the buttons to a panel and create a content pane with the list and the
                    // button panel
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    buttonPanel.add(editButton, BorderLayout.NORTH);
                    buttonPanel.add(buyButton, BorderLayout.SOUTH);
                    buttonPanel.add(addProductButton, BorderLayout.SOUTH);
                    JPanel contentPane = new JPanel(new BorderLayout());
                    contentPane.add(scrollPane, BorderLayout.CENTER);
                    contentPane.add(buttonPanel, BorderLayout.SOUTH);

                    JOptionPane.showMessageDialog(null, contentPane, "All Contacts", JOptionPane.PLAIN_MESSAGE);
                } else {
                    // If no results are found, show a message
                    JOptionPane.showMessageDialog(null, "No contacts found", "All Contacts", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

// Add the Product button to the JFrame menu
        panel.add(productButton);

        gbc.gridy++;
        JButton logoutButton = new JButton("Log Out");
        logoutButton.setVisible(false);
        panel.add(logoutButton, gbc);

        gbc.gridy++;
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Exit the application
                System.exit(0);
            }
        });
        quitButton.setVisible(true);
        panel.add(quitButton, gbc);


        add(panel);
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JTextField emailField = new JTextField(10);
                    JTextField passwordField = new JTextField(10);

                    JPanel myPanel = new JPanel();
                    myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
                    myPanel.add(new JLabel("Email:"));
                    myPanel.add(emailField);
                    myPanel.add(Box.createVerticalStrut(15)); // a spacer
                    myPanel.add(new JLabel("Password:"));
                    myPanel.add(passwordField);

                    int result = JOptionPane.showOptionDialog(null, myPanel,
                            "Log In", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, new String[]{"Log In", "Cancel"}, null);
                    if (result == JOptionPane.YES_OPTION) {
                        String email = emailField.getText();
                        String password = passwordField.getText();

                        User loginResult = users.login(email,password);

                        if (loginResult != null) {
                            activeUser = loginResult;
                            JOptionPane.showMessageDialog(null, "You are now logged in as " + email);
                            loginButton.setVisible(false);
                            registerButton.setVisible(false);
                            logoutButton.setVisible(true);
                            quitButton.setVisible(true);
                            updateBalanceButton.setVisible(true);

                        } else {
                            // Login failed
                            JOptionPane.showMessageDialog(null, "Invalid email or password", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    }
                };
            });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create text fields for name, email, and password
                JTextField nameField = new JTextField(10);
                JTextField emailField = new JTextField(10);
                JTextField passwordField = new JTextField(10);

                // Create panel with labeled text fields
                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
                myPanel.add(new JLabel("Name:"));
                myPanel.add(nameField);
                myPanel.add(Box.createVerticalStrut(15)); // a spacer
                myPanel.add(new JLabel("Email:"));
                myPanel.add(emailField);
                myPanel.add(Box.createVerticalStrut(15)); // a spacer
                myPanel.add(new JLabel("Password:"));
                myPanel.add(passwordField);

                // Show panel in a dialog box
                int result = JOptionPane.showOptionDialog(null, myPanel,
                        "Register", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, new String[]{"Register", "Cancel"}, null);
                if (result == JOptionPane.YES_OPTION) {
                    // Get name, email, and password values
                    String name = nameField.getText();
                    String email = emailField.getText();
                    String password = passwordField.getText();

                    // Attempt to register user
                    boolean registerResult = users.registerUser(name, email, password, 0.0);

                    if (registerResult) {
                        JOptionPane.showMessageDialog(null, "User registered successfully");
                    } else {
                        // Registration failed
                        JOptionPane.showMessageDialog(null, "User with that email already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activeUser = null;
                JOptionPane.showMessageDialog(null, "You have logged out");
                loginButton.setVisible(true);
                registerButton.setVisible(true);
                logoutButton.setVisible(false);
                quitButton.setVisible(true);
                updateBalanceButton.setVisible(false);
            }
        });

        updateBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField balanceField = new JTextField(10);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
                myPanel.add(new JLabel("Balance:"));
                myPanel.add(balanceField);

                int result = JOptionPane.showOptionDialog(null, myPanel,
                        "Update Balance", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, new String[]{"Update", "Cancel"}, null);
                if (result == JOptionPane.YES_OPTION) {
                    String balanceString = balanceField.getText();

                    try {
                        double balance = Double.parseDouble(balanceString);
                        activeUser.setBalance(balance);
                        users.updateUser(activeUser);
                        JOptionPane.showMessageDialog(null, "Balance updated successfully");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid balance value", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        }

    public class ProductListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            // create a label with the contact's name and phone number
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Product product = (Product) value;
            setText(product.getName() + " - $" + product.getPrice() + " - Quantity: " + product.getQuantity());
            return label;
        }
    }


}