package Controller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utilidad centralizada para cargar recursos (imágenes, etc.).
 */
public class ResourceLoader {

    /**
     * Carga una imagen buscando primero en /Recursos, luego en la raíz del classpath.
     * @param nombreArchivo Nombre del archivo de imagen, por ejemplo "nave.png".
     * @return BufferedImage cargada o null si no se encuentra o falla.
     */
    public static BufferedImage cargarImagen(String nombreArchivo) {
        // Intentar en /Recursos
        String ruta1 = "/Recursos/" + nombreArchivo;
        String ruta2 = "/" + nombreArchivo;

        BufferedImage img = null;
        try (InputStream is1 = ResourceLoader.class.getResourceAsStream(ruta1)) {
            if (is1 != null) {
                img = ImageIO.read(is1);
            } else {
                try (InputStream is2 = ResourceLoader.class.getResourceAsStream(ruta2)) {
                    if (is2 != null) {
                        img = ImageIO.read(is2);
                    }
                }
            }
        } catch (IOException e) {
            // Podrías registrar el error en un log si lo deseas
            img = null;
        }
        return img;
    }
}

