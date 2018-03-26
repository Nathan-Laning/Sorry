public class Main{
    public static void main(String[] args) {
        testing();
//        deck DECK = new deck();
//        int i =1;
//        for(;;){
////            System.out.println(i);
//            DECK.draw();
//            i++;
//        }
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setBackground(BLACK);
//        screenSize.setSize(screenSize.getHeight(),screenSize.getHeight());
//        testing();
    }


    public static void testing(){
//        Window w = new Window();
        Display d = new Display();
//        Display.image i = d.new image("Sorry-pawns.png",4,2);
        Display.image e = d.new image("Sorry-board.jpg");
        Display.image a = d.new image("Sorry-pawns.png",4,2);
        Display.image b = d.new image("Sorry-pawns.png",4,1);
        Display.image c = d.new image("Sorry-pawns.png",4,3);
        Display.image f = d.new image("Sorry-pawns.png",4,4);


//
        a.move(50,50,1);
        a.hide();
        b.move(50,40,1);
        c.move(50,10,1);
        f.move(50,90,1);
        a.show();

//        e.move(10,10,1);
//        j.move(0,0,1);

//        image board = new Display.image("Sorry-board.jpg");
//        pawn.init();
//        pawn.move(100,100);
//        image back = new image("sorry-board.jpg");

//        JLabel splash = loadImage("sorry-Splash.jpg");
//        JLabel back = loadImage("sorry-board.jpg");
//        JLabel label2 = loadImage("Sorry-cards.png",12,1);
//        JLabel label1 = loadImage("Sorry-pawns.png",4,2);
//        JLabel pawn2 = loadImage("Sorry-pawns.png",4,4);
//        JPanel panel = new JPanel(null);
//        panel.add(splash);
//        panel.add(pawn2);
//        panel.add(label2);
//        panel.add(label1);
//        panel.add(back);
//        label1.setLocation(60,60);
//        label2.setLocation(400,500);
//        splash.setLocation(0,0);
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        frame.getContentPane().add(new JLabel(new ImageIcon(loadImage("Sorry-pawns.png",100,100))));
//        frame.add(panel);
//        frame.setSize(screenSize);
//        frame.setVisible(true);
////        splash.setLocation(dimension,dimension);
//        for (int i = 0; i < dimension/2+6; i++) {
//            try {
//                Thread.sleep(refreshRate);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            label1.setLocation(16,i);
//            pawn2.setLocation(i,16);
//        }


    }



}
