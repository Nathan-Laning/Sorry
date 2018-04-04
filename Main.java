import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends Display {
    //    static Display DISPLAY = new Display();
    public static void main(String[] args) {
        new mainMenu();
    }
}
class mainMenu extends Main{

    mainMenu(){
        clickSpace newGame = new clickSpace(3425, 665, 725, 2120);
        clickSpace loadGame = new clickSpace(3425, 665, 725, 2785);
        clickSpace rules = new clickSpace(3425, 665, 725, 2785 + 665);
        image StartingPage = new image("Sorry-splash.jpg");
        newGame.button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                newGame();
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
                Rules();
                loadGame.disable();
                rules.disable();
                newGame.disable();
            }
        });
        }
        private void newGame(){
            //just displaying gameboard for fun
            //start new Game, however we want to do that
            image GAMEBOARD=new image("Sorry-board.jpg");
        }
        private void Rules(){
            //open rules page
            //allow exit
            //re-instantiate game if started
        }
        private void loadGame(){
            //figure out load game dialogue

        }
}
