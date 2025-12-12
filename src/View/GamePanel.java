package View;

import Controller.GameController;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.RenderingHints;

public class GamePanel extends JPanel {
    private final GameController controller;

    public GamePanel(GameController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    public GameController getController() {
        return controller;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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
        int x = getWidth() - textWidth - 10;
        int y = 10 + g2.getFontMetrics().getAscent();
        g2.setColor(Color.WHITE);
        g2.drawString(hud, x, y);
    }
}
