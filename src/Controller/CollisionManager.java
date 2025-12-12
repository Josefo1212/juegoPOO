package Controller;

import Model.Asteroide;
import Model.Nave;
import Model.Proyectil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsula la lógica de colisiones entre entidades del juego.
 */
public class CollisionManager {

    private static final int PUNTOS_NIVEL_GRANDE = 30;
    private static final int PUNTOS_NIVEL_MEDIANO = 20;
    private static final int PUNTOS_NIVEL_PEQUENO = 10;

    public int manejarColisionesProyectilAsteroide(List<Proyectil> proyectiles, List<Asteroide> asteroides) {
        int puntosGanados = 0;
        List<Asteroide> nuevos = new ArrayList<>();
        for (Proyectil proyectil : proyectiles) {
            if (!proyectil.estaActivo()) continue;
            for (Asteroide asteroide : asteroides) {
                if (!asteroide.estaActivo()) continue;
                if (proyectil.colisionaCon(asteroide)) {
                    proyectil.setActivo(false);
                    Asteroide[] fragmentos = asteroide.recibirImpacto();
                    Collections.addAll(nuevos, fragmentos);
                    puntosGanados += puntajePorNivel(asteroide.getNivel());
                    break;
                }
            }
        }
        asteroides.addAll(nuevos);
        return puntosGanados;
    }

    public void manejarColisionNaveAsteroide(Nave nave, List<Asteroide> asteroides) {
        if (nave == null || !nave.estaActivo()) {
            return;
        }
        List<Asteroide> nuevos = new ArrayList<>();
        for (Asteroide asteroide : asteroides) {
            if (!asteroide.estaActivo()) continue;
            if (nave.colisionaCon(asteroide)) {
                nave.perderVida();
                Asteroide[] fragmentos = asteroide.recibirImpacto();
                Collections.addAll(nuevos, fragmentos);
                if (nave.getVidas() <= 0) {
                    nave.setActivo(false);
                }
                break;
            }
        }
        asteroides.addAll(nuevos);
    }

    private int puntajePorNivel(int nivel) {
        return switch (nivel) {
            case 3 -> PUNTOS_NIVEL_GRANDE;
            case 2 -> PUNTOS_NIVEL_MEDIANO;
            default -> PUNTOS_NIVEL_PEQUENO;
        };
    }
}

