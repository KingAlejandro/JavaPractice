package org.JavaPractice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.text.DecimalFormat;

/**
 * GUI class that extends JFrame and contains the main menu for the supermarket application.
 */
public class GUI extends JFrame {
    private final Users users;
    private final Products products;
    private final Sales sales;
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
        productButton.setVisible(false);

        /*
          ActionListener for the product button. When the button is clicked, displays information about all products
          in the database and allows the user to buy a product, edit a product (if the user is a manager or admin),
          or add a new product (if the user is a manager or admin).
          @param e the ActionEvent that triggered this method
         */
        productButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Get all products from the database
                ArrayList<Product> productList = null;
                try {
                    productList = products.getAllProducts();
                } catch (SQLException ex) {
                    showErrorDialog("Couldn't load products");
                }

                JFrame productFrame = new JFrame("Product List");
                productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                DefaultListModel<Product> listModel = createListModel(productList);

                final JList<Product> list = new JList<>(listModel);
                list.setCellRenderer(new ProductListRenderer());
                JScrollPane scrollPane = new JScrollPane(list);
                scrollPane.setPreferredSize(new Dimension(400, 300));

                // Create Buy and Edit buttons
                JButton buyButton = new JButton("Buy");
                JButton editButton = new JButton("Edit");
                JButton addProductButton = new JButton("Add Product");



                // Add action listeners to Buy and Edit buttons
                buyButton.addActionListener(new ActionListener() {
                    /**
                     * ActionListener for the Buy button. When the button is clicked, allows the user to select a
                     * product to buy and enter a quantity to purchase. If the user confirms the purchase, updates the
                     * selected product's quantity and adds a new sale to the Sales database.
                     * @param e the ActionEvent that triggered this method
                     */
                    public void actionPerformed(ActionEvent e) {
// Get the selected product from the list
                        Product selectedProduct = list.getSelectedValue();

                        if (selectedProduct != null) {
                            // Create a JPanel to hold the form fields
                            JPanel formPanel = new JPanel(new GridLayout(0, 2));

                            // Create the form fields and add them to the form panel
                            JLabel quantityLabel = new JLabel("Quantity:");
                            JTextField quantityField = new JTextField();
                            formPanel.add(quantityLabel);
                            formPanel.add(quantityField);

                            // Show the form panel in a dialog box
                            int result = JOptionPane.showConfirmDialog(null, formPanel, "Buy Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                            // If the user clicked OK, update the product and add the sale to the database
                            if (result == JOptionPane.OK_OPTION) {
                                // Get the values from the form fields
                                int quantity = Integer.parseInt(quantityField.getText());

                                // Check that the quantity is valid
                                if (quantity <= 0) {
                                    showErrorDialog("Invalid quantity");
                                    return;
                                } else if (quantity > selectedProduct.getQuantity()) {
                                    showErrorDialog("Not enough stock available");
                                    return;
                                } else if (activeUser.getBalance() < (selectedProduct.getQuantity()*selectedProduct.getPrice())) {
                                    showErrorDialog("You don't have enough balance for this purchase");
                                    return;
                                }

                                try {
                                    // Update the selected product in the database
                                    selectedProduct.setQuantity(selectedProduct.getQuantity() - quantity);
                                    // Get the current date and time
                                    LocalDateTime now = LocalDateTime.now();
                                    // Add the sale to the database
                                    sales.addSale(activeUser.getUuid(),selectedProduct.getProductID(), quantity, selectedProduct.getPrice(), selectedProduct.getCost(), now);                                    products.updateProduct(selectedProduct);
                                    list.repaint();
                                } catch (SQLException ex) {
                                    showErrorDialog("Couldn't update product or add sale");
                                }
                            }
                        }
                    }
                });

                editButton.addActionListener(new ActionListener() {
                    /**
                     * ActionListener for the Edit button. When the button is clicked, allows the user to select a
                     * product to edit and modify its name, quantity, and price. If the user confirms the changes, updates
                     * the product in the database.
                     * @param e the ActionEvent that triggered this method
                     */
                    public void actionPerformed(ActionEvent e) {
                        // Get the selected product from the list
                        Product selectedProduct = list.getSelectedValue();

                        if (selectedProduct != null) {
                            // Show the product dialog and update the product in the database if the user clicked OK
                            Product updatedProduct = showProductDialog("Edit Product", selectedProduct);
                            if (updatedProduct != null) {
                                try {
                                    products.updateProduct(updatedProduct);
                                    list.repaint();
                                } catch (SQLException ex) {
                                    showErrorDialog("Couldn't update product");
                                }
                            }
                        }
                    }

                });

                addProductButton.addActionListener(new ActionListener() {
                    /**
                     * ActionListener for the Add Product button. When the button is clicked, allows the user to enter
                     * information about a new product and add it to the database.
                     * @param e the ActionEvent that triggered this method
                     */
                    public void actionPerformed(ActionEvent e) {
                        // Create a new product with default values
                        Product product = new Product("", 0.0, 0.0, 0, 0);

                        // Show the product dialog and update the product in the database if the user clicked OK
                        Product updatedProduct = showProductDialog("Add Product", product);
                        if (updatedProduct != null) {
                            try {
                                products.addProduct(updatedProduct);
                                listModel.addElement(updatedProduct);
                                // Repaint the list
                                list.repaint();
                            } catch (SQLException ex) {
                                showErrorDialog("Couldn't add product");
                            }
                        }
                    }
                });

                // Create a JPanel to hold the JList and buttons
                // Add the buttons to a panel and create a content pane with the list and the
                // button panel


                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                System.out.println("User type: " + activeUser.getUserType());


                if (activeUser.getUserType().equals("manager") || activeUser.getUserType().equals("admin")){
                    buttonPanel.add(editButton, BorderLayout.NORTH);
                    buttonPanel.add(addProductButton, BorderLayout.SOUTH);
                }
                buttonPanel.add(buyButton, BorderLayout.SOUTH);
                JPanel contentPane = new JPanel(new BorderLayout());
                contentPane.add(scrollPane, BorderLayout.CENTER);
                contentPane.add(buttonPanel, BorderLayout.SOUTH);

                JOptionPane.showMessageDialog(null, contentPane, "All Products", JOptionPane.PLAIN_MESSAGE);
            }

