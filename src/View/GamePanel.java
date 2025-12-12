package View;

import Controller.GameController;
import Controller.ResourceLoader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private final GameController controller;
    private final BufferedImage fondo = ResourceLoader.cargarImagen("fondoEStrellas.png");
    private final JButton restartButton = new JButton("Reiniciar");
    private final int padding = 10;

    public GamePanel(GameController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(null);
        configurarRestartButton();
    }

    private void configurarRestartButton() {
        restartButton.setVisible(false);
        restartButton.setFocusPainted(false);
        restartButton.setFont(new Font("Consolas", Font.BOLD, 18));
        restartButton.setForeground(Color.WHITE);
        restartButton.setBackground(new Color(25, 25, 25, 220));
        restartButton.setOpaque(true);
        restartButton.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 180), 2));
        restartButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        restartButton.setToolTipText("Reiniciar la partida");
        restartButton.addActionListener(e -> {
            controller.reiniciar(GameController.ASTEROIDES_INICIALES);
            controller.iniciar();
            restartButton.setVisible(false);
            requestFocusInWindow();
        });
        add(restartButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (fondo != null) {
            g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), null);
        }

        controller.getAsteroides().forEach(asteroide -> {
            if (asteroide.estaActivo()) {
                g2.setColor(Color.GRAY);
                asteroide.dibujar(g2);
            }
        });

        controller.getProyectiles().forEach(proyectil -> {
            if (proyectil.estaActivo()) {
                g2.setColor(Color.YELLOW);
                proyectil.dibujar(g2);
            }
        });

        if (controller.getNave() != null && controller.getNave().estaActivo()) {
            g2.setColor(Color.CYAN);
            controller.getNave().dibujar(g2);
        }

        g2.setFont(new Font("Consolas", Font.BOLD, 16));
        String hud = "Puntos: " + controller.getScore() + "  Vidas: " + controller.getVidas();
        int textWidth = g2.getFontMetrics().stringWidth(hud);
        int x = getWidth() - textWidth - padding;
        int y = padding + g2.getFontMetrics().getAscent();
        g2.setColor(Color.WHITE);
        g2.drawString(hud, x, y);

        if (controller.estaGameOver()) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setFont(new Font("Consolas", Font.BOLD, 36));
            String msg = "GAME OVER";
            int msgWidth = g2.getFontMetrics().stringWidth(msg);
            g2.setColor(Color.RED);
            g2.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2 - 20);
            posicionarBoton();
        } else {
            restartButton.setVisible(false);
        }
        if (controller.estaGameOver()) {
            controller.detener();
        }
    }

    private void posicionarBoton() {
        int botonAncho = 140;
        int botonAlto = 40;
        int x = (getWidth() - botonAncho) / 2;
        int y = getHeight() / 2 + 10;
        restartButton.setBounds(x, y, botonAncho, botonAlto);
        restartButton.setVisible(true);
    }
}
