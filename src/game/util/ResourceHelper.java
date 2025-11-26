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

        Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g2.dispose();

        return scaled;
    }

    public static BufferedImage loadImageAndScale(String pathToImage, int targetWidth, int targetHeight) {
        return scaleImage(loadImage(pathToImage), targetWidth, targetHeight);
    }
}
