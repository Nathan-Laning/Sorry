import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * /- Display -/
 *
 * *---------------*
 * |       /\      |
 * |  /\  /  \   /\|
 * | /  \/    \/   |
 * |/   /    /     |
 * *---------------*
 *
 * Class built to handle all imaging processing, which includes:
 * loading images
 * changing image location
 * scaling window/images uniformly
 * cropping images
 * window naming
 */
class Display {
    private static int layer = 1;
    private static JLayeredPane panel = new JLayeredPane();
    private static final JFrame frame = new JFrame("Sorry!");
    private static int heightGap=22,widthGap=0;//default for osx and linux
    public static int size = 600;
    public static double ratio = size/5000.0;
    public final image glow;

    /**
     * Creates an entirely new display for images to exist in
     */
    Display() {
        if(System.getProperty("os.name").startsWith("Windows")){
            widthGap=16;
            heightGap=39;
        }
        setSize(size);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.setBounds(0, 0, size, size);
        frame.setResizable(false);
        glow=new image("glow_colored.png");
        glow.hide();
    }
    void setSize(int size){
        //need to add loading from file instead of call so desired resolution is saved
        this.size=size;
        frame.setSize(size+ widthGap, size + heightGap);
    }


    /**
     * /- Click Space -/
     * ____________
     * |      /|  |
     * |    /  |  |
     * |  /    |  |
     * |/__  _ |  |
     * |  / / \|  |
     * |_/_/______|
     *
     * Click Space is a callable method that creates a portion of space specified
     * in position and dimension. It also has a glow affect that will resize and move a
     * pre-rendered image called glow. this is to save on load times as loading new images is taxing.
     * To create a new one you need to call the constructer "clickSpace" and specify:
     *
     * NOTE: Items entered in reference to the maximum size 5000, for example
     * clickSpace(500,500,2250,2250) will but a box directly in the middle regardless of the window size
     * int width -> the width of the Click Space
     * int height -> height of the Click Space
     * int x-> the upper left corner of the CLick Space's x co-ordinate
     * int y-> the upper left corner of the CLick Space's y co-ordinate
     *
     * The Mouse initiation must be overwritten for it to work properly as button, this can be done as follows:
     * Note: the disabling part is optional, but makes it no longer clickable until it is re-enabled
     * clickSpace CLICK_HERE = new clickSpace(500,500,2250,2250);
     * CLICK_HERE.button.addMouseListener(new MouseAdapter() {
     *  @Override
     *  public void mouseClicked(MouseEvent e) {
     *      //Do
     *      CLICK_HERE.disable();
     *  }
     * });
     **/
    class clickSpace{
        // default init
        Point pos;
        int height,width;
        public JButton button;
        public java.awt.event.MouseListener M = new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                glow.reSize(width,height);
                glow.move((int)(pos.x/(size/5000.0)),(int)(pos.y/(size/5000.0)));
                glow.show();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                glow.hide();
            }
        };

        /**
         * Constructor
         * @param width the width of the Click Space
         * @param height the height of the Click Space
         * @param x the upper left corner of the CLick Space's x co-ordinate
         * @param y the upper left corner of the CLick Space's y co-ordinate
         */
        clickSpace(int width,int height,int x, int y){
            this.height = (int) (ratio * height);
            this.width = (int) (ratio * width);
            pos = new Point((int)(x*ratio),(int)(y*ratio));
            button = new JButton();
            button.setSize(this.width,this.height);
            button.setLocation(pos);

            enable();
            button.setOpaque(false);
            button.setBorderPainted(false);
            panel.add(button);
            panel.setLayer(button,layer);
        }

        //enables button
        public void enable(){
            button.addMouseListener(M);
        }
        //disables button
        public void disable(){
            button.removeMouseListener(M);
            glow.hide();
        }
    }

    /**
     * /- Image -/
     *
     * *---------------*
     * |       /\      |
     * |  /\  /  \   /\|
     * | /  \/    \/   |
     * |/   /    /     |
     * *---------------*
     *
     * Loads a new image based on the given image. It is essential that
     * images are within the res/ folder or else it will not successfully load
     * as this is an assumption within the load function
     *
     * Images will automatically scaled to the current window size, but can also be
     * resized using the resize function.
     *
     * Images can also be moved either by specifying position, or specifying position and the
     * time it takes to get there in seconds
     *
     * Images can also be loaded from larger maps by using the slicing function. this is entirely
     * automated by specifying the total number of images within the file, and the individual you desire.
     * This assumes that all images are the same width, but not height as a point of note...
     */
    class image {
        private BufferedImage img;
        private Point location = new Point(0, 0);
        private JLabel label;
        private int width,height;
        public boolean clicked=false;
        image(String imageName) {
            loadImage(imageName);
            reScale();
            panel.setBounds(0, 0, size, size);
        }

        image(String imageName, int total, int individual) {
            loadImage(imageName);
            img = img.getSubimage(((width * individual) - width) / total, 0, width / total, height);
            this.width = img.getWidth();
            this.height = img.getHeight();
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
            location = new Point((int)(x*ratio), (int)(y*ratio));
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
            x=(int)(x*ratio);
            y=(int)(y*ratio);
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
         * /- RE-SIZE -/
         * re-sizes the image based on...
         * @param width desired width
         * @param height desired height
         */
         private void reSize(int width,int height){
            this.height=(int)(height/(size/5000.0));
            this.width=(int)(width/(size/5000.0));
            reScale();
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




