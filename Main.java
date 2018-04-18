import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends Display {

    //    static Display DISPLAY = new Display();
    public static void main(String[] args) {
        new mainMenu();

    }

}


class mainMenu extends Main {

    mainMenu() {
        image LOADINGSCREEN = new image("loading4.png");

        image StartingPage = new image("Sorry-splash.png");
        clickSpace newGame = new clickSpace(1712, 332, 362, 1060);
        clickSpace loadGame = new clickSpace(1712, 332, 362, 1392);
        clickSpace rules = new clickSpace(1712, 332, 362, 1725);
        newGame.button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                Thread T = new Thread(()->newGame());
//                T.start();
                loadGame.disable();
                rules.disable();
                newGame.disable();


            }
        });
        loadGame.button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadGame();
                loadGame.disable();
                rules.disable();
                newGame.disable();
            }
        });
        rules.button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                Rules();
                loadGame.disable();
                rules.disable();
                newGame.disable();
            }
        });
        LOADINGSCREEN.hide();
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
