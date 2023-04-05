package org.JavaPractice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private User activeUser = null;
    public GUI() {

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
