package Controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderboardManager {

    private static final int MAX_ENTRIES = 10;
    private final Path storagePath;
    private final List<ScoreEntry> entries = new ArrayList<>();

    public LeaderboardManager(Path storagePath) {
        this.storagePath = storagePath;
        cargar();
    }

    public synchronized void registrarPuntaje(String nombre, int puntaje) {
        entries.add(new ScoreEntry(nombre, puntaje));
        entries.sort(Comparator.comparingInt(ScoreEntry::puntaje).reversed());
        if (entries.size() > MAX_ENTRIES) {
            entries.subList(MAX_ENTRIES, entries.size()).clear();
        }
        guardar();
    }

    public synchronized List<ScoreEntry> obtenerTop() {
        return new ArrayList<>(entries);
    }

    private void cargar() {
        if (!Files.exists(storagePath)) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(storagePath, StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String nombre = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    entries.add(new ScoreEntry(nombre, score));
                }
            }
            entries.sort(Comparator.comparingInt(ScoreEntry::puntaje).reversed());
        } catch (IOException | NumberFormatException ex) {
            entries.clear();
        }
    }

    private void guardar() {
        try {
            Files.createDirectories(storagePath.getParent());
            String data = entries.stream()
                .map(e -> e.nombre() + "," + e.puntaje())
                .collect(Collectors.joining(System.lineSeparator()));
            Files.writeString(storagePath, data, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }

    public record ScoreEntry(String nombre, int puntaje) {}
}

