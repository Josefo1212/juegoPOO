package Model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

import Controller.ResourceLoader;
import Model.CollisionPolygon;
import Model.CollisionShape;
import Model.Vector2;

public class Nave extends EntidadJuego {
    private double rotacion; // ángulo en grados
    private int vidas;
    private double energia;
    private static BufferedImage imagen = ResourceLoader.cargarImagen("nave.png");

    public Nave(double x, double y, double vx, double vy, int ancho, int alto, boolean activo) {
        super(x, y, vx, vy, ancho, alto, activo);
        this.rotacion = 0;
        this.vidas = 3;
        this.energia = 100.0;
    }

    @Override
    public void mover() {
        double friccion = 0.99;
        vx *= friccion;
        vy *= friccion;
        super.mover();
    }

    public void rotarIzquierda(double delta) {
        rotacion -= delta;
    }

    public void rotarDerecha(double delta) {
        rotacion += delta;
    }

    /**
     * Acelera la nave en la dirección a la que apunta la imagen.
     * Si la imagen está dibujada apuntando hacia arriba, aplicamos un offset de -90 grados.
     */
    public void acelerar(double fuerza) {
        double rad = Math.toRadians(rotacion - 90); // offset para alinear imagen hacia arriba
        vx += Math.cos(rad) * fuerza;
        vy += Math.sin(rad) * fuerza;
    }

    /**
     * Frena la nave multiplicando la velocidad por un factor (0 < factor < 1).
     */
    public void frenar(double factor) {
        vx *= factor;
        vy *= factor;
    }

    public Proyectil disparar() {
        double rad = Math.toRadians(rotacion - 90); // mismo offset que acelerar
        double velocidadProyectil = 10.0;
        double pvx = Math.cos(rad) * velocidadProyectil;
        double pvy = Math.sin(rad) * velocidadProyectil;
        int anchoProyectil = 4;
        int altoProyectil = 4;
        int tiempoVida = 60; // ticks de juego

        // Punto de disparo: punta de la nave en dirección de rotación (con offset)
        double offset = ancho / 2.0 + 4;
        double px = x + Math.cos(rad) * offset;
        double py = y + Math.sin(rad) * offset;

        return new Proyectil(px, py, pvx, pvy, anchoProyectil, altoProyectil, true, tiempoVida, rotacion);
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform old = g2.getTransform();
        g2.translate(x, y);
        g2.rotate(Math.toRadians(rotacion)); // rotacion visual
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

    public void reiniciar(double nuevoX, double nuevoY) {
        this.x = nuevoX;
        this.y = nuevoY;
        this.vx = 0;
        this.vy = 0;
        this.rotacion = 0;
        this.vidas = 3;
        this.energia = 100.0;
        this.activo = true;
    }

    @Override
    public CollisionShape getCollisionShape() {
        double rad = Math.toRadians(rotacion - 90);
        double halfW = ancho / 2.0;
        double halfH = alto / 2.0;
        Vector2[] base = new Vector2[] {
            new Vector2(0, -halfH),
            new Vector2(halfW * 0.8, halfH),
            new Vector2(-halfW * 0.8, halfH)
        };
        Vector2[] rotated = new Vector2[base.length];
        for (int i = 0; i < base.length; i++) {
            Vector2 v = base[i].rotate(rad).add(new Vector2(x, y));
            rotated[i] = v;
        }
        return new CollisionPolygon(rotated);
    }
}
