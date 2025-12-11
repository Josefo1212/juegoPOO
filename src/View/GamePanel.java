package View;

import Model.Nave;
import Model.Asteroide;
import Model.Proyectil;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class GamePanel extends JPanel {
    private Nave nave;
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final List<Proyectil> proyectiles = new ArrayList<>();

    public GamePanel(Nave naveInicial) {
        this.nave = naveInicial;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    public Nave getNave() {
        return nave;
    }

    public void setNave(Nave nave) {
        this.nave = nave;
    }

    public List<Asteroide> getAsteroides() {
        return asteroides;
    }

    public List<Proyectil> getProyectiles() {
        return proyectiles;
    }

    public void actualizarJuego() {
        if (nave != null && nave.estaActivo()) {
            nave.mover();
            limitarNaveDentroPantalla();
        }

        for (Asteroide a : asteroides) {
            if (a.estaActivo()) {
                a.mover();
            }
        }

        for (Proyectil p : proyectiles) {
            if (p.estaActivo()) {
                p.mover();
            }
        }

        // Colisiones: proyectil vs asteroide
        List<Asteroide> nuevos = new ArrayList<>();
        for (Proyectil p : new ArrayList<>(proyectiles)) {
            if (!p.estaActivo()) continue;
            for (Asteroide a : new ArrayList<>(asteroides)) {
                if (!a.estaActivo()) continue;
                if (p.colisionaCon(a)) {
                    p.setActivo(false);
                    Asteroide[] frag = a.recibirImpacto();
                    Collections.addAll(nuevos, frag);
                    break;
                }
            }
        }
        asteroides.addAll(nuevos);

        // Colisiones: nave vs asteroide
        if (nave != null && nave.estaActivo()) {
            for (Asteroide a : new ArrayList<>(asteroides)) {
                if (!a.estaActivo()) continue;
                if (nave.colisionaCon(a)) {
                    nave.perderVida();
                    Asteroide[] frag = a.recibirImpacto();
                    Collections.addAll(asteroides, frag);
                    if (nave.getVidas() <= 0) {
                        nave.setActivo(false);
                    }
                    break;
                }
            }
        }

        // Limpiar entidades inactivas
        proyectiles.removeIf(p -> !p.estaActivo());
        asteroides.removeIf(a -> !a.estaActivo());
    }

    private void limitarNaveDentroPantalla() {
        if (nave == null) return;
        int anchoPanel = getWidth();
        int altoPanel = getHeight();

        double x = nave.getX();
        double y = nave.getY();
        int anchoNave = nave.getAncho();
        int altoNave = nave.getAlto();

        double minX = anchoNave / 2.0;
        double maxX = anchoPanel - anchoNave / 2.0;
        double minY = altoNave / 2.0;
        double maxY = altoPanel - altoNave / 2.0;

        double nx = Math.max(minX, Math.min(maxX, x));
        double ny = Math.max(minY, Math.min(maxY, y));

        nave.setX(nx);
        nave.setY(ny);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (nave != null && nave.estaActivo()) {
            g.setColor(Color.CYAN);
            nave.dibujar(g);
        }

        g.setColor(Color.GRAY);
        for (Asteroide a : asteroides) {
            if (a.estaActivo()) {
                a.dibujar(g);
            }
        }

        g.setColor(Color.YELLOW);
        for (Proyectil p : proyectiles) {
            if (p.estaActivo()) {
                p.dibujar(g);
            }
        }
    }
}
