# Galactic Storm

Galactic Storm es un arcade shooter 2D donde pilotas una nave y debes sobrevivir a oleadas crecientes de asteroides.

## ¿Qué hace el juego?

- **Pantalla inicial interactiva**: botón “Iniciar juego” para arrancar y “Mostrar controles” para recordar el esquema de teclas.
- **Arquitectura MVC**:
  - `Model`: `Nave`, `Asteroide`, `Proyectil` encapsulan atributos y comportamiento.
  - `Controller`: `GameController` (bucle principal + oleadas), `CollisionManager` (colisiones circulares), `InputController` (teclado).
  - `View`: `StartPanel` y `GamePanel` muestran UI, HUD, fondo y botones.
- **Oleadas dinámicas**: el juego comienza con pocos asteroides y, conforme pasan los ticks, se generan más y más hasta un máximo configurable. Cada oleada puede subdividirse cuando destruyes un asteroide grande.
- **Sistema de puntuación y vidas**: HUD en la esquina superior derecha indica puntos acumulados y vidas restantes; perder las tres vidas muestra overlay “GAME OVER”.
- **Botón de reinicio estilizado**: tras un game over aparece un botón “Reiniciar” que limpia el estado, repuebla asteroides y relanza el bucle.
- **Controles responsivos**: rotación suave, aceleración alineada con la orientación del sprite, freno con reversa ligera, disparos desde la punta de la nave y límites del tablero para evitar salir de la pantalla.
- **Recursos visuales incluidos**: `nave.png`, `asteroide.png` y `fondoEStrellas.png` (cargados con `ResourceLoader` que busca dentro de `src/Recursos`).

## ¿Cómo se juega?

1. **Arranque**  
   Ejecuta la clase `App`. Aparecerá la pantalla inicial.
   - Pulsa **“Iniciar Juego”** para entrar al gameplay.
   - Pulsa **“Mostrar Controles”** si necesitas recordatorio rápido (se abre un diálogo).

2. **Controles en el juego**  
   - `W`: Acelerar hacia la dirección actual de la nave.
   - `S`: Frenar y aplicar una ligera reversa (útil para maniobrar).
   - `A` / `D`: Rotar la nave a la izquierda/derecha.
   - `Espacio`: Disparar proyectiles (hay un pequeño cooldown).
   - Mouse no se utiliza; todo es con teclado.

3. **Objetivo**  
   - Destruye asteroides para sumar puntos (los grandes valen más).
   - Evita choques: al perder una vida la nave reaparece hasta agotar las 3 disponibles.
   - Las oleadas se vuelven más densas con el tiempo; elimina amenazas rápido para evitar saturaciones.

4. **Game Over y Reinicio**  
   - Si pierdes las 3 vidas se muestra un overlay con “GAME OVER” y el botón **“Reiniciar”**.
   - Pulsa el botón para reiniciar la partida sin volver a la pantalla inicial; reapareces con 3 vidas y la progresión de oleadas se reinicia desde pocos asteroides.
