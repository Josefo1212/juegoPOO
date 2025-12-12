package View;

import Controller.LeaderboardManager;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;

public class LeaderboardDialog extends JDialog {

    public LeaderboardDialog(JFrame owner, List<LeaderboardManager.ScoreEntry> entries) {
        super(owner, "Tabla de Líderes", true);
        setLayout(new BorderLayout());

        String[] columns = {"Posición", "Jugador", "Puntaje"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        int position = 1;
        for (LeaderboardManager.ScoreEntry entry : entries) {
            model.addRow(new Object[]{position++, entry.nombre(), entry.puntaje()});
        }

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setSize(300, 300);
        setLocationRelativeTo(owner);
    }
}