            private Product showProductDialog(String title, Product product) {
                // Create a JPanel to hold the form fields
                JPanel formPanel = new JPanel(new GridLayout(0, 2));

                // Create the form fields and add them to the form panel
                JLabel nameLabel = new JLabel("Product Name:");
                JTextField nameField = new JTextField(product.getName());
                JLabel priceLabel = new JLabel("Price:");
                JTextField priceField = new JTextField(Double.toString(product.getPrice()));
                JLabel costLabel = new JLabel("Cost:");
                JTextField costField = new JTextField(Double.toString(product.getCost()));
                JLabel quantityLabel = new JLabel("Quantity:");
                JTextField quantityField = new JTextField(Integer.toString(product.getQuantity()));
                JLabel weightLabel = new JLabel("Weight:");
                JTextField weightField = new JTextField(Double.toString(product.getWeight()));
                formPanel.add(nameLabel);
                formPanel.add(nameField);
                formPanel.add(priceLabel);
                formPanel.add(priceField);
                formPanel.add(costLabel);
                formPanel.add(costField);
                formPanel.add(quantityLabel);
                formPanel.add(quantityField);
                formPanel.add(weightLabel);
                formPanel.add(weightField);

                // Show the form panel in a dialog box
                int result = JOptionPane.showConfirmDialog(null, formPanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // If the user clicked OK, update the product in the database and return it
                if (result == JOptionPane.OK_OPTION) {
                    // Get the values from the form fields
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    double cost = Double.parseDouble(costField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());
                    double weight = Double.parseDouble(weightField.getText());

                    // Update the product and return it
                    product.setName(name);
                    product.setPrice(price);
                    product.setCost(cost);
                    product.setQuantity(quantity);
                    product.setWeight(weight);
                    return product;
                }

                // If the user clicked Cancel, return null
                return null;
            }

        });

