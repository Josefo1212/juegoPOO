package Model;

import java.awt.Graphics;

/**
 * Representa un proyectil disparado por la nave en Galactic Storm.
 */
public class Proyectil extends EntidadJuego {

    private int tiempoVida;   // cuántos "ticks" de juego dura
    private double direccion; // ángulo en grados o radianes

    public Proyectil(double x, double y, double vx, double vy,
                     int ancho, int alto, boolean activo,
                     int tiempoVida, double direccion) {
        super(x, y, vx, vy, ancho, alto, activo);
        this.tiempoVida = tiempoVida;
        this.direccion = direccion;
    }

    @Override
    public void mover() {
        super.mover();
        tiempoVida--;
        if (tiempoVida <= 0) {
            activo = false;
        }
    }

    public boolean estaActivo() {
        return activo && tiempoVida > 0;
    }

    @Override
    public void dibujar(Graphics g) {
        // Dibujo sencillo del proyectil: un pequeño ovalo centrado en (x,y).
        int drawX = (int) (x - ancho / 2.0);
        int drawY = (int) (y - alto / 2.0);
        g.fillOval(drawX, drawY, ancho, alto);
    }
}
