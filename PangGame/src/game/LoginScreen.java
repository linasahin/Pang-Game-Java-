package game;

import javax.swing.*;
import java.awt.*;

public class LoginScreen {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pang Game - Login");
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel background = new JLabel(new ImageIcon(LoginScreen.class.getResource("/start_bg.png")));
        background.setBounds(0, 0, 600, 450);
        background.setLayout(null);
        frame.setContentPane(background);

        JLabel nameLabel = new JLabel("Enter Your Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(200, 100, 200, 30);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(200, 140, 200, 35);
        nameField.setHorizontalAlignment(SwingConstants.CENTER);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        background.add(nameField);

        JButton loginButton = new JButton("Start Game");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBounds(240, 190, 120, 35);
        loginButton.setBackground(new Color(173, 216, 230));
        background.add(loginButton);

        loginButton.addActionListener(e -> {
            String name = nameField.getText();
            if (!name.isEmpty()) {
                frame.dispose();
                JFrame gameFrame = new JFrame("Pang Game - Player: " + name);
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                GamePanel panel = new GamePanel();
                panel.setCurrentUser(name);
                gameFrame.add(panel);
                gameFrame.pack();
                gameFrame.setLocationRelativeTo(null);
                gameFrame.setVisible(true);
                panel.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter your name.");
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
