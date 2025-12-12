package View;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Pantalla inicial que se muestra antes de arrancar Galactic Storm.
 */
public class StartPanel extends JPanel {

    private final JButton startButton = new JButton("Iniciar Juego");
    private final JButton controlsButton = new JButton("Mostrar Controles");

    public StartPanel(Runnable onStartGame, Runnable onShowControls) {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Galactic Storm", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Consolas", Font.BOLD, 42));

        configurarBoton(startButton, onStartGame);
        configurarBoton(controlsButton, onShowControls);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy = 1;
        add(startButton, gbc);

        gbc.gridy = 2;
        add(controlsButton, gbc);
    }

    private void configurarBoton(JButton button, Runnable action) {
        button.setFocusPainted(false);
        button.setFont(new Font("Consolas", Font.BOLD, 20));
        button.setBackground(new Color(30, 30, 30));
        button.setForeground(Color.WHITE);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> {
            if (action != null) {
                button.setEnabled(false);
                action.run();
            }
        });
    }

    public void habilitarBotonInicio() {
        startButton.setEnabled(true);
    }
}

