package game;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GameDataManager {
    private static final String FILE = "scores.txt";

    public static List<String> getTopScores() {
        List<String> all = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                all.add(line);
            }
        } catch (IOException e) {}

        all.sort((a, b) -> {
            int scoreA = Integer.parseInt(a.split("\\|")[1].trim());
            int scoreB = Integer.parseInt(b.split("\\|")[1].trim());
            return Integer.compare(scoreB, scoreA);
        });

        return all.subList(0, Math.min(10, all.size()));
    }

    public static void saveGame(String username, int score, String difficulty) {
        try (FileWriter writer = new FileWriter("scores.txt", true)) {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write(username + " | " + score + " | " + difficulty + " | " + date + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<String> getHistory(String username) {
        List<String> userScores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + " |")) {
                    userScores.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userScores;
    }

}
