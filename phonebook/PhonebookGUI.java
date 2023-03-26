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
                    Contact contact = new Contact(name, phoneNumber);
                    phonebook.addEntry(contact);
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
                        JOptionPane.showMessageDialog(null, scrollPane, "Search Results", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No contacts found", "Search Results",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        });

        panel.add(searchButton, c);

        c.gridy = 5;
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
