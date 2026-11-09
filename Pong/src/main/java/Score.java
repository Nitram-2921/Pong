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

public class Score extends Rectangle {
    static int GAME_WIDTH;
    static int GAME_HEIGHT;
    int player1;
    int player2;
    int player3;
    int player4;
    String[] playerNames;
    int mode;

    Score(int GAME_WIDTH, int GAME_HEIGHT, String[] names, int mode) {
        Score.GAME_WIDTH = GAME_WIDTH;
        Score.GAME_HEIGHT = GAME_HEIGHT;
        this.playerNames = names;
        this.mode = mode;
    }

    public void draw(Graphics g) {
        if (mode == ModeMenu.MODE_FFA) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 24));
            g.drawString(playerNames[0] + ": " + player1, 20, 35);
            FontMetrics fm = g.getFontMetrics();
            String right = playerNames[1] + ": " + player2;
            g.drawString(right, GAME_WIDTH - fm.stringWidth(right) - 20, 35);
            g.drawString(playerNames[2] + ": " + player3, 20, GAME_HEIGHT - 20);
            String bottom = playerNames[3] + ": " + player4;
            g.drawString(bottom, GAME_WIDTH - fm.stringWidth(bottom) - 20, GAME_HEIGHT - 20);
            g.drawLine(0, GAME_HEIGHT / 2, GAME_WIDTH, GAME_HEIGHT / 2);
            g.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT);
            return;
        }
        g.setColor(Color.white);
        g.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT);

        // Puntajes
        g.setFont(new Font("Consolas", Font.PLAIN, 60));
        g.drawString(String.valueOf(player1 / 10) + String.valueOf(player1 % 10),
                     (GAME_WIDTH / 2) - 85, 50);
        g.drawString(String.valueOf(player2 / 10) + String.valueOf(player2 % 10),
                     (GAME_WIDTH / 2) + 20, 50);

        // Nombres
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        if (mode == ModeMenu.MODE_2V2) {
            g.drawString(playerNames[0] + " / " + playerNames[2], 20, 32);
            FontMetrics fm = g.getFontMetrics();
            String right = playerNames[1] + " / " + playerNames[3];
            g.drawString(right, GAME_WIDTH - fm.stringWidth(right) - 20, 32);
        } else {
            g.drawString(playerNames[0], 20, 32);
            FontMetrics fm = g.getFontMetrics();
            g.drawString(playerNames[1], GAME_WIDTH - fm.stringWidth(playerNames[1]) - 20, 32);
        }
    }
}