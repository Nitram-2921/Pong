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

public class ModeMenu extends JDialog {
    public static final int MODE_NONE = 0;
    public static final int MODE_1V1 = 1;
    public static final int MODE_2V2 = 2;
    public static final int MODE_FFA = 3;

    private int selectedMode = MODE_NONE;

    public ModeMenu(JFrame parent) {
        super(parent, "Pong Game - Modo de Juego", true);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 0, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel title = new JLabel("SELECCIONA EL MODO");
        title.setFont(new Font("Consolas", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(title, gbc);

        JButton btn1v1 = new JButton("1 vs 1");
        btn1v1.setFont(new Font("Consolas", Font.BOLD, 22));
        btn1v1.setBackground(new Color(0, 180, 0));
        btn1v1.setForeground(Color.WHITE);
        btn1v1.setFocusPainted(false);
        btn1v1.setPreferredSize(new Dimension(200, 60));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.insets = new Insets(30, 15, 15, 15);
        panel.add(btn1v1, gbc);

        JButton btn2v2 = new JButton("2 vs 2");
        btn2v2.setFont(new Font("Consolas", Font.BOLD, 22));
        btn2v2.setBackground(new Color(0, 120, 200));
        btn2v2.setForeground(Color.WHITE);
        btn2v2.setFocusPainted(false);
        btn2v2.setPreferredSize(new Dimension(200, 60));
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(btn2v2, gbc);

        JButton btnFfa = new JButton("FREE FOR ALL (4 jugadores)");
        btnFfa.setFont(new Font("Consolas", Font.BOLD, 20));
        btnFfa.setBackground(new Color(150, 70, 190));
        btnFfa.setForeground(Color.WHITE);
        btnFfa.setFocusPainted(false);
        btnFfa.setPreferredSize(new Dimension(420, 55));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 15, 15, 15);
        panel.add(btnFfa, gbc);

        JButton backBtn = new JButton("Volver");
        backBtn.setFont(new Font("Consolas", Font.BOLD, 16));
        backBtn.setBackground(new Color(120, 120, 120));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 15, 15, 15);
        panel.add(backBtn, gbc);

        btn1v1.addActionListener(e -> {
            selectedMode = MODE_1V1;
            dispose();
        });
        btn2v2.addActionListener(e -> {
            selectedMode = MODE_2V2;
            dispose();
        });
        btnFfa.addActionListener(e -> { selectedMode = MODE_FFA; dispose(); });
        backBtn.addActionListener(e -> {
            selectedMode = MODE_NONE;
            dispose();
        });

        getRootPane().setDefaultButton(btn1v1);

        add(panel);
        setSize(650, 430);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public int getSelectedMode() {
        return selectedMode;
    }
}