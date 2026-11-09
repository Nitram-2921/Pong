/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sala 2 - 5
 */
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    static final int GAME_WIDTH = 1500;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 21;

    // 1v1
    static final int PADDLE_WIDTH_1V1  = 25;
    static final int PADDLE_HEIGHT_1V1 = 100;
    static final int PADDLE_SPEED_1V1  = 13;

    // 2v2: más pequeñas y algo más rápidas
    static final int PADDLE_WIDTH_2V2  = 20;
    static final int PADDLE_HEIGHT_2V2 = 75;
    static final int PADDLE_SPEED_2V2  = 16;

    static final int WINNING_SCORE = 15;
    static final int MIN_SEPARATION_2V2 = 10;
    static final int PADDLE_LENGTH_FFA = 150;
    static final int PADDLE_THICKNESS_FFA = 20;
    int lastHitPlayer = -1; // separación mínima entre paletas compañeras

    Thread gameThread;
    Image image;
    Graphics graphics;
    Ball ball;
    Score score;
    Random random;

    Paddle[] paddles;
    int numPaddles;

    String[] playerNames;
    Color[]  playerColors;
    int mode;
    GameFrame frame;

    volatile boolean running = true;
    volatile boolean gameOver = false;
    volatile boolean paused = false;

    JButton restartButton;

    GamePanel(String[] names, Color[] colors, int mode, GameFrame frame) {
        this.playerNames  = names;
        this.playerColors = colors;
        this.mode = mode;
        this.frame = frame;

        numPaddles = (mode == ModeMenu.MODE_2V2 || mode == ModeMenu.MODE_FFA) ? 4 : 2;
        paddles = new Paddle[numPaddles];

        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT, names, mode);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);
        this.setBackground(new Color(0, 153, 0));
        this.setLayout(null);

        restartButton = new JButton("Reiniciar Juego");
        restartButton.setFont(new Font("Consolas", Font.BOLD, 24));
        restartButton.setBackground(new Color(0, 180, 0));
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusPainted(false);
        restartButton.setFocusable(false);
        restartButton.setBounds((GAME_WIDTH / 2) - 130, (GAME_HEIGHT / 2) + 30, 260, 50);
        restartButton.setVisible(false);
        restartButton.addActionListener(e -> {
            running = false;
            frame.startNewGame();
        });
        this.add(restartButton);

        gameThread = new Thread(this);
        gameThread.start();
    }

    private void togglePause() {
        if (gameOver) return;
        paused = !paused;
        if (paused) {
            for (Paddle p : paddles) p.setYDirection(0);
        }
        repaint();
    }

    private void returnToMenu() {
        running = false;
        frame.startNewGame();
    }

    public void newBall() {
        random = new Random();
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2),
                        random.nextInt(GAME_HEIGHT - BALL_DIAMETER),
                        BALL_DIAMETER, BALL_DIAMETER);
    }

    public void newPaddles() {
        if (mode == ModeMenu.MODE_FFA) {
            int len = PADDLE_LENGTH_FFA, thick = PADDLE_THICKNESS_FFA;
            paddles[0] = new Paddle(0, GAME_HEIGHT / 2 - len / 2, thick, len, 1, playerColors[0], 15);
            paddles[1] = new Paddle(GAME_WIDTH - thick, GAME_HEIGHT / 2 - len / 2, thick, len, 2, playerColors[1], 15);
            paddles[2] = new Paddle(GAME_WIDTH / 2 - len / 2, 0, len, thick, 3, playerColors[2], 15, true);
            paddles[3] = new Paddle(GAME_WIDTH / 2 - len / 2, GAME_HEIGHT - thick, len, thick, 4, playerColors[3], 15, true);
        } else if (mode == ModeMenu.MODE_2V2) {
            int w  = PADDLE_WIDTH_2V2;
            int h  = PADDLE_HEIGHT_2V2;
            int sp = PADDLE_SPEED_2V2;
            int gap = 40;

            int topY = (GAME_HEIGHT / 2) - h - (gap / 2);
            int botY = (GAME_HEIGHT / 2) + (gap / 2);

            paddles[0] = new Paddle(0, topY, w, h, 1, playerColors[0], sp);
            paddles[2] = new Paddle(0, botY, w, h, 3, playerColors[2], sp);

            paddles[1] = new Paddle(GAME_WIDTH - w, topY, w, h, 2, playerColors[1], sp);
            paddles[3] = new Paddle(GAME_WIDTH - w, botY, w, h, 4, playerColors[3], sp);
        } else {
            int w  = PADDLE_WIDTH_1V1;
            int h  = PADDLE_HEIGHT_1V1;
            int sp = PADDLE_SPEED_1V1;

            paddles[0] = new Paddle(0, (GAME_HEIGHT / 2) - (h / 2), w, h, 1, playerColors[0], sp);
            paddles[1] = new Paddle(GAME_WIDTH - w, (GAME_HEIGHT / 2) - (h / 2), w, h, 2, playerColors[1], sp);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);

        if (!gameOver) {
            g.setColor(new Color(255, 255, 255, 180));
            g.setFont(new Font("Consolas", Font.BOLD, 18));
            FontMetrics fm = g.getFontMetrics();

            String hint1 = paused
                ? "PAUSA - Presiona ESPACIO para reanudar"
                : "Presiona ESPACIO para pausar";
            String hint2 = "Presiona BACKSPACE para volver al menú principal";

            int lineHeight = fm.getHeight();
            int baseY = GAME_HEIGHT - 20 - fm.getDescent();
            g.drawString(hint2, 20, baseY);
            g.drawString(hint1, 20, baseY - lineHeight);
        }

        if (paused && !gameOver) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 0, 0, 120));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Consolas", Font.BOLD, 72));
            String text = "PAUSA";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(text, (GAME_WIDTH - fm.stringWidth(text)) / 2, GAME_HEIGHT / 2 - 20);
        }

        if (gameOver) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            String winner;
            if (mode == ModeMenu.MODE_FFA) {
                int[] scores = {score.player1, score.player2, score.player3, score.player4};
                int best = 0;
                for (int i = 1; i < scores.length; i++) if (scores[i] > scores[best]) best = i;
                winner = playerNames[best];
            } else if (mode == ModeMenu.MODE_2V2) {
                winner = (score.player1 >= WINNING_SCORE)
                    ? playerNames[0] + " y " + playerNames[2]
                    : playerNames[1] + " y " + playerNames[3];
            } else {
                winner = (score.player1 >= WINNING_SCORE) ? playerNames[0] : playerNames[1];
            }

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Consolas", Font.BOLD, 48));
            String text = "¡" + winner + " Gana!";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(text, (GAME_WIDTH - fm.stringWidth(text)) / 2, (GAME_HEIGHT / 2) - 20);

            g.setColor(new Color(255, 255, 255, 200));
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            String hint2 = "Presiona BACKSPACE para volver al menú principal";
            FontMetrics fm2 = g.getFontMetrics();
            g.drawString(hint2, 20, GAME_HEIGHT - 20 - fm2.getDescent());
        }
    }

    public void draw(Graphics g) {
        for (Paddle p : paddles) p.draw(g);
        ball.draw(g);
        score.draw(g);
    }

    public void move() {
        for (Paddle p : paddles) p.move();
        checkPaddleCollision();   // 👈 colisión entre compañeras + límites de pantalla
        ball.move();
    }

    /**
     * Aplica límites de pantalla a todas las paletas y, en modo 2v2,
     * impide que las paletas compañeras (mismo lado) se superpongan.
     */
    private void checkPaddleCollision() {
        // 1) Límites de pantalla (arriba/abajo) para todas
        for (Paddle p : paddles) {
            if (p.horizontal) {
                if (p.x < 0) p.x = 0;
                if (p.x > GAME_WIDTH - p.width) p.x = GAME_WIDTH - p.width;
            } else {
                if (p.y < 0) p.y = 0;
                if (p.y > GAME_HEIGHT - p.height) p.y = GAME_HEIGHT - p.height;
            }
        }

        if (mode == ModeMenu.MODE_FFA || mode != ModeMenu.MODE_2V2) return;

        // 2) Pares compañeras por lado: {arriba, abajo}
        //    izquierda: 0 (id1) y 2 (id3)
        //    derecha:   1 (id2) y 3 (id4)
        int[][] pairs = { {0, 2}, {1, 3} };
        int sep = MIN_SEPARATION_2V2;

        for (int[] pair : pairs) {
            Paddle top = paddles[pair[0]];
            Paddle bot = paddles[pair[1]];

            if (top.y + top.height + sep > bot.y) {
                int overlap = (top.y + top.height + sep) - bot.y;
                int half = overlap / 2;
                bot.y += half;
                top.y -= overlap - half;
            }
        }

        // 3) Reaplicar límites de pantalla
        for (Paddle p : paddles) {
            if (p.y <= 0) p.y = 0;
            if (p.y >= GAME_HEIGHT - p.height) p.y = GAME_HEIGHT - p.height;
        }

        // 4) Segunda pasada por si el empuje las volvió a solapar
        for (int[] pair : pairs) {
            Paddle top = paddles[pair[0]];
            Paddle bot = paddles[pair[1]];

            if (top.y + top.height + sep > bot.y) {
                int overlap = (top.y + top.height + sep) - bot.y;
                if (bot.y + bot.height + overlap <= GAME_HEIGHT) {
                    bot.y += overlap;
                } else if (top.y - overlap >= 0) {
                    top.y -= overlap;
                } else {
                    int half = overlap / 2;
                    bot.y += half;
                    top.y -= overlap - half;
                    if (top.y < 0) top.y = 0;
                    if (bot.y + bot.height > GAME_HEIGHT) bot.y = GAME_HEIGHT - bot.height;
                }
            }
        }
    }

    public void checkCollision() {
        if (gameOver) return;

        if (mode == ModeMenu.MODE_FFA) {
            for (Paddle p : paddles) {
                if (ball.intersects(p)) {
                    lastHitPlayer = p.id - 1;
                    if (p.horizontal) {
                        ball.yVelocity = (p.id == 3) ? Math.abs(ball.yVelocity) : -Math.abs(ball.yVelocity);
                        ball.xVelocity += (ball.x + BALL_DIAMETER / 2 < p.x + p.width / 2) ? -1 : 1;
                    } else {
                        ball.xVelocity = (p.id == 1) ? Math.abs(ball.xVelocity) : -Math.abs(ball.xVelocity);
                        ball.yVelocity += (ball.y + BALL_DIAMETER / 2 < p.y + p.height / 2) ? -1 : 1;
                    }
                    ball.setXDirection(ball.xVelocity);
                    ball.setYDirection(ball.yVelocity);
                    break;
                }
            }
            int missedPlayer = -1;
            if (ball.x <= 0 && ball.xVelocity < 0) missedPlayer = 0;
            else if (ball.x >= GAME_WIDTH - BALL_DIAMETER && ball.xVelocity > 0) missedPlayer = 1;
            else if (ball.y <= 0 && ball.yVelocity < 0) missedPlayer = 2;
            else if (ball.y >= GAME_HEIGHT - BALL_DIAMETER && ball.yVelocity > 0) missedPlayer = 3;

            if (missedPlayer != -1) {
                if (lastHitPlayer >= 0 && lastHitPlayer != missedPlayer) {
                    switch (lastHitPlayer) {
                        case 0 -> score.player1++;
                        case 1 -> score.player2++;
                        case 2 -> score.player3++;
                        case 3 -> score.player4++;
                    }
                    int[] scores = {score.player1, score.player2, score.player3, score.player4};
                    if (scores[lastHitPlayer] >= WINNING_SCORE) endGame();
                }
                if (!gameOver) { lastHitPlayer = -1; newPaddles(); newBall(); }
            }
            return;
        }

        if (ball.y <= 0 || ball.y >= GAME_HEIGHT - BALL_DIAMETER) ball.setYDirection(-ball.yVelocity);
        for (Paddle p : paddles) {
            if (ball.intersects(p)) {
                boolean leftSide = (p.x < GAME_WIDTH / 2);
                ball.xVelocity = Math.abs(ball.xVelocity) + 1;
                if (!leftSide) ball.xVelocity = -ball.xVelocity;
                ball.yVelocity += (ball.yVelocity > 0) ? 1 : -1;
                ball.setXDirection(ball.xVelocity);
                ball.setYDirection(ball.yVelocity);
                break;
            }
        }
        if (ball.x <= 0) {
            score.player2++;
            if (score.player2 >= WINNING_SCORE) endGame(); else { newPaddles(); newBall(); }
        }
        if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            score.player1++;
            if (score.player1 >= WINNING_SCORE) endGame(); else { newPaddles(); newBall(); }
        }
    }

    private void endGame() {
        gameOver = true;
        running = false;
        SwingUtilities.invokeLater(() -> {
            restartButton.setVisible(true);
        });
    }

    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                if (!gameOver && !paused) {
                    move();
                    checkCollision();
                }
                repaint();
                delta--;
            }
        }
    }

    public class AL extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                returnToMenu();
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
                togglePause();
                return;
            }
            if (gameOver || paused) return;
            for (Paddle p : paddles) p.keyPressed(e);
        }
        @Override
        public void keyReleased(KeyEvent e) {
            for (Paddle p : paddles) p.keyReleased(e);
        }
    }
}