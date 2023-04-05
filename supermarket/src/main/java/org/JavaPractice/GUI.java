package org.JavaPractice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private Users users;
    private User activeUser = null;
    public GUI(Users users) {
        this.users = users;

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

        if (activeUser == null) {
            JButton registerButton = new JButton("Register");
            panel.add(registerButton, gbc);
        }

        if (activeUser == null) {
            gbc.gridy++;
            JButton loginButton = new JButton("Log In");
            panel.add(loginButton, gbc);

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

                        JOptionPane.showMessageDialog(null, "You are now logged in as " + email);

                    }
                };
            });
        }

        if (activeUser != null) {
            gbc.gridy++;
            JButton logoutButton = new JButton("Log Out");
            panel.add(logoutButton, gbc);
        }

        gbc.gridy++;
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Exit the application
                System.exit(0);
            }
        });
        panel.add(quitButton, gbc);

        add(panel);
    }

}
