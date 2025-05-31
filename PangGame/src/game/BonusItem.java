package game;

import java.awt.*;
import java.util.Random;

public class BonusItem {
    private int x, y;
    private int size = 30;
    private int dy = 3;
    private String type;
    private boolean active = true;

    private static final String[] TYPES = {"Double", "Fixed", "Clock", "Dynamite"};

    public BonusItem(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = TYPES[new Random().nextInt(TYPES.length)];
    }

    public void update() {
        y += dy;
        if (y > 600) active = false;
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, size, size);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString(type.substring(0, 1), x + 10, y + 20);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public String getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }
}
