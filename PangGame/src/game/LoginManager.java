package game;

import java.io.*;
import java.util.*;

public class LoginManager {
    private static final String FILE = "users.txt";

    public static boolean authenticate(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] creds = line.split(",");
                if (creds.length == 2 && creds[0].equals(username) && creds[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean register(String username, String password) {
        try {
            File file = new File(FILE);
            if (!file.exists()) file.createNewFile();

            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                    if (line.split(",")[0].equals(username)) {
                        return false;
                    }
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(username + "," + password);
                writer.newLine();
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
