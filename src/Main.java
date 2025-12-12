import Model.Nave;
import View.GamePanel;
import Controller.InputController;
import Controller.GameController;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    private static final int ASTEROIDES_INICIALES = 25;
    private static final int MUNDO_ANCHO = 800;
    private static final int MUNDO_ALTO = 600;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Nave nave = new Nave(MUNDO_ANCHO / 2.0, MUNDO_ALTO / 2.0, 0, 0, 40, 20, true);

            GameController controller = new GameController(nave, MUNDO_ANCHO, MUNDO_ALTO);
            GamePanel gamePanel = new GamePanel(controller);
            controller.setView(gamePanel);

            InputController inputController = new InputController(controller);
            controller.setInputController(inputController);
            gamePanel.addKeyListener(inputController);

            controller.generarAsteroidesIniciales(ASTEROIDES_INICIALES);

            JFrame frame = new JFrame("Galactic Storm");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            gamePanel.requestFocusInWindow();
            controller.iniciar();
        });
    }
}
