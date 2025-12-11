package Model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.IOException;
import java.awt.Rectangle;

public class Nave extends EntidadJuego {
    private double rotacion; // ángulo en grados
    private int vidas;
    private double energia;
    private static BufferedImage imagen;

    static {
        // Cargar la imagen usando try-with-resources desde rutas posibles
        try (InputStream is1 = Nave.class.getResourceAsStream("/Recursos/nave.png")) {
            if (is1 != null) {
                imagen = ImageIO.read(is1);
            } else {
                try (InputStream is2 = Nave.class.getResourceAsStream("/nave.png")) {
                    if (is2 != null) {
                        imagen = ImageIO.read(is2);
                    } else {
                        imagen = null;
                    }
                }
            }
        } catch (IOException e) {
            imagen = null;
        }
    }

    public Nave(double x, double y, double vx, double vy, int ancho, int alto, boolean activo) {
        super(x, y, vx, vy, ancho, alto, activo);
        this.rotacion = 0;
        this.vidas = 3;
        this.energia = 100.0;
    }

    public void rotarIzquierda(double delta) {
        rotacion -= delta;
    }

    public void rotarDerecha(double delta) {
        rotacion += delta;
    }

    public void acelerar(double fuerza) {
        double rad = Math.toRadians(rotacion);
        vx += Math.cos(rad) * fuerza;
        vy += Math.sin(rad) * fuerza;
    }

    public Proyectil disparar() {
        double rad = Math.toRadians(rotacion);
        double velocidadProyectil = 10.0;
        double pvx = Math.cos(rad) * velocidadProyectil;
        double pvy = Math.sin(rad) * velocidadProyectil;
        int anchoProyectil = 4;
        int altoProyectil = 4;
        int tiempoVida = 60; // por ejemplo, 60 "ticks" de juego

        // Disparar desde el centro de la nave
        double px = x;
        double py = y;

        return new Proyectil(px, py, pvx, pvy, anchoProyectil, altoProyectil, true, tiempoVida, rotacion);
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform old = g2.getTransform();
        // trasladar al centro (x,y), rotar, y dibujar la imagen centrada
        g2.translate(x, y);
        g2.rotate(Math.toRadians(rotacion));
        if (imagen != null) {
            g2.drawImage(imagen, -ancho/2, -alto/2, ancho, alto, null);
        } else {
            g2.fillRect(-ancho/2, -alto/2, ancho, alto);
        }
        g2.setTransform(old);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) (x - ancho / 2.0), (int) (y - alto / 2.0), ancho, alto);
    }

    public double getRotacion() { return rotacion; }
    public int getVidas() { return vidas; }
    public double getEnergia() { return energia; }
    public void perderVida() { vidas = Math.max(0, vidas - 1); }
}
