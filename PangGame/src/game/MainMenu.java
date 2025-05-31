package game;

import javax.swing.*;

public class MainMenu {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pang Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LoginDialog loginDialog = new LoginDialog(frame);
        loginDialog.setVisible(true);

        if (loginDialog.isSucceeded()) {
            String currentUser = loginDialog.getCurrentUser();
            GamePanel gamePanel = new GamePanel();
            gamePanel.setCurrentUser(currentUser);

            frame.setContentPane(gamePanel);
            frame.setJMenuBar(new MenuBar(frame, gamePanel));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } else {
            System.exit(0);
        }
    }
}
