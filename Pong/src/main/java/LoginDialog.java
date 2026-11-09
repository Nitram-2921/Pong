/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sala 2 - 7
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginDialog extends JDialog {
    private final int mode; 

    private JTextField[] nameFields;
    private JComboBox<String>[] colorBoxes;

    private String[] playerNames;
    private Color[] playerColors;
    private boolean confirmed = false;

    private final Color[] basicColors = {
        Color.BLUE, Color.RED, Color.YELLOW, Color.ORANGE,
        Color.CYAN, Color.MAGENTA, Color.PINK, Color.WHITE
    };
    private final String[] colorNames = {
        "Azul", "Rojo", "Amarillo", "Naranja",
        "Cian", "Magenta", "Rosa", "Blanco"
    };

    @SuppressWarnings("unchecked")
    public LoginDialog(JFrame parent, int mode) {
        super(parent, "Configuración - Pong Game", true);
        this.mode = mode;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        int numPlayers = (mode == ModeMenu.MODE_2V2 || mode == ModeMenu.MODE_FFA) ? 4 : 2;

        nameFields  = new JTextField[numPlayers];
        colorBoxes  = new JComboBox[numPlayers];
        playerNames = new String[numPlayers];
        playerColors = new Color[numPlayers];

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 0, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);

        JLabel title = new JLabel("PONG GAME - " + (mode == ModeMenu.MODE_FFA ? "FREE FOR ALL" : (numPlayers == 4 ? "2 vs 2" : "1 vs 1")));
        title.setFont(new Font("Consolas", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(title, gbc);

        if (numPlayers == 4 && mode == ModeMenu.MODE_2V2) {
            JLabel hdrL = new JLabel("EQUIPO IZQUIERDO");
            hdrL.setFont(new Font("Consolas", Font.BOLD, 14));
            hdrL.setForeground(new Color(150, 200, 255));
            gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(hdrL, gbc);
        }

        int row = 2;
        for (int i = 0; i < numPlayers; i++) {
          
            if (numPlayers == 4 && mode == ModeMenu.MODE_2V2 && i == 2) {
                JLabel hdrR = new JLabel("EQUIPO DERECHO");
                hdrR.setFont(new Font("Consolas", Font.BOLD, 14));
                hdrR.setForeground(new Color(255, 180, 180));
                gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 3;
                gbc.anchor = GridBagConstraints.WEST;
                panel.add(hdrR, gbc);
            }

            JLabel lbl = new JLabel("Jugador " + (i + 1) + ":");
            lbl.setFont(new Font("Consolas", Font.BOLD, 18));
            lbl.setForeground(Color.WHITE);
            gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.EAST;
            panel.add(lbl, gbc);

            nameFields[i] = new JTextField(12);
            nameFields[i].setFont(new Font("Consolas", Font.PLAIN, 14));
            gbc.gridx = 1; gbc.gridy = row;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(nameFields[i], gbc);

            colorBoxes[i] = new JComboBox<>(colorNames);

            int[] defaults = {0, 1, 4, 5};
            colorBoxes[i].setSelectedIndex(defaults[i % defaults.length]);
            colorBoxes[i].setFont(new Font("Consolas", Font.PLAIN, 14));
            gbc.gridx = 2; gbc.gridy = row;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(colorBoxes[i], gbc);

            row++;
        }

        String controls = (mode == ModeMenu.MODE_FFA)
            ? "<html><center>Controles FREE FOR ALL:<br>J1 (izquierda): W / S &nbsp;&nbsp; J2 (derecha): ↑ / ↓<br>J3 (arriba): A / D &nbsp;&nbsp; J4 (abajo): J / L</center></html>"
            : (numPlayers == 4)
            ? "<html><center>Controles:<br>J1 (izq): W / S &nbsp;&nbsp; J3 (izq): R / F<br>J2 (der): ↑ / ↓ &nbsp;&nbsp; J4 (der): O / L</center></html>"
            : "<html><center>Controles:<br>J1: W / S &nbsp;&nbsp; J2: ↑ / ↓</center></html>";
        JLabel controlsLbl = new JLabel(controls);
        controlsLbl.setFont(new Font("Consolas", Font.PLAIN, 13));
        controlsLbl.setForeground(new Color(200, 200, 200));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(controlsLbl, gbc);

        JButton startBtn = new JButton("Iniciar Juego");
        startBtn.setFont(new Font("Consolas", Font.BOLD, 16));
        startBtn.setBackground(new Color(0, 180, 0));
        startBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        gbc.gridy = row++;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(startBtn, gbc);

        startBtn.addActionListener(e -> {
            for (int i = 0; i < nameFields.length; i++) {
                String n = nameFields[i].getText().trim();
                if (n.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Por favor ingrese el nombre del jugador " + (i + 1) + ".",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                playerNames[i] = n;
            }
            for (int i = 0; i < playerNames.length; i++) {
                for (int j = i + 1; j < playerNames.length; j++) {
                    if (playerNames[i].equalsIgnoreCase(playerNames[j])) {
                        JOptionPane.showMessageDialog(this,
                            "Los nombres deben ser diferentes.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            for (int i = 0; i < colorBoxes.length; i++) {
                playerColors[i] = basicColors[colorBoxes[i].getSelectedIndex()];
            }
            confirmed = true;
            dispose();
        });

        getRootPane().setDefaultButton(startBtn);

        add(panel);
        setSize(560, numPlayers == 4 ? 620 : 420);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                nameFields[0].requestFocusInWindow();
            }
        });
    }

    public boolean isConfirmed() { return confirmed; }
    public String[] getPlayerNames()  { return playerNames; }
    public Color[]  getPlayerColors() { return playerColors; }

    public String getPlayer1Name() { return playerNames[0]; }
    public String getPlayer2Name() { return playerNames[1]; }
    public Color  getPlayer1Color(){ return playerColors[0]; }
    public Color  getPlayer2Color(){ return playerColors[1]; }
}