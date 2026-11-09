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

public class GameFrame extends JFrame {
    GamePanel panel;
    String[] playerNames;
    Color[]  playerColors;
    int currentMode = ModeMenu.MODE_1V1;

    GameFrame() {
        this.setTitle("Pong Game");
        this.setResizable(false);
        this.setBackground(new Color(0, 153, 0));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        startNewGame();
    }

    public void startNewGame() {

        this.setVisible(false);

        MainMenu menu = new MainMenu(this);
        menu.setVisible(true);
        if (!menu.isStartGame()) {
            System.exit(0);
            return;
        }

        ModeMenu modeMenu = new ModeMenu(this);
        modeMenu.setVisible(true);
        int mode = modeMenu.getSelectedMode();
        if (mode == ModeMenu.MODE_NONE) {
            startNewGame();
            return;
        }
        currentMode = mode;

        LoginDialog login = new LoginDialog(this, currentMode);
        login.setVisible(true);
        if (!login.isConfirmed()) {
            System.exit(0);
            return;
        }

        playerNames  = login.getPlayerNames();
        playerColors = login.getPlayerColors();

        if (panel != null) {
            this.remove(panel);
        }

        panel = new GamePanel(playerNames, playerColors, currentMode, this);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        panel.requestFocusInWindow();
    }
}