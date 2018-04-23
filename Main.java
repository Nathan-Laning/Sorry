import javax.swing.border.LineBorder;
import java.awt.*;
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
//        newGame();
        newGame.MouseEntered(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                newGame.button.setBorderPainted(true);
                newGame.button.setBorder(new LineBorder(Color.WHITE));
            }

        });
        newGame.MouseExited(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                newGame.button.setBorderPainted(false);
            }

        });
        loadGame.MouseEntered(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loadGame.button.setBorderPainted(true);
                loadGame.button.setBorder(new LineBorder(Color.WHITE));
            }

        });
        loadGame.MouseExited(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                loadGame.button.setBorderPainted(false);
            }

        });
        rules.MouseEntered(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                rules.button.setBorderPainted(true);
                rules.button.setBorder(new LineBorder(Color.WHITE));
            }

        });
        rules.MouseExited(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                rules.button.setBorderPainted(false);
            }

        });
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
        //ask how many human players/cpu players
        //ask difficulty levels
        //new image for num players
        //go to difficulty levels page
        //start game
        gameBoard G = new gameBoard(0,false,false);

    }

    private void Rules() {
        //open rules page
        //allow exit

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