// Add the Product button to the JFrame menu
        panel.add(productButton);

        // Create a button to show the sales panel
        gbc.gridy++;
        JButton salesButton = new JButton("Sales");
        salesButton.setVisible(false);

// Add an action listener to the sales button
        salesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a panel to hold the buttons
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton viewAllButton = new JButton("View all sales");
                JButton viewPeriodButton = new JButton("View sales within a period");
                buttonPanel.add(viewAllButton);
                buttonPanel.add(viewPeriodButton);

                // Add action listeners to the buttons
                viewAllButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Get all sales from the database
                        JOptionPane.getRootFrame().dispose();
                        ArrayList<Sale> saleList;
                        try {
                            saleList = sales.getAllSales();
                        } catch (SQLException ex) {
                            showErrorDialog("Couldn't load sales");
                            return;
                        }

                        // Create a frame to hold the sale list
                        JFrame saleFrame = new JFrame("Sale List");
                        saleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        // Create and populate a list model to hold the sale data
                        DefaultListModel<Sale> listModel = createListModel(saleList);

                        // Create a list to hold the sale data
                        final JList<Sale> list = new JList<>(listModel);
                        list.setCellRenderer(new SaleListRenderer());

                        // Create a scroll pane to hold the list
                        JScrollPane scrollPane = new JScrollPane(list);
                        scrollPane.setPreferredSize(new Dimension(400, 300));

                        // Create a panel to hold the buttons
                        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                        JButton infoButton = new JButton("Sale Info");
                        JButton reportButton = new JButton("Report");
                        buttonPanel2.add(infoButton);
                        buttonPanel2.add(reportButton);

                        // Add an action listener to the info button
                        infoButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                Sale selectedSale = list.getSelectedValue();
                                if (selectedSale != null) {
                                    showSaleInfo(selectedSale);
                                }
                            }
                        });

                        final ArrayList<Sale> finalSaleList = saleList;
                        reportButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (finalSaleList.isEmpty()) {
                                    showErrorDialog("No sales to display");
                                    return;
                                }
                                generateReport(finalSaleList);
                            }
                        });

                        // Add the components to the sale frame
                        saleFrame.add(scrollPane, BorderLayout.CENTER);
                        saleFrame.add(buttonPanel2, BorderLayout.SOUTH);
                        saleFrame.pack();
                        saleFrame.setLocationRelativeTo(null);
                        saleFrame.setVisible(true);
                    }
                });

                viewPeriodButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.getRootFrame().dispose();
                        // Create a panel to hold the form fields
                        JPanel formPanel = new JPanel(new GridLayout(0, 2));

                        // Create the form fields and add them to the panel
                        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
                        JTextField startDateField = new JTextField();
                        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
                        JTextField endDateField = new JTextField();
                        formPanel.add(startDateLabel);
                        formPanel.add(startDateField);
                        formPanel.add(endDateLabel);
                        formPanel.add(endDateField);

                        // Show the form panel in a dialog box
                        int periodResult = JOptionPane.showConfirmDialog(null, formPanel, "Sales within a specific period", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                        // If the user clicked OK, show the list of sales within the specified period
                        if (periodResult == JOptionPane.OK_OPTION) {
                            // Get the start and end dates from the form fields
                            LocalDateTime startDate = LocalDateTime.parse(startDateField.getText() + "T00:00:00");
                            LocalDateTime endDate = LocalDateTime.parse(endDateField.getText() + "T23:59:59");

                            // Get the sales within the specified period from the database
                            ArrayList<Sale> saleList;
                            try {
                                saleList = sales.getSalesWithinPeriod(startDate, endDate);
                            } catch (SQLException ex) {
                                showErrorDialog("Couldn't load sales within period");
                                return;
                            }

                            // Create a frame to hold the sale list
                            JFrame saleFrame = new JFrame("Sale List");
                            saleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                            // Create and populate list model to hold the sale data
                            DefaultListModel<Sale> listModel = createListModel(saleList);

                            // Create a list to hold the sale data
                            final JList<Sale> list = new JList<>(listModel);
                            list.setCellRenderer(new SaleListRenderer());

                            // Create a scroll pane to hold the list
                            JScrollPane scrollPane = new JScrollPane(list);
                            scrollPane.setPreferredSize(new Dimension(400, 300));

                            // Create a panel to hold the buttons
                            JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                            JButton infoButton = new JButton("Info");
                            JButton reportButton = new JButton("Report");
                            buttonPanel2.add(infoButton);
                            buttonPanel2.add(reportButton);

                            // Add an action listener to the info button
                            infoButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    Sale selectedSale = list.getSelectedValue();
                                    if (selectedSale != null) {
                                        showSaleInfo(selectedSale);
                                    }
                                }
                            });

                            final ArrayList<Sale> finalSaleList = saleList;
                            reportButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (finalSaleList.isEmpty()) {
                                        showErrorDialog("No sales to display");
                                        return;
                                    }
                                    generateReport(finalSaleList);
                                }
                            });

                            // Add the components to the sale frame
                            saleFrame.add(scrollPane, BorderLayout.CENTER);
                            saleFrame.add(buttonPanel2, BorderLayout.SOUTH);
                            saleFrame.pack();
                            saleFrame.setLocationRelativeTo(null);
                            saleFrame.setVisible(true);
                        }
                    }
                });

                // Show the button panel in a dialog box
                JOptionPane.showConfirmDialog(null, buttonPanel, "Sales", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            }
            private void showSaleInfo(Sale selectedSale) {
                // Get the user who made the sale
                User user = null;
                try {
                    user = users.getUserById(selectedSale.getUserID());
                } catch (SQLException ex) {
                    showErrorDialog("Error getting user data: " + ex.getMessage());
                }

                // Get the product that was sold
                Product product = null;
                try {
                    product = products.getProductById(selectedSale.getProductID());
                } catch (SQLException ex) {
                    showErrorDialog("Error getting product data: " + ex.getMessage());
                }

                // Calculate cost and margin
                double cost = selectedSale.getCostAtSale() * selectedSale.getQuantitySold();
                double price = selectedSale.getPriceAtSale() * selectedSale.getQuantitySold();
                double margin = (price - cost) / cost * 100;

                // Calculate profit
                double profit = price - cost;

                // Create a panel to hold the sale information
                JPanel salePanel = new JPanel(new GridLayout(0, 2));

                // Add the sale information to the panel
                salePanel.add(new JLabel("Product:"));
                if (product != null) {
                    salePanel.add(new JLabel(product.getName()));
                } else {
                    salePanel.add(new JLabel("N/A"));
                }
                salePanel.add(new JLabel("Quantity:"));
                salePanel.add(new JLabel(Integer.toString(selectedSale.getQuantitySold())));
                salePanel.add(new JLabel("Price:"));
                salePanel.add(new JLabel(Double.toString(selectedSale.getPriceAtSale())));
                salePanel.add(new JLabel("Cost at Sale:"));
                salePanel.add(new JLabel(Double.toString(selectedSale.getCostAtSale())));
                salePanel.add(new JLabel("Margin:"));
                salePanel.add(new JLabel(String.format("%.2f%%", margin)));
                salePanel.add(new JLabel("Profit:"));
                salePanel.add(new JLabel(Double.toString(profit)));
                salePanel.add(new JLabel("Time:"));
                salePanel.add(new JLabel(selectedSale.getSaleDate().toString()));
                salePanel.add(new JLabel("Buyer:"));
                if (user != null) {
                    salePanel.add(new JLabel(user.getName()));
                } else {
                    salePanel.add(new JLabel("N/A"));
                }
                salePanel.add(new JLabel("Email:"));
                if (user != null) {
                    salePanel.add(new JLabel(user.getEmail()));
                } else {
                    salePanel.add(new JLabel("N/A"));
                }

                // Show the sale panel in a dialog box
                JOptionPane.showMessageDialog(null, salePanel, "Sale Info", JOptionPane.PLAIN_MESSAGE);
            }

            private void generateReport(ArrayList<Sale> salesList) {
                double totalSalePrice = 0.0;
                double totalCostPrice = 0.0;
                for (Sale sale : salesList) {
                        totalSalePrice += sale.getPriceAtSale() * sale.getQuantitySold();
                        totalCostPrice += sale.getCostAtSale() * sale.getQuantitySold();
                }
                double profit = totalSalePrice - totalCostPrice;
                double profitMargin = 0.0;
                if (totalSalePrice != 0.0) {
                    profitMargin = profit / totalSalePrice * 100;
                }
                DecimalFormat df = new DecimalFormat("0.00");
                String reportString = "Total Sale Price: $" + df.format(totalSalePrice) + "\nTotal Cost Price: $" + df.format(totalCostPrice) + "\nProfit: $" + df.format(profit) + "\nProfit Margin: " + df.format(profitMargin) + "%";
                JOptionPane.showMessageDialog(null, reportString, "Sales Report", JOptionPane.PLAIN_MESSAGE);
            }

        });
        panel.add(salesButton, gbc);

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
                        JOptionPane.showMessageDialog(null, "You are now logged in as " + email + " type: " + activeUser.getUserType());
                        loginButton.setVisible(false);
                        registerButton.setVisible(false);
                        logoutButton.setVisible(true);
                        quitButton.setVisible(true);
                        updateBalanceButton.setVisible(true);

                        productButton.setVisible(true);
                        if (activeUser.getUserType().equals("admin")){
                            salesButton.setVisible(true);
                        }


                    } else {
                        // Login failed
                        showErrorDialog("Invalid email or password");
                    }

                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create text fields for name, email, and password
                JTextField nameField = new JTextField(10);
                JTextField emailField = new JTextField(10);
                JTextField passwordField = new JTextField(10);
                String[] options = {"buyer", "manager", "admin"};
                JComboBox<String> typeField = new JComboBox<>(options);

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
                myPanel.add(Box.createVerticalStrut(15)); // a spacer
                myPanel.add(new JLabel("Type:"));
                myPanel.add(typeField);

                // Show panel in a dialog box
                int result = JOptionPane.showOptionDialog(null, myPanel,
                        "Register", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, new String[]{"Register", "Cancel"}, null);
                if (result == JOptionPane.YES_OPTION) {
                    // Get name, email, and password values
                    String name = nameField.getText();
                    String email = emailField.getText();
                    String password = passwordField.getText();
                    String type = (String) typeField.getSelectedItem();

                    // Attempt to register user
                    boolean registerResult = users.registerUser(name, email, password, 0.0, type);

                    if (registerResult) {
                        JOptionPane.showMessageDialog(null, "User registered successfully");
                    } else {
                        // Registration failed
                        showErrorDialog("User with that email already exists");
                    }
                }
            }
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
                productButton.setVisible(false);
                salesButton.setVisible(false);
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
                        showErrorDialog("Invalid balance value");
                    }
                }
            }
        });



    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Creates a DefaultListModel containing the given list of products.
     * @param objects the list of objects to add to the list model
     * @return the DefaultListModel containing the products
     */
    private DefaultListModel createListModel(ArrayList<?> objects) {
        DefaultListModel model = new DefaultListModel();
        for (Object obj : objects) {
            model.addElement(obj);
        }
        return model;
    }

    private void showSales(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        ArrayList<Sale> salesList;
        if (startDate != null && endDate != null) {
            salesList = sales.getSalesWithinPeriod(startDate, endDate);
        } else {
            salesList = sales.getAllSales();
        }

        if (salesList.isEmpty()) {
            showErrorDialog("No sales to display");
            return;
        }

        JList<Sale> saleList = new JList<>(createListModel(salesList));
        saleList.setCellRenderer(new SaleListRenderer());

        JScrollPane scrollPane = new JScrollPane(saleList);

        JOptionPane.showMessageDialog(this, scrollPane, "Sales", JOptionPane.PLAIN_MESSAGE);
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

    public class SaleListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // Create a label with the sale's date and total volume
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Sale sale = (Sale) value;
            setText(sale.getSaleDate().toString() + " - Total Volume: $" + String.format("%.2f", sale.getPriceAtSale() * sale.getQuantitySold()));
            return label;
        }
    }

}