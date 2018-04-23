import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main{

    //    static Display DISPLAY = new Display();
    public static void main(String[] args) {
        new mainMenu();

    }

}


class mainMenu extends Display{
    image StartingPage = new image("Sorry-splash.png");
    mainMenu() {
        clickSpace newGame = new clickSpace(1712, 332, 362, 1060);
        clickSpace loadGame = new clickSpace(1712, 332, 362, 1392);
        clickSpace rules = new clickSpace(1712, 332, 362, 1725);
        newGame();
        newGame.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadGame.disable();
                rules.disable();
                newGame();
                newGame.disable();
            }
        });
        loadGame.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadGame();
                loadGame.disable();
                rules.disable();
                newGame.disable();
            }
        });
        rules.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                Rules();
                loadGame.disable();
                rules.disable();
                newGame.disable();
            }
        });
//       newGame();
    }


    private void newGame() {
        Thread T = new Thread(()-> StartingPage.move(2500,0,1));
        T.start();
        gameBoard G = new gameBoard();

    }

    private void Rules() {
        //open rules page
        //allow exit
        //re-instantiate game if started
        //*****TESTING: Still need to work on re sizing instructions image

        image back = new image("back.png");
        image infoPage = new image("Sorry-rules.png");
        back.reSize(70, 20);
        back.move(10, 10);
        clickSpace backButton = new clickSpace(back);
        backButton.addClick(new MouseAdapter() {
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
