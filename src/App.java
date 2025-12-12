import Controller.GameController;
import Controller.InputController;
import Model.Nave;
import View.GameWindow;

import javax.swing.SwingUtilities;

public class App {

    private static final int MUNDO_ANCHO = 800;
    private static final int MUNDO_ALTO = 600;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Nave nave = new Nave(MUNDO_ANCHO / 2.0, MUNDO_ALTO / 2.0, 0, 0, 40, 20, true);
            GameController controller = new GameController(nave, MUNDO_ANCHO, MUNDO_ALTO);
            InputController inputController = new InputController(controller);
            controller.setInputController(inputController);
            GameWindow window = new GameWindow(controller, inputController, MUNDO_ANCHO, MUNDO_ALTO);

            window.requestFocusInWindow();
        });
    }
}
