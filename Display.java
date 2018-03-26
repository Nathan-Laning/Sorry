import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * /- IMAGE -/
 * <p>
 * *---------------*
 * |       /\      |
 * |  /\  /  \   /\|
 * | /  \/    \/   |
 * |/   /    /     |
 * *---------------*
 * <p>
 * Class built to handle all imaging processing, which includes:
 * loading images
 * changing image location
 * scaling window/images uniformly
 * cropping images
 * window naming
 * <p>
 * Should it include?
 * mouse and click events?
 * rotating if we can get it working?
 * A "grid" type system for movement and images since it will be always square?
 * A message board of types?
 */
public class Display {
    static JPanel panel = new JPanel(null);
    static final JFrame frame = new JFrame("Sorry!");
    private static double scale;//static so its applied to all children
    class image {

        private BufferedImage img;
        private Point location = new Point(0, 0);
        private int height, width, heightGap = 20;
        int size = 1000;
        boolean init = true;
        private JLabel label;

        image(String imageName) {
            loadImage(imageName);
            reScale();
            init();
            panel.setBounds(0, 0, size, size);
            panel.add(label);
        }

        public void init() {
            if (init) {
                frame.setSize(size, size + heightGap);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(panel);
                init = false;
            }
        }

        image(String imageName, int total, int individual) {
            loadImage(imageName);
            init();
            img = img.getSubimage(((width * individual) - width) / total, 0, width / total, height);
            width = img.getWidth();
            height = img.getHeight();
            reScale();
            panel.setBounds(0, 0, size, size);
            panel.add(label);
        }

        public void move(int x, int y) {
            location = new Point(x, y);
            label.setLocation(location);
        }

        private void reScale() {
            scale = size / 5000.0;
//        scale=1;
            height = (int) (scale * height);
            width = (int) (scale * width);
            label = new JLabel(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            label.setBounds(0, 0, width, height);
        }

        private void loadImage(String imageName) {
            try {
                img = ImageIO.read(new File("res/" + imageName));
                height = img.getHeight();
                width = img.getWidth();

            } catch (IOException i) {
                System.out.println("failed to find file");
            }
        }


    }
}



