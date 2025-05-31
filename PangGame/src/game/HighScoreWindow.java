package game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HighScoreWindow extends JFrame {
    public HighScoreWindow() {
        setTitle("High Scores");
        setSize(400, 300);
        setLocationRelativeTo(null);

        List<String> scores = GameDataManager.getTopScores();
        JTextArea textArea = new JTextArea();
        for (String line : scores) textArea.append(line + "\n");
        textArea.setEditable(false);

        add(new JScrollPane(textArea));
        setVisible(true);
    }
}
