package it.vitalegi.mangar.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class ImageUtil {

    public static BufferedImage downloadImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveImage(BufferedImage image, String formatName, Path path) {
        try {
            boolean result = ImageIO.write(image, formatName, path.toFile());
            if (!result) {
                throw new RuntimeException("Error while saving image to " + path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
