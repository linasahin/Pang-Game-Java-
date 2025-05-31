package game;

import javax.swing.*;
import java.awt.event.*;

public class MenuBar extends JMenuBar {
    public MenuBar(JFrame frame, GamePanel gamePanel) {
        JMenu gameMenu = new JMenu("Game");
        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(e -> {
            GamePanel newPanel = new GamePanel(1);
            newPanel.setCurrentUser(gamePanel.getCurrentUser());
            frame.setContentPane(newPanel);
            frame.revalidate();
        });
        gameMenu.add(restartItem);

        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(quitItem);

        JMenu difficultyMenu = new JMenu("Difficulty");
        String[] levels = { "Easy", "Medium", "Hard", "Extreme" };
        for (int i = 0; i < levels.length; i++) {
            int level = i + 1;
            JMenuItem item = new JMenuItem(levels[i]);
            item.addActionListener(e -> {
                GamePanel newPanel = new GamePanel(level);
                newPanel.setCurrentUser(gamePanel.getCurrentUser());
                frame.setContentPane(newPanel);
                frame.revalidate();
            });
            difficultyMenu.add(item);
        }

        JMenu optionsMenu = new JMenu("Options");

        JMenu bgMenu = new JMenu("Background");
        for (int i = 1; i <= 4; i++) {
            int bgNum = i;
            JMenuItem item = new JMenuItem("Background " + i);
            item.addActionListener(e -> gamePanel.setBackgroundIndex(bgNum));
            bgMenu.add(item);
        }
        optionsMenu.add(bgMenu);

        add(gameMenu);
        add(difficultyMenu);
        add(optionsMenu);
    }
}
