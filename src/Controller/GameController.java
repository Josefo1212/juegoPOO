package Controller;

import Model.Asteroide;
import Model.Nave;
import Model.Proyectil;
import View.GamePanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Coordina la lógica principal del juego Galactic Storm.
 * Mantiene el estado del modelo y notifica a la vista cuando hay actualizaciones.
 */
public class GameController {

    public static final int ASTEROIDES_INICIALES = 8;
    private static final int TIMER_DELAY_MS = 16;
    private static final double MIN_ASTEROID_DISTANCE = 120.0;
    private static final double MIN_ASTEROID_SPEED = 0.1;
    private static final double MAX_ASTEROID_SPEED = 1.5;
    private static final int SPAWN_INTERVAL_BASE = 300;
    private static final int SPAWN_INTERVAL_MIN = 120;
    private static final int SPAWN_WAVE_BASE = 3;
    private static final int SPAWN_WAVE_MAX = 15;
    private static final int MAX_ASTEROIDES_ACTIVOS = 80;

    private final Nave nave;
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final List<Proyectil> proyectiles = new ArrayList<>();
    private final CollisionManager collisionManager = new CollisionManager();
    private final int worldWidth;
    private final int worldHeight;
    private final Random random = new Random();

    private GamePanel view;
    private InputController inputController;
    private ScheduledExecutorService executor;
    private int score;
    private boolean gameOver;
    private Runnable onGameOver;
    private final LeaderboardManager leaderboardManager;
    private int spawnTickCounter = 0;
    private int spawnIntervalTicks = SPAWN_INTERVAL_BASE;
    private int waveSize = SPAWN_WAVE_BASE;

    public GameController(Nave nave, int worldWidth, int worldHeight, LeaderboardManager leaderboardManager) {
        this.nave = nave;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.leaderboardManager = leaderboardManager;
    }

    public void setView(GamePanel view) {
        this.view = view;
    }

    public void setInputController(InputController inputController) {
        this.inputController = inputController;
    }

    public void iniciar() {
        if (executor != null && !executor.isShutdown()) {
            return;
        }
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "GameLoop");
            t.setDaemon(true);
            return t;
        });
        executor.scheduleAtFixedRate(this::tick, 0, TIMER_DELAY_MS, TimeUnit.MILLISECONDS);
    }

    private void tick() {
        if (inputController != null) {
            inputController.aplicarInput();
        }
        actualizarEstado();
        if (view != null) {
            javax.swing.SwingUtilities.invokeLater(view::repaint);
        }
    }

    public void setOnGameOver(Runnable onGameOver) {
        this.onGameOver = onGameOver;
    }

    public void detener() {
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }

    private void actualizarEstado() {
        if (nave != null && nave.estaActivo()) {
            nave.mover();
            limitarNaveEnPantalla();
        }
        if (!nave.estaActivo()) {
            if (!gameOver) {
                gameOver = true;
                registrarPuntaje();
                if (onGameOver != null) {
                    onGameOver.run();
                }
            }
            return;
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
        manejarSpawns();
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
        generarAsteroides(cantidad);
    }

    private void generarAsteroides(int cantidad) {
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

    private void manejarSpawns() {
        if (gameOver) return;
        spawnTickCounter++;
        if (spawnTickCounter < spawnIntervalTicks) {
            return;
        }
        spawnTickCounter = 0;
        int activos = (int) asteroides.stream().filter(Asteroide::estaActivo).count();
        int disponibles = Math.max(0, MAX_ASTEROIDES_ACTIVOS - activos);
        int cantidad = Math.min(waveSize, disponibles);
        if (cantidad > 0) {
            generarAsteroides(cantidad);
        }
        if (waveSize < SPAWN_WAVE_MAX) {
            waveSize++;
        }
        if (spawnIntervalTicks > SPAWN_INTERVAL_MIN) {
            spawnIntervalTicks = Math.max(SPAWN_INTERVAL_MIN, spawnIntervalTicks - 10);
        }
    }

    public boolean estaGameOver() {
        return gameOver;
    }

    public void reiniciar(int cantidadAsteroides) {
        asteroides.clear();
        proyectiles.clear();
        score = 0;
        gameOver = false;
        spawnTickCounter = 0;
        spawnIntervalTicks = SPAWN_INTERVAL_BASE;
        waveSize = SPAWN_WAVE_BASE;
        if (nave != null) {
            nave.reiniciar(worldWidth / 2.0, worldHeight / 2.0);
        }
        generarAsteroidesIniciales(cantidadAsteroides);
    }

    private void registrarPuntaje() {
        if (leaderboardManager != null) {
            leaderboardManager.registrarPuntaje("Jugador", score);
        }
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

    public List<LeaderboardManager.ScoreEntry> getLeaderboard() {
        return leaderboardManager != null ? leaderboardManager.obtenerTop() : Collections.emptyList();
    }

    public int getVidas() {
        return nave != null ? nave.getVidas() : 0;
    }
}
