package game;

import java.awt.*;

public class Ball {
    private int x, y;
    private int dx, dy;
    private int size;
    private boolean active = true;
    private int initialDy;

    public Ball(int x, int y, int size, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.dx = dx;
        this.dy = dy;
        this.dy = dy;
        this.initialDy = Math.abs(dy);
    }

    public void update() {
        x += dx;
        y += dy;

        if (x <= 0 || x + size >= 800) {
            dx *= -1;
        }

        if (y + size >= 580) {
            y = 580 - size;
            dy = -22;
        }

        dy += 1; 
    }


    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public void draw(Graphics g) {
        g.setColor(Color.PINK);
        g.fillOval(x, y, size, size);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSize() {
        return size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDX() {
        return dx;
    }

    public int getDY() {
        return dy;
    }
    
    public void setY(int y) {
        this.y = y;
    }

}
