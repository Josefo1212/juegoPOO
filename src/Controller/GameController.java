package Controller;

import Model.Asteroide;
import Model.Nave;
import Model.Proyectil;
import View.GamePanel;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Coordina la lógica principal del juego Galactic Storm.
 * Mantiene el estado del modelo y notifica a la vista cuando hay actualizaciones.
 */
public class GameController {

    private static final int TIMER_DELAY_MS = 16;
    private static final double MIN_ASTEROID_DISTANCE = 120.0;
    private static final double MIN_ASTEROID_SPEED = 0.1;
    private static final double MAX_ASTEROID_SPEED = 1.5;

    private final Nave nave;
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final List<Proyectil> proyectiles = new ArrayList<>();
    private final CollisionManager collisionManager = new CollisionManager();
    private final int worldWidth;
    private final int worldHeight;
    private final Random random = new Random();

    private GamePanel view;
    private InputController inputController;
    private Timer timer;
    private int score;

    public GameController(Nave nave, int worldWidth, int worldHeight) {
        this.nave = nave;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void setView(GamePanel view) {
        this.view = view;
    }

    public void setInputController(InputController inputController) {
        this.inputController = inputController;
    }

    public void iniciar() {
        if (timer != null && timer.isRunning()) {
            return;
        }
        ActionListener tick = e -> {
            if (inputController != null) {
                inputController.aplicarInput();
            }
            actualizarEstado();
            if (view != null) {
                view.repaint();
            }
        };
        timer = new Timer(TIMER_DELAY_MS, tick);
        timer.start();
    }

    public void detener() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void actualizarEstado() {
        if (nave != null && nave.estaActivo()) {
            nave.mover();
            limitarNaveEnPantalla();
        }

        for (Asteroide asteroide : asteroides) {
            if (asteroide.estaActivo()) {
                asteroide.mover();
            }
        }

        for (Proyectil proyectil : proyectiles) {
            if (proyectil.estaActivo()) {
                proyectil.mover();
            }
        }

        score += collisionManager.manejarColisionesProyectilAsteroide(proyectiles, asteroides);
        collisionManager.manejarColisionNaveAsteroide(nave, asteroides);

        proyectiles.removeIf(p -> !p.estaActivo());
        asteroides.removeIf(a -> !a.estaActivo());
    }

    private void limitarNaveEnPantalla() {
        double minX = nave.getAncho() / 2.0;
        double maxX = worldWidth - nave.getAncho() / 2.0;
        double minY = nave.getAlto() / 2.0;
        double maxY = worldHeight - nave.getAlto() / 2.0;

        double nx = Math.max(minX, Math.min(maxX, nave.getX()));
        double ny = Math.max(minY, Math.min(maxY, nave.getY()));

        nave.setX(nx);
        nave.setY(ny);
    }

    public void generarAsteroidesIniciales(int cantidad) {
        int generados = 0;
        while (generados < cantidad) {
            double x = random.nextDouble() * worldWidth;
            double y = random.nextDouble() * worldHeight;
            if (distanciaANave(x, y) < MIN_ASTEROID_DISTANCE) {
                continue;
            }

            double vx = -MAX_ASTEROID_SPEED + random.nextDouble() * (2 * MAX_ASTEROID_SPEED);
            double vy = -MAX_ASTEROID_SPEED + random.nextDouble() * (2 * MAX_ASTEROID_SPEED);
            if (Math.abs(vx) < MIN_ASTEROID_SPEED && Math.abs(vy) < MIN_ASTEROID_SPEED) {
                continue;
            }

            int nivel = 1 + random.nextInt(3);
            int tamano = switch (nivel) {
                case 3 -> 64;
                case 2 -> 44;
                default -> 28;
            };

            asteroides.add(new Asteroide(x, y, vx, vy, tamano, tamano, true, tamano, nivel));
            generados++;
        }
    }

    private double distanciaANave(double x, double y) {
        if (nave == null) {
            return Double.MAX_VALUE;
        }
        double dx = x - nave.getX();
        double dy = y - nave.getY();
        return Math.hypot(dx, dy);
    }

    public void registrarProyectil(Proyectil proyectil) {
        if (proyectil != null) {
            proyectiles.add(proyectil);
        }
    }

    public void disparar() {
        if (nave == null || !nave.estaActivo()) {
            return;
        }
        Proyectil proyectil = nave.disparar();
        registrarProyectil(proyectil);
    }

    public Nave getNave() {
        return nave;
    }

    public List<Asteroide> getAsteroides() {
        return Collections.unmodifiableList(asteroides);
    }

    public List<Proyectil> getProyectiles() {
        return Collections.unmodifiableList(proyectiles);
    }

    public int getScore() {
        return score;
    }

    public int getVidas() {
        return nave != null ? nave.getVidas() : 0;
    }
}

