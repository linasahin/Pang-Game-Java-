package game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HistoryWindow extends JFrame {
    public HistoryWindow(String username) {
        setTitle("History - " + username);
        setSize(400, 300);
        setLocationRelativeTo(null);

        List<String> history = GameDataManager.getHistory(username);
        JTextArea textArea = new JTextArea();
        for (String line : history) textArea.append(line + "\n");
        textArea.setEditable(false);

        add(new JScrollPane(textArea));
        setVisible(true);
    }
}
