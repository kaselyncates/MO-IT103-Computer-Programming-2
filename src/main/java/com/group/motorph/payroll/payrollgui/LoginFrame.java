/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author KC
 */


package com.group.motorph.payroll.payrollgui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import com.opencsv.*;
import java.nio.file.Paths;

/**
 * This class creates a login screen for the MotorPH Payroll System.
 * Users must input correct credentials (from users.csv) to access the system.
 */

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
    setTitle("MotorPH Payroll Login");
    setSize(350, 200);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout(10, 10));

    // Form panel for username/password
    JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20)); 
    
    // Username
    formPanel.add(new JLabel("Username:"));
    usernameField = new JTextField();
    formPanel.add(usernameField);

    // Password
    formPanel.add(new JLabel("Password:"));
    passwordField = new JPasswordField();
    formPanel.add(passwordField);

    add(formPanel, BorderLayout.CENTER);

    JButton loginButton = new JButton("Login");
    loginButton.addActionListener(e -> authenticateUser());

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(loginButton);

    add(buttonPanel, BorderLayout.SOUTH);
}

    /**
     * Authenticates the user by checking the credentials against a CSV file
     */
    
    private void authenticateUser() {
    String username = usernameField.getText().trim();
    String password = new String(passwordField.getPassword()).trim();

    try {
        File file = new File("src/main/java/com/group/motorph/users/users.csv");
        CSVReader reader = new CSVReader(new FileReader(file));

        String[] line;
        reader.readNext(); // skip header
        boolean authenticated = false;

        while ((line = reader.readNext()) != null) {
            if (line[0].equals(username) && line[1].equals(password)) {
                authenticated = true;
                break;
            }
        }

        if (authenticated) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            dispose();  // Close login window
            new PayrollGUI(); // Launch payroll system
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error reading user file: " + ex.getMessage());
    }
}

}
