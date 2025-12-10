package Model;

import java.awt.*;

public abstract class Entidad {
    private double x, y;
    private double vx, vy;
    private int ancho, alto;

    public Entidad(double x, double y, double vx, double vy) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.ancho = ancho; // Valor predeterminado
        this.alto = alto;  // Valor predeterminado
    }

    public void mover(){
        this.x += this.vx;
        this.y += this.vy;
    }

    public abstract void dibuhar(Graphics g);

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, ancho, alto);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }



}
