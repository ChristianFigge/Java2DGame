package game.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ResourceHelper {
    public static BufferedImage loadImage(String pathToResource) {
        try {
            return ImageIO.read(
                    Objects.requireNonNull(
                            ResourceHelper.class.getResource(pathToResource)
                    )
            );
        } catch (NullPointerException | IOException e) {
            System.err.println("Unable to load file '" + pathToResource + "'!");
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage scaleImage(BufferedImage original, int targetWidth, int targetHeight) {
        BufferedImage scaled = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = scaled.createGraphics();

        // Settings for highest possible scaling quality
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return scaled;
    }

    public static BufferedImage loadAndScaleImage(String pathToImage, int targetWidth, int targetHeight) {
        return scaleImage(loadImage(pathToImage), targetWidth, targetHeight);
    }
}
