package Model;

import Model.CollisionShape;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Clase base para todas las entidades del juego Galactic Storm.
 */
public abstract class EntidadJuego {
    // Atributos comunes
    protected double x, y;          // Posición (centro)
    protected double vx, vy;        // Velocidad
    protected int ancho, alto;      // Dimensiones para dibujo/colisión
    protected boolean activo;       // Si la entidad está activa en el juego

    public EntidadJuego(double x, double y, double vx, double vy,
                        int ancho, int alto, boolean activo) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.ancho = ancho;
        this.alto = alto;
        this.activo = activo;
    }

    /**
     * Actualiza la posición usando la velocidad actual.
     */
    public void mover() {
        x += vx;
        y += vy;
    }

    /**
     * Dibuja la entidad en pantalla.
     */
    public abstract void dibujar(Graphics g);

    /**
     * Devuelve el rectángulo de colisión centrado en (x,y).
     */
    public Rectangle getBounds() {
        return new Rectangle((int) (x - ancho / 2.0), (int) (y - alto / 2.0), ancho, alto);
    }

    /**
     * Radio de colisión por defecto (basado en el tamaño mínimo).
     */
    public double getRadioColision() {
        return Math.min(ancho, alto) / 2.0;
    }

    public CollisionShape getCollisionShape() {
        return null;
    }

    /**
     * Colisión circular simple entre dos entidades.
     */
    public boolean colisionaCon(EntidadJuego otra) {
        if (otra == null) return false;
        CollisionShape a = this.getCollisionShape();
        CollisionShape b = otra.getCollisionShape();
        if (a != null && b != null) {
            return a.intersects(b);
        }
        double dx = this.x - otra.x;
        double dy = this.y - otra.y;
        double dist2 = dx * dx + dy * dy;
        double r = this.getRadioColision() + otra.getRadioColision();
        return dist2 <= r * r;
    }

    public boolean estaActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Getters básicos
    public double getX() { return x; }
    public double getY() { return y; }
    public double getVx() { return vx; }
    public double getVy() { return vy; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }

    // Setters de posición (útiles para limitar dentro de la pantalla)
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
