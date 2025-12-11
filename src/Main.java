import Model.Nave;
import Model.Asteroide;
import View.GamePanel;
import Controller.InputController;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Crear la nave inicial en el centro de la pantalla
            Nave nave = new Nave(400, 300, 0, 0, 40, 20, true);

            // Crear el panel de juego y la ventana
            GamePanel gamePanel = new GamePanel(nave);

            // Añadir algunos asteroides de prueba
            gamePanel.getAsteroides().add(new Asteroide(100, 100, 1, 0.5, 40, 40, true, 40, 3));
            gamePanel.getAsteroides().add(new Asteroide(600, 400, -1, -0.5, 30, 30, true, 30, 2));

            JFrame frame = new JFrame("Galactic Storm");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);

            // Crear y registrar el controlador de entrada
            InputController inputController = new InputController(nave, gamePanel);
            gamePanel.addKeyListener(inputController);

            frame.setVisible(true);
            gamePanel.requestFocusInWindow();

            // Bucle de juego sencillo con Timer (aprox. 60 FPS)
            int delay = 16; // ms
            Timer timer = new Timer(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    inputController.aplicarInput();
                    gamePanel.actualizarJuego();
                    gamePanel.repaint();
                }
            });
            timer.start();
        });
    }
}
