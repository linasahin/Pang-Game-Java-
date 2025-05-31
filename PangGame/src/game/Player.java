package game;

import javax.swing.*;
import java.awt.*;

public class Player {
    private int x, y;
    private boolean left, right;
    private final int SPEED = 5;
    private Image image;

    public Player() {
        this.x = 400;
        this.y = 450;
        this.image = new ImageIcon("player.png").getImage();
    }

    public void update() {
        if (left) x -= SPEED;
        if (right) x += SPEED;
        x = Math.max(0, Math.min(x, 750));
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, 115, 115, null);
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }
    
    public void resetPosition() {
        this.x = 400;
        this.y = 450;
    }
}
