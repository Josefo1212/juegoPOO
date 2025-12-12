package View;

import Controller.GameController;
import Controller.InputController;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.CardLayout;

public class GameWindow extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final GamePanel gamePanel;
    private final StartPanel startPanel;

    public GameWindow(GameController controller, InputController inputController, int width, int height) {
        super("Galactic Storm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(cardLayout);

        gamePanel = new GamePanel(controller);
        controller.setView(gamePanel);
        gamePanel.addKeyListener(inputController);

        startPanel = new StartPanel(
            () -> {
                mostrarJuego();
                controller.generarAsteroidesIniciales(GameController.ASTEROIDES_INICIALES);
                controller.iniciar();
                gamePanel.requestFocusInWindow();
            },
            () -> JOptionPane.showMessageDialog(this,
                "Controles:\nW - Acelerar\nS - Frenar/Reversa\nA/D - Rotar\nEspacio - Disparar",
                "Controles", JOptionPane.INFORMATION_MESSAGE)
        );

        add(startPanel, "start");
        add(gamePanel, "game");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void mostrarJuego() {
        cardLayout.show(getContentPane(), "game");
    }

    public void mostrarInicio() {
        cardLayout.show(getContentPane(), "start");
        startPanel.habilitarBotonInicio();
    }
}

