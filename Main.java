package sample;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import static java.awt.Color.BLACK;


public class Main{
    final private static JFrame frame = new JFrame("Sorry!");
    final public static int refreshRate = 4;
    final private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    final public static int dimension=(int)screenSize.height;
    final private static int original_size=5000;

    public static void main(String[] args) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(BLACK);
        screenSize.setSize(screenSize.getHeight(),screenSize.getHeight());
        testing();
    }


    public static void testing(){
        JLabel splash = loadImage("sorry-Splash.jpg");
        JLabel back = loadImage("sorry-board.jpg");
        JLabel label2 = loadImage("Sorry-cards.png",12,1);
        JLabel label1 = loadImage("Sorry-pawns.png",4,2);
        JLabel pawn2 = loadImage("Sorry-pawns.png",4,4);
        JPanel panel = new JPanel(null);
        panel.add(splash);
        panel.add(pawn2);
        panel.add(label2);
        panel.add(label1);
        panel.add(back);
//        label1.setLocation(60,60);
        label2.setLocation(400,500);
//        splash.setLocation(0,0);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        frame.getContentPane().add(new JLabel(new ImageIcon(loadImage("Sorry-pawns.png",100,100))));
        frame.add(panel);
        frame.setSize(screenSize);
        frame.setVisible(true);
        splash.setLocation(dimension,dimension);
        for (int i = 0; i < dimension/2+6; i++) {
            try {
                Thread.sleep(refreshRate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            label1.setLocation(16,i);
            pawn2.setLocation(i,16);
        }


    }

    public static JLabel loadImage(String source){
        try {
            BufferedImage img = ImageIO.read(new File("src/sample/res/"+source));
            //scaling to window size
            int height = (dimension*img.getHeight())/original_size;
            int width  = (dimension*img.getWidth())/original_size;
            JLabel label = new JLabel(new ImageIcon(img.getScaledInstance(width,height,Image.SCALE_SMOOTH)));
            label.setBounds(0,0,width,height);
            return label;
        }catch (IOException i) {
            System.out.println("failed to find file");
            return null;
        }
    }
    public static JLabel loadImage(String source,int size,int location){
        try {
            BufferedImage img = ImageIO.read(new File("src/sample/res/"+source));
            int height = img.getHeight();
            int width = img.getWidth();
            img = img.getSubimage(((width*location)-width)/size,0,width/size,height);
            //scaling to window size
            height = (dimension*img.getHeight())/original_size;
            width  = (dimension*img.getWidth())/original_size;
            JLabel label = new JLabel(new ImageIcon(img.getScaledInstance(width,height,Image.SCALE_SMOOTH)));
            label.setBounds(0,0,width,height);
            return label;
        }catch (IOException i) {
            System.out.println("failed to find file");
            return null;
        }
    }


}
