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
                JTextField emailField = new JTextField(10);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
                myPanel.add(new JLabel("Name:"));
                myPanel.add(nameField);
                myPanel.add(Box.createVerticalStrut(15)); // a spacer
                myPanel.add(new JLabel("Phone Number:"));
                myPanel.add(phoneField);
                myPanel.add(Box.createVerticalStrut(15)); // a spacer
                myPanel.add(new JLabel("Email:"));
                myPanel.add(emailField);

                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "Please Enter Name and Phone Number", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String name = nameField.getText();
                    String phoneNumber = phoneField.getText();
                    String email = emailField.getText();
                    Contact contact = new Contact(name, phoneNumber, email);
                    phonebook.addEntry(contact);
                    JOptionPane.showMessageDialog(null, name + " has been added to the phonebook.");

                }
            }
        });

        panel.add(addButton, c);

        c.gridy = 2;
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel searchPanel = new JPanel();
                searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));

                JLabel searchLabel = new JLabel("Enter name to search:");
                searchPanel.add(searchLabel);

                JTextField searchField = new JTextField();
                searchPanel.add(searchField);

                int result = JOptionPane.showConfirmDialog(
                        null,
                        searchPanel,
                        "Search",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String query = searchField.getText();
                    ArrayList<Contact> contacts = phonebook.searchContacts(query);
                    if (contacts != null) {
                        DefaultListModel<Contact> listModel = new DefaultListModel<>();
                        for (Contact contact : contacts) {
                            listModel.addElement(contact);
                        }
                        JList<Contact> list = new JList<>(listModel);
                        list.setCellRenderer(new ContactListRenderer());
                        JScrollPane scrollPane = new JScrollPane(list);
                        scrollPane.setPreferredSize(new Dimension(400, 300));

                        JButton editButton = new JButton("Edit Contact");
                        editButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Contact selectedContact = list.getSelectedValue();

                                if (selectedContact == null) {
                                    JOptionPane.showMessageDialog(null, "Please select a contact to update",
                                            "Update Contact", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }

                                Contact updatedContact = editContact(phonebook, selectedContact);

                                if (updatedContact != null) {
                                    int index = list.getSelectedIndex();
                                    listModel.set(index, updatedContact);
                                    list.repaint();
                                    JOptionPane.showMessageDialog(null, "The contact has been updated");
                                }

                            }
                        });

                        JButton deleteButton = new JButton("Delete Contact");
                        deleteButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Contact selectedContact = list.getSelectedValue();

                                if (selectedContact == null) {
                                    JOptionPane.showMessageDialog(null, "Please select a contact to delete",
                                            "Delete Contact", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                phonebook.deleteEntry(selectedContact);
                                int index = list.getSelectedIndex();
                                listModel.remove(index);

                                list.repaint();
                                JOptionPane.showMessageDialog(null, "The contact has been deleted");
                            }
                        });

                        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                        buttonPanel.add(editButton, BorderLayout.NORTH);
                        buttonPanel.add(deleteButton, BorderLayout.SOUTH);
                        JPanel contentPane = new JPanel(new BorderLayout());
                        contentPane.add(scrollPane, BorderLayout.CENTER);
                        contentPane.add(buttonPanel, BorderLayout.SOUTH);

                        JOptionPane.showMessageDialog(null, contentPane, "Search Results", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No contacts found", "Search Results",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        });

        panel.add(searchButton, c);

        c.gridy = 3;
        JButton displayAllButton = new JButton("Display All");
        displayAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Contact> contacts = phonebook.displayAllContacts();
                if (contacts != null) {
                    DefaultListModel<Contact> listModel = new DefaultListModel<>();
                    for (Contact contact : contacts) {
                        listModel.addElement(contact);
                    }
                    JList<Contact> list = new JList<>(listModel);
                    list.setCellRenderer(new ContactListRenderer());
                    JScrollPane scrollPane = new JScrollPane(list);
                    scrollPane.setPreferredSize(new Dimension(400, 300));

                    JButton editButton = new JButton("Edit Contact");
                    editButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Contact selectedContact = list.getSelectedValue();

                            if (selectedContact == null) {
                                JOptionPane.showMessageDialog(null, "Please select a contact to update",
                                        "Update Contact", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            Contact updatedContact = editContact(phonebook, selectedContact);

                            if (updatedContact != null) {
                                int index = list.getSelectedIndex();
                                listModel.set(index, updatedContact);
                                list.repaint();
                                JOptionPane.showMessageDialog(null, "The contact has been updated");
                            }

                        }
                    });

                    JButton deleteButton = new JButton("Delete Contact");
                    deleteButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Contact selectedContact = list.getSelectedValue();

                            if (selectedContact == null) {
                                JOptionPane.showMessageDialog(null, "Please select a contact to delete",
                                        "Delete Contact", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            phonebook.deleteEntry(selectedContact);
                            int index = list.getSelectedIndex();
                            listModel.remove(index);

                            list.repaint();
                            JOptionPane.showMessageDialog(null, "The contact has been deleted");
                        }
                    });

                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    buttonPanel.add(editButton, BorderLayout.NORTH);
                    buttonPanel.add(deleteButton, BorderLayout.SOUTH);
                    JPanel contentPane = new JPanel(new BorderLayout());
                    contentPane.add(scrollPane, BorderLayout.CENTER);
                    contentPane.add(buttonPanel, BorderLayout.SOUTH);

                    JOptionPane.showMessageDialog(null, contentPane, "All Contacts", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No contacts found", "All Contacts", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        panel.add(displayAllButton, c);

        c.gridy = 4;
        JButton importExportButton = new JButton("Import/Export");
        importExportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = { "Import from CSV", "Export to CSV" };
                int choice = JOptionPane.showOptionDialog(null, "Choose an option", "Import/Export",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                switch (choice) {
                    case 0:
                        JFileChooser fileChooser = new JFileChooser();
                        int returnVal = fileChooser.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            String filepath = fileChooser.getSelectedFile().getAbsolutePath();
                            phonebook.importFromCsv(filepath);
                        }
                        break;
                    case 1:
                        JFileChooser fileSaveChooser = new JFileChooser();
                        int returnSaveVal = fileSaveChooser.showSaveDialog(null);
                        if (returnSaveVal == JFileChooser.APPROVE_OPTION) {
                            String filepath = fileSaveChooser.getSelectedFile().getAbsolutePath();
                            phonebook.exportToCsv(filepath);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        panel.add(importExportButton, c);

        c.gridy = 5;
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        c.gridy = 6;
        panel.add(quitButton, c);

        add(panel);
        setVisible(true);
    }

    public Contact editContact(Phonebook phonebook, Contact selectedContact) {
        JTextField nameField = new JTextField(selectedContact.getName());
        JTextField phoneNumberField = new JTextField(selectedContact.getPhoneNumber());
        JTextField emailField = new JTextField(selectedContact.getEmail());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneNumberField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Contact",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            phonebook.updateEntry(selectedContact, nameField.getText(), phoneNumberField.getText(),
                    emailField.getText());
            return new Contact(selectedContact.getUuid().toString(),
                    nameField.getText(), phoneNumberField.getText(), emailField.getText());
        } else {
            return null;
        }

    }

    public class ContactListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Contact contact = (Contact) value;
            label.setText(contact.getName() + ": " + contact.getPhoneNumber());
            return label;
        }
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
