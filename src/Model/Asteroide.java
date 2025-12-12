package Model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import Controller.ResourceLoader;
import Model.CollisionCircle;
import Model.CollisionShape;
import Model.Vector2;

/**
 * Representa un asteroide en Galactic Storm.
 */
public class Asteroide extends EntidadJuego {

    private int tamano; // por ejemplo, radio o escala
    private int nivel;  // 3 = grande, 2 = mediano, 1 = pequeño
    private static final Random rnd = new Random();
    private static final BufferedImage imagen = ResourceLoader.cargarImagen("asteroide.png");

    public Asteroide(double x, double y, double vx, double vy,
                     int ancho, int alto, boolean activo,
                     int tamano, int nivel) {
        super(x, y, vx, vy, ancho, alto, activo);
        this.tamano = tamano;
        this.nivel = nivel;
    }

    @Override
    public void mover() {
        super.mover();
        // aquí podrías añadir lógica extra de movimiento
    }

    public Asteroide[] dividir() {
        if (nivel <= 1) return new Asteroide[0];
        int nuevoNivel = nivel - 1;
        int nuevoTamano = Math.max(8, tamano / 2);
        Asteroide[] frag = new Asteroide[2];
        for (int i = 0; i < 2; i++) {
            double nvx = vx + (rnd.nextDouble() - 0.5) * 3;
            double nvy = vy + (rnd.nextDouble() - 0.5) * 3;
            double nx = x + (i==0 ? -nuevoTamano : nuevoTamano);
            double ny = y + (i==0 ? -nuevoTamano : nuevoTamano);
            frag[i] = new Asteroide(nx, ny, nvx, nvy, nuevoTamano, nuevoTamano, true, nuevoTamano, nuevoNivel);
        }
        return frag;
    }

    public Asteroide[] recibirImpacto() {
        Asteroide[] res = dividir();
        this.activo = false;
        return res;
    }

    @Override
    public void dibujar(Graphics g) {
        if (imagen != null) {
            Graphics2D g2 = (Graphics2D) g;
            int drawX = (int) (x - ancho / 2.0);
            int drawY = (int) (y - alto / 2.0);
            g2.drawImage(imagen, drawX, drawY, ancho, alto, null);
        } else {
            int drawX = (int) (x - ancho / 2.0);
            int drawY = (int) (y - alto / 2.0);
            g.fillOval(drawX, drawY, ancho, alto);
        }
    }

    public int getTamano() { return tamano; }
    public int getNivel() { return nivel; }

    @Override
    public CollisionShape getCollisionShape() {
        return new CollisionCircle(new Vector2(x, y), Math.max(ancho, alto) / 2.2);
    }
}
