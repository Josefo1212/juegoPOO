package Controller;

import Model.Nave;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Controlador de entrada por teclado para Galactic Storm.
 *
 * Esquema de teclas:
 *  - A: rotar izquierda
 *  - D: rotar derecha
 *  - W: acelerar
 *  - S: frenar (reducir velocidad)
 *  - ESPACIO: disparar
 */
public class InputController extends KeyAdapter {

    private final GameController controller;

    private boolean izquierda;
    private boolean derecha;
    private boolean acelerar;
    private boolean frenar;
    private boolean disparar;

    // Control de cadencia de disparo (en ticks del Timer)
    private int cooldownDisparo = 0;
    private static final int COOLDOWN_MAX = 10; // ~10 frames entre disparos

    public InputController(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_A -> izquierda = true;
            case KeyEvent.VK_D -> derecha = true;
            case KeyEvent.VK_W -> acelerar = true;
            case KeyEvent.VK_S -> frenar = true;
            case KeyEvent.VK_SPACE -> disparar = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_A -> izquierda = false;
            case KeyEvent.VK_D -> derecha = false;
            case KeyEvent.VK_W -> acelerar = false;
            case KeyEvent.VK_S -> frenar = false;
            case KeyEvent.VK_SPACE -> disparar = false;
        }
    }

    /**
     * Llamar en cada tick del juego para aplicar el estado actual del teclado.
     */
    public void aplicarInput() {
        Nave nave = controller != null ? controller.getNave() : null;
        if (nave == null || !nave.estaActivo()) return;

        double deltaRotacion = 5.0;        // grados por tick
        double fuerzaAceleracion = 0.2;   // aceleración por tick
        double fuerzaFreno = 0.90;        // factor de frenado multiplicativo

        if (izquierda) {
            nave.rotarIzquierda(deltaRotacion);
        }
        if (derecha) {
            nave.rotarDerecha(deltaRotacion);
        }
        if (acelerar) {
            nave.acelerar(fuerzaAceleracion);
        }
        if (frenar) {
            nave.frenar(fuerzaFreno);
        }

        if (cooldownDisparo > 0) {
            cooldownDisparo--;
        }

        if (disparar && cooldownDisparo == 0) {
            controller.disparar();
            cooldownDisparo = COOLDOWN_MAX;
        }
    }
}
