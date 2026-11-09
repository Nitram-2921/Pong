import java.awt.*;
import java.awt.event.*;

public class Paddle extends Rectangle {
    int id;
    int yVelocity;
    int xVelocity;
    int speed;
    Color paddleColor;
    boolean horizontal;

    Paddle(int x, int y, int width, int height, int id, Color color, int speed) {
        this(x, y, width, height, id, color, speed, false);
    }

    Paddle(int x, int y, int width, int height, int id, Color color, int speed, boolean horizontal) {
        super(x, y, width, height);
        this.id = id;
        this.paddleColor = color;
        this.speed = speed;
        this.horizontal = horizontal;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (horizontal) {
            if (id == 3) {
                if (key == KeyEvent.VK_A) xVelocity = -speed;
                if (key == KeyEvent.VK_D) xVelocity = speed;
            } else if (id == 4) {
                if (key == KeyEvent.VK_J) xVelocity = -speed;
                if (key == KeyEvent.VK_L) xVelocity = speed;
            }
        } else {
            switch (id) {
                case 1 -> { if (key == KeyEvent.VK_W) yVelocity = -speed; if (key == KeyEvent.VK_S) yVelocity = speed; }
                case 2 -> { if (key == KeyEvent.VK_UP) yVelocity = -speed; if (key == KeyEvent.VK_DOWN) yVelocity = speed; }
                case 3 -> { if (key == KeyEvent.VK_R) yVelocity = -speed; if (key == KeyEvent.VK_F) yVelocity = speed; }
                case 4 -> { if (key == KeyEvent.VK_O) yVelocity = -speed; if (key == KeyEvent.VK_L) yVelocity = speed; }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (horizontal) {
            if ((id == 3 && (key == KeyEvent.VK_A || key == KeyEvent.VK_D)) ||
                (id == 4 && (key == KeyEvent.VK_J || key == KeyEvent.VK_L))) xVelocity = 0;
        } else {
            switch (id) {
                case 1 -> { if (key == KeyEvent.VK_W || key == KeyEvent.VK_S) yVelocity = 0; }
                case 2 -> { if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) yVelocity = 0; }
                case 3 -> { if (key == KeyEvent.VK_R || key == KeyEvent.VK_F) yVelocity = 0; }
                case 4 -> { if (key == KeyEvent.VK_O || key == KeyEvent.VK_L) yVelocity = 0; }
            }
        }
    }

    public void setYDirection(int direction) { yVelocity = direction; }
    public void move() { x += xVelocity; y += yVelocity; }

    public void draw(Graphics g) {
        g.setColor(paddleColor);
        g.fillRect(x, y, width, height);
    }
}
