package game;

import java.awt.*;

public class Bullet {
    private int x, y;
    private boolean fixed = false;
    private boolean active = true;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Bullet(int x, int y, boolean fixed) {
        this(x, y);
        this.fixed = fixed;
    }

    public void update() {
        if (!fixed) {
            y -= 10;
            if (y < 0) {
                y = 0;
                active = false;
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        if (fixed) {
            g.fillRect(x, 0, 5, y + 20);
        } else {
            g.fillRect(x, y, 5, 20);
        }
    }

    public Rectangle getBounds() {
        if (fixed) {
            return new Rectangle(x, 0, 5, y + 20);
        }
        return new Rectangle(x, y, 5, 20);
    }

    public boolean isActive() {
        return active || fixed;
    }

    public boolean isFixed() {
        return fixed;
    }
}
