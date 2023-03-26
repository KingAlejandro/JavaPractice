import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PhonebookGUI extends JFrame {
    private Phonebook phonebook;

    public PhonebookGUI(Phonebook phonebook) {
        setTitle("Phonebook");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 10, 5, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        c.gridy = 1;
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField nameField = new JTextField(10);
                JTextField phoneField = new JTextField(10);
        
                JPanel myPanel = new JPanel();
                myPanel.add(new JLabel("Name:"));
                myPanel.add(nameField);
                myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                myPanel.add(new JLabel("Phone Number:"));
                myPanel.add(phoneField);
        
                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "Please Enter Name and Phone Number", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String name = nameField.getText();
                    String phoneNumber = phoneField.getText();
                    phonebook.addEntry(name, phoneNumber);
                    System.out.println(name + " has been added to the phonebook.");
                    JOptionPane.showMessageDialog(null, name + " has been added to the phonebook.");

                }
            }
        });
        
        panel.add(addButton, c);

        c.gridy = 2;
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Enter a name to remove from the phonebook:");

                if (name != null && !name.isEmpty()) {
                    phonebook.deleteEntry(name);

                    JOptionPane.showMessageDialog(null, name + " has been removed from the phonebook.");
                    System.out.println(name + " has been removed from the phonebook.");

                }
            }
        });
        panel.add(removeButton, c);

        c.gridy = 3;
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField nameField = new JTextField(10);
                JTextField phoneNumberField = new JTextField(10);

                JPanel myPanel = new JPanel();
                myPanel.add(new JLabel("Name:"));
                myPanel.add(nameField);
                myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                myPanel.add(new JLabel("Phone Number:"));
                myPanel.add(phoneNumberField);

                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "Please Enter Name and Phone Number", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String name = nameField.getText();
                    String phoneNumber = phoneNumberField.getText();
                    phonebook.updateEntry(name, phoneNumber);
                    JOptionPane.showMessageDialog(null, name + "'s phone number has been updated in the phonebook.");
                }
            }
        });
        panel.add(updateButton, c);

        c.gridy = 4;
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // do something when the search button is clicked
            }
        });
        panel.add(searchButton, c);

        c.gridy = 5;
        JButton displayAllButton = new JButton("Display All");
        displayAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> contacts = phonebook.displayAllContacts();
                if (contacts != null) {
                    JTextArea textArea = new JTextArea();
                    for (String contact : contacts) {
                        textArea.append(contact + "\n");
                    }
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(400, 300));
                    JOptionPane.showMessageDialog(null, scrollPane, "All Contacts", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No contacts found", "All Contacts", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        panel.add(displayAllButton, c);

        c.gridy = 6;
        JButton importExportButton = new JButton("Import/Export");
        importExportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // do something when the import/export button is clicked
            }
        });
        panel.add(importExportButton, c);

        c.gridy = 7;
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        c.gridy = 8;
        panel.add(quitButton, c);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Phonebook phonebook = new Phonebook();
                new PhonebookGUI(phonebook).setVisible(true);
            }
        });
    }
}

