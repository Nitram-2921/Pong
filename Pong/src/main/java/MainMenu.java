/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sala 2 - 5
 */
import java.awt.*;
import javax.swing.*;

public class MainMenu extends JDialog {
    private boolean startGame = false;

    public MainMenu(JFrame parent) {
        super(parent, "Pong Game - Menú Principal", true);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 0, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        JLabel title = new JLabel("PONG GAME");
        title.setFont(new Font("Consolas", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(title, gbc);

        JLabel subtitle = new JLabel("Juego de Pong para 2-4 jugadores");
        subtitle.setFont(new Font("Consolas", Font.PLAIN, 18));
        subtitle.setForeground(new Color(200, 200, 200));
        gbc.gridy = 1;
        panel.add(subtitle, gbc);

        JButton playBtn = new JButton("Jugar");
        playBtn.setFont(new Font("Consolas", Font.BOLD, 24));
        playBtn.setBackground(new Color(0, 180, 0));
        playBtn.setForeground(Color.WHITE);
        playBtn.setFocusPainted(false);
        playBtn.setPreferredSize(new Dimension(200, 60));
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        panel.add(playBtn, gbc);

        JButton exitBtn = new JButton("Salir");
        exitBtn.setFont(new Font("Consolas", Font.BOLD, 18));
        exitBtn.setBackground(new Color(150, 0, 0));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        exitBtn.setPreferredSize(new Dimension(200, 45));
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 15, 15, 15);
        panel.add(exitBtn, gbc);

        playBtn.addActionListener(e -> {
            startGame = true;
            dispose();
        });

        exitBtn.addActionListener(e -> {
            startGame = false;
            dispose();
        });

        getRootPane().setDefaultButton(playBtn);

        add(panel);
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public boolean isStartGame() {
        return startGame;
    }
}