import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * /- Display -/
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
class Display {
    private static int layer = 1;
    private static JLayeredPane panel = new JLayeredPane();
    private static final JFrame frame = new JFrame("Sorry!");
    private static int height, width, heightGap=0,widthGap=0;
    public static int size = 1000;

    /**
     * Creates an entirely new display for images to exist in
     */
    Display() {
        if(System.getProperty("os.name").startsWith("Windows")){
            widthGap=16;
            heightGap=39;
        }
        frame.setSize(size+ widthGap, size + heightGap);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.setBounds(0, 0, size, size);
        frame.setResizable(false);
    }

    class image{
        private BufferedImage img;
        private Point location = new Point(0, 0);
        private JLabel label;


        image(String imageName) {
            loadImage(imageName);
            reScale();
            panel.setBounds(0, 0, size, size);
        }


        image(String imageName, int total, int individual) {
            loadImage(imageName);
            img = img.getSubimage(((width * individual) - width) / total, 0, width / total, height);
            width = img.getWidth();
            height = img.getHeight();
            reScale();
            panel.setBounds(0, 0, size, size);
        }

        /**
         * /- MOVE -/
         * moves image object to new specified position instantly
         * @param x new x
         * @param y new y
         */
        void move(int x, int y) {
            location = new Point(x, y);
            label.setLocation(location);
        }

        /**
         * /- MOVE -/
         * moves image object to new specified position taking the amount of time given
         * @param x new x
         * @param y new y
         * @param delay delay in seconds
         */
        void move(int x, int y,double delay) {
            double millis_delay = delay*1000*(2.0/3.0);//the 2/3 is because of the natural running delay
            double xRate = ((x-location.getX())/(millis_delay));
            double yRate = ((y-location.getY())/(millis_delay));
            double xDifference=location.getX();
            double yDifference=location.getY();
            for(int i = 0; i < millis_delay*10; i+=10) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    location=new Point(x,y);
                }
                xDifference+=xRate;
                yDifference+=yRate;
                location = new Point((int)xDifference, (int)yDifference);
                label.setLocation(location);
            }

        }


        /**
         * /- RE-SCALE -/
         * rescales image based on window size
         */
        private void reScale() {
            double scale = size / 5000.0;
            height = (int) (scale * height);
            width = (int) (scale * width);
            label = new JLabel(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            panel.add(label);
            layer++;
            panel.setLayer(label,layer);
            label.setBounds(0, 0, width, height);
        }

        /**
         * /- LOAD IMAGE -/
         * loads the image into the panel using the name
         * @param imageName the name of the image, check res for resources
         */
        private void loadImage(String imageName) {
            try {
                img = ImageIO.read(new File("res/" + imageName));
                height = img.getHeight();
                width = img.getWidth();

            } catch (IOException i) {
                System.out.println("failed to find file");
            }
        }

        /**
         * /- HIDE -/
         * Hides image behind all other images
         *
         */
        void hide(){
            panel.setLayer(label,0);
        }

        /**
         * /- SHOW -/
         * Bring image to the very front
         */
        void show(){
            layer++;
            panel.setLayer(label,layer);
        }

    }
}



