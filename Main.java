import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends Display {
    //    static Display DISPLAY = new Display();
    public static void main(String[] args) {
        new mainMenu();

    }

    static void out(int i) {
        switch (i) {
            case 0:
                System.out.print("\u001B[31m");
                break;
            case 1:
                System.out.print("\u001B[32m");
                break;
            case 2:
                System.out.print("\u001B[34m");
                break;
            case 3:
                System.out.print("\u001B[33m");
                break;
            case 4:
                System.out.print("\u001B[30m");
                break;
            default:
                System.out.print(" ");
                return;
        }
        System.out.print(i);
        System.out.print("\u001B[0m");
    }

}


class mainMenu extends Main {

    mainMenu() {

//        clickSpace newGame = new clickSpace(3425, 665, 725, 2120);
//        clickSpace loadGame = new clickSpace(3425, 665, 725, 2785);
//        clickSpace rules = new clickSpace(3425, 665, 725, 2785 + 665);
//        image StartingPage = new image("Sorry-splash.jpg");
//
//
////        grow_test.hide();
////        grow_test.grow(1);
//
//
//        newGame.button.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                System.out.println("clicked");
//                newGame();
//                loadGame.disable();
//                rules.disable();
//                newGame.disable();
//            }
//        });
//        loadGame.button.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
////                loadGame();
//                loadGame.disable();
//                rules.disable();
//                newGame.disable();
//            }
//        });
//        rules.button.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                Rules();
//                loadGame.disable();
//                rules.disable();
//                newGame.disable();
//            }
//        });
        newGame();
    }

    private void newGame() {
        //just displaying gameboard for fun
        //start new Game, however we want to do that

        gameBoard G = new gameBoard();

    }

    private void Rules() {
        //open rules page
        //allow exit
        //re-instantiate game if started
        //*****TESTING: Still need to work on re sizing instructions image

        image back = new image("back.png");
        image infoPage = new image("sorry_rules.png");
        back.reSize(70, 20);
        back.move(10, 10);
        clickSpace backButton = new clickSpace(back);
        backButton.button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //image clickedBack = new image("clickedBack.jpg");
                new mainMenu();
            }
        });

    }

    private void loadGame() {
        //figure out load game dialogue

    }
}
