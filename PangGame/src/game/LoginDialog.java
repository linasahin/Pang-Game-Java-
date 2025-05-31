package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean succeeded;
    private String currentUser;

    public LoginDialog(JFrame parent) {
        super(parent, "Login", true);
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(parent);

        JLabel nameLabel = new JLabel("Username:");
        nameLabel.setBounds(100, 40, 200, 25);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(nameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 70, 200, 30);
        usernameField.setHorizontalAlignment(SwingConstants.CENTER);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(100, 110, 200, 25);
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 140, 200, 30);
        passwordField.setHorizontalAlignment(SwingConstants.CENTER);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(80, 190, 100, 30);
        add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(220, 190, 100, 30);
        add(registerButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (validateUser(username, password)) {
                succeeded = true;
                currentUser = username;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (!username.isEmpty() && !password.isEmpty()) {
                if (userExists(username)) {
                    JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    registerUser(username, password);
                    JOptionPane.showMessageDialog(this, "User registered successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a username and password");
            }
        });
    }

    private boolean validateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.split(",")[0].equals(username)) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void registerUser(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public String getCurrentUser() {
        return currentUser;
    }
}
