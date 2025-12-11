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

public class GamePanel extends JPanel {
    private Nave nave;
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final List<Proyectil> proyectiles = new ArrayList<>();

    public GamePanel(Nave naveInicial) {
        this.nave = naveInicial;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
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
                    // desactiva proyectil
                    p.setActivo(false);
                    // asteroide recibe impacto y puede generar fragmentos
                    Asteroide[] frag = a.recibirImpacto();
                    for (Asteroide f : frag) {
                        nuevos.add(f);
                    }
                    break; // proyectil ya impactó
                }
            }
        }
        // Añadir fragmentos generados
        asteroides.addAll(nuevos);

        // Colisiones: nave vs asteroide
        if (nave != null && nave.estaActivo()) {
            for (Asteroide a : new ArrayList<>(asteroides)) {
                if (!a.estaActivo()) continue;
                if (nave.colisionaCon(a)) {
                    // la nave pierde vida y el asteroide se rompe
                    nave.perderVida();
                    Asteroide[] frag = a.recibirImpacto();
                    for (Asteroide f : frag) asteroides.add(f);
                    if (nave.getVidas() <= 0) {
                        nave.setActivo(false);
                    }
                    break; // manejar una colisión por tick
                }
            }
        }

        // Limpiar entidades inactivas
        proyectiles.removeIf(p -> !p.estaActivo());
        asteroides.removeIf(a -> !a.estaActivo());
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
