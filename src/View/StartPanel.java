package View;

import Controller.ResourceLoader;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class StartPanel extends JPanel {

    private static final Color COLOR_FONDO = new Color(5, 8, 20);
    private static final Color COLOR_ACENTO = new Color(0, 183, 255);
    private static final Color COLOR_ACENTO_OSCURO = new Color(110, 52, 217);

    private final JButton startButton = new JButton("Iniciar Juego");
    private final JButton controlsButton = new JButton("Mostrar Controles");
    private final JButton leaderboardButton = new JButton("Tabla de Líderes");
    private final BufferedImage background = ResourceLoader.cargarImagen("fondoEStrellas.png");

    public StartPanel(Runnable onStartGame, Runnable onShowControls, Runnable onShowLeaderboard) {
        setLayout(new GridBagLayout());
        setBackground(COLOR_FONDO);
        setBorder(new EmptyBorder(40, 60, 40, 60));
        setOpaque(false);

        JLabel title = crearTitulo();
        JLabel subtitle = crearSubtitulo();

        configurarBoton(startButton, onStartGame);
        configurarBoton(controlsButton, onShowControls);
        configurarBoton(leaderboardButton, onShowLeaderboard);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy = 1;
        add(subtitle, gbc);

        gbc.gridy = 2;
        add(startButton, gbc);

        gbc.gridy = 3;
        add(controlsButton, gbc);

        gbc.gridy = 4;
        add(leaderboardButton, gbc);
    }

    private JLabel crearTitulo() {
        JLabel title = new JLabel("GALACTIC STORM", SwingConstants.CENTER);
        title.setForeground(COLOR_ACENTO);
        title.setFont(obtenerFuenteTematica(Font.BOLD, 48f));
        title.setBorder(new EmptyBorder(0, 0, 4, 0));
        return title;
    }

    private JLabel crearSubtitulo() {
        JLabel subtitle = new JLabel("PILOTA LA NAVE. DOMINA LA TORMENTA.", SwingConstants.CENTER);
        subtitle.setForeground(new Color(200, 200, 220));
        subtitle.setFont(obtenerFuenteTematica(Font.PLAIN, 18f));
        subtitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        return subtitle;
    }

    private Font obtenerFuenteTematica(int style, float size) {
        String[] favoritas = {"Orbitron", "Eurostile", "Agency FB", "Consolas"};
        String disponible = Arrays.stream(favoritas)
            .filter(this::fuenteDisponible)
            .findFirst()
            .orElse(Font.SANS_SERIF);
        return new Font(disponible, style, Math.round(size));
    }

    private boolean fuenteDisponible(String nombre) {
        return Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
            .anyMatch(f -> f.equalsIgnoreCase(nombre));
    }

    private void configurarBoton(JButton button, Runnable action) {
        button.setFocusPainted(false);
        button.setFont(obtenerFuenteTematica(Font.BOLD, 20f));
        button.setBackground(new Color(20, 24, 46));
        button.setForeground(Color.WHITE);
        button.setBorder(new LineBorder(COLOR_ACENTO, 2, true));
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setPreferredSize(button.getPreferredSize());
        button.addActionListener(e -> {
            if (action != null) {
                if (button == startButton) {
                    button.setEnabled(false);
                }
                action.run();
                if (button != startButton) {
                    button.setEnabled(true);
                }
            }
        });
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(COLOR_ACENTO);
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(20, 24, 46));
                button.setForeground(Color.WHITE);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        if (background != null) {
            g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        } else {
            g2d.setColor(COLOR_FONDO);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        g2d.setPaint(new GradientPaint(0, 0, new Color(0, 0, 0, 140), 0, getHeight(), new Color(10, 0, 30, 220)));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.dispose();
    }

    public void habilitarBotonInicio() {
        startButton.setEnabled(true);
    }
}
