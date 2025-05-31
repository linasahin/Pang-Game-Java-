package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener {
    private javax.swing.Timer timer;
    private Player player;
    private ArrayList<Ball> balls;
    private ArrayList<Bullet> bullets;
    private ArrayList<BonusItem> bonusItems = new ArrayList<>();
    private boolean gameOver = false;
    private boolean saved = false;
    private int score = 0;
    private int difficulty;
    private int bonusTimer = 0;
    private boolean freezeActive = false;
    private long freezeEndTime;
    private boolean doubleShot = false;
    private boolean fixedArrow = false;
    private String lastBonus = "";
    private int backgroundIndex = 1;
    private String currentUser = "";
    private int timeLeft;
    private int lives;
    private int maxLives;

    private Image heartFull = new ImageIcon("heart_full.png").getImage();
    private Image heartEmpty = new ImageIcon("heart_empty.png").getImage();

    public GamePanel() {
        this(askDifficulty());
    }

    public GamePanel(int difficulty) {
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        requestFocusInWindow();
        this.difficulty = difficulty;

        String[] options = {"Background 1", "Background 2", "Background 3", "Background 4"};
        int choice = JOptionPane.showOptionDialog(null, "Choose your background:", "Background Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        backgroundIndex = (choice == -1) ? 1 : choice + 1;

        switch (difficulty) {
            case 1 -> maxLives = lives = 3;
            case 2 -> maxLives = lives = 2;
            default -> maxLives = lives = 1;
        }

        player = new Player();
        bullets = new ArrayList<>();
        balls = new ArrayList<>();

        int ballCount = switch (difficulty) {
            case 1 -> 2;
            case 2 -> 3;
            case 3 -> 4;
            case 4 -> 5;
            default -> 3;
        };

        int ballSize = switch (difficulty) {
            case 1 -> 80;
            case 2 -> 60;
            case 3 -> 50;
            case 4 -> 40;
            default -> 60;
        };

        int speed = switch (difficulty) {
            case 1 -> 2;
            case 2 -> 4;
            case 3 -> 6;
            case 4 -> 8;
            default -> 4;
        };

        timeLeft = switch (difficulty) {
            case 1 -> 120;
            case 2 -> 90;
            case 3 -> 75;
            case 4 -> 60;
            default -> 90;
        };

        for (int i = 0; i < ballCount; i++) {
            balls.add(new Ball(100 + i * 100, 100, ballSize, speed, -18));
        }

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) player.setLeft(true);
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.setRight(true);
                if (e.getKeyCode() == KeyEvent.VK_SPACE) shoot();
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) player.setLeft(false);
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.setRight(false);
            }
        });

        timer = new javax.swing.Timer(20, this);
        timer.start();

        new javax.swing.Timer(1000, e -> {
            if (!gameOver) timeLeft--;
            if (timeLeft <= 0 && !gameOver) {
                gameOver = true;
                repaint();
            }
        }).start();

        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
            JFrame top = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (top != null) {
                top.setSize(800, 600);
                top.setResizable(false);
                top.setVisible(true);
                top.revalidate();
                top.repaint();
            }
        });
    }

    private static int askDifficulty() {
        String[] difficulties = {"Easy", "Medium", "Hard", "Extreme"};
        int choice = JOptionPane.showOptionDialog(null, "Choose difficulty level:", "Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);
        return (choice == -1) ? 2 : choice + 1;
    }

    public void setCurrentUser(String user) {
        this.currentUser = user;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setBackgroundIndex(int index) {
        backgroundIndex = index;
    }

    private void shoot() {
        if (doubleShot) {
            bullets.add(new Bullet(player.getX() + 10, player.getY(), fixedArrow));
            bullets.add(new Bullet(player.getX() + 30, player.getY(), fixedArrow));
        } else {
            bullets.add(new Bullet(player.getX() + 20, player.getY(), fixedArrow));
        }
    }

    private String getDifficultyName(int diff) {
        return switch (diff) {
            case 1 -> "Easy";
            case 2 -> "Medium";
            case 3 -> "Hard";
            case 4 -> "Extreme";
            default -> "Unknown";
        };
    }

    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        player.update();
        checkPlayerCollision();

        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();
            if (!b.isActive()) it.remove();
        }

        Iterator<Ball> ballIt = balls.iterator();
        ArrayList<Ball> newBalls = new ArrayList<>();
        while (ballIt.hasNext()) {
            Ball ball = ballIt.next();

            if (!freezeActive || System.currentTimeMillis() > freezeEndTime) {
                freezeActive = false;
                ball.update();
            }

            Iterator<Bullet> bulletIt = bullets.iterator();
            while (bulletIt.hasNext()) {
                Bullet bullet = bulletIt.next();
                if (ball.getBounds().intersects(bullet.getBounds())) {
                    Utils.playSound("pop.wav");
                    bulletIt.remove();
                    ballIt.remove();
                    if (ball.getSize() > 30) {
                        int newSize = ball.getSize() / 2;
                        newBalls.add(new Ball(ball.getX(), ball.getY(), newSize, -3, -18));
                        newBalls.add(new Ball(ball.getX(), ball.getY(), newSize, 3, -18));
                    }
                    score += 10;
                    break;
                }
            }
        }
        balls.addAll(newBalls);

        if (balls.isEmpty() && !gameOver) {
            gameOver = true;
            Utils.playSound("win.wav");
        }

        if (gameOver && !saved) {
            saved = true;
            if (!currentUser.equals("")) {
                GameDataManager.saveGame(currentUser, score, getDifficultyName(difficulty));
            }

            int choice = JOptionPane.showOptionDialog(null,
                    (balls.isEmpty() ? "You cleared all bubbles!\n" : "") +
                            "Game Over! Your score: " + score + "\nPlay again?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Restart", "Quit"},
                    "Restart");

            if (choice == 0) {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                GamePanel newPanel = new GamePanel(difficulty);
                newPanel.setCurrentUser(currentUser);
                topFrame.setContentPane(newPanel);
                topFrame.setJMenuBar(new MenuBar(topFrame, newPanel));
                topFrame.setSize(800, 600);
                topFrame.setVisible(true);
                SwingUtilities.invokeLater(() -> {
                    newPanel.requestFocusInWindow();
                    topFrame.revalidate();
                    topFrame.repaint();
                });
                topFrame.revalidate();
            } else {
                System.exit(0);
            }
        }

        bonusTimer++;
        if (bonusTimer >= 300) {
            bonusItems.add(new BonusItem(new Random().nextInt(750), 0));
            bonusTimer = 0;
        }

        Iterator<BonusItem> bonusIt = bonusItems.iterator();
        while (bonusIt.hasNext()) {
            BonusItem item = bonusIt.next();
            item.update();
            if (item.isActive() && item.getBounds().intersects(new Rectangle(player.getX(), player.getY(), 50, 20))) {
                lastBonus = item.getType();
                switch (item.getType()) {
                    case "Double" -> doubleShot = true;
                    case "Fixed" -> fixedArrow = true;
                    case "Clock" -> {
                        freezeActive = true;
                        freezeEndTime = System.currentTimeMillis() + 5000;
                    }
                    case "Dynamite" -> {
                        ArrayList<Ball> split = new ArrayList<>();
                        for (Ball b : balls) {
                            if (b.getSize() > 30) {
                                int s = b.getSize() / 2;
                                split.add(new Ball(b.getX(), b.getY(), s, -3, -18));
                                split.add(new Ball(b.getX(), b.getY(), s, 3, -18));
                            }
                        }
                        balls.clear();
                        balls.addAll(split);
                    }
                }
                item.deactivate();
            }
            if (!item.isActive()) bonusIt.remove();
        }

        repaint();
    }

    private long lastHitTime = 0;

    private void checkPlayerCollision() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastHitTime < 1000) return;

        Rectangle playerBounds = new Rectangle(player.getX(), player.getY(), 50, 50);
        Iterator<Ball> it = balls.iterator();
        while (it.hasNext()) {
            Ball ball = it.next();
            if (ball.getBounds().intersects(playerBounds)) {
                Utils.playSound("hit.wav");
                lives--;
                lastHitTime = currentTime;

                // TEMP: move the ball away slightly to stop it from hitting twice
                ball.setY(ball.getY() - 50);

                if (lives <= 0) {
                    gameOver = true;
                }
                break;
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image bg = new ImageIcon("background" + backgroundIndex + ".png").getImage();
        g.drawImage(bg, 0, 0, 800, 600, null);

        player.draw(g);
        for (Ball ball : balls) ball.draw(g);
        for (Bullet bullet : bullets) bullet.draw(g);
        for (BonusItem item : bonusItems) item.draw(g);

        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 100);
        g.drawString("Time: " + timeLeft, 10, 120);
        if (!lastBonus.equals("")) g.drawString("Bonus: " + lastBonus, 10, 140);

        for (int i = 0; i < maxLives; i++) {
            Image heart = (i < lives) ? heartFull : heartEmpty;
            g.drawImage(heart, 10 + i * 40, 10, 36, 36, null);
        }

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 300, 300);
        }
    }
}
