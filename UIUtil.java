import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;

public class UIUtil {

    public static BufferedImage scale(BufferedImage img, double scale) {
        int w = img.getWidth();
        int h = img.getHeight();
        int dw = (int) (w * scale);
        int dh = (int) (h * scale);
        return UIUtil.resize(img, dw, dh);
    }

    public static BufferedImage resize(BufferedImage img, int width, int height) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized; 
    }

    public static BufferedImage readBufferedImage(String path) {
        path = "img/" + path;
        try {
            return ImageIO.read(new File((path))); 
        } catch (IOException e) {
            System.out.println(path + " threw");
            System.err.println(e);
        }
        return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    }

    public static ImageIcon readImageIcon(String path, int width, int height) {
        return new ImageIcon(resize(readBufferedImage(path), width, height));        
    }

    public static Color transparent(Color c, double alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (alpha * 255));
    }
}
