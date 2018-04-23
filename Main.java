import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
public class Main{
    //    static Display DISPLAY = new Display();
    public static void main(String[] args) {
        new mainMenu();
    }
}
class mainMenu extends Display{
    image StartingPage = new image("Sorry-splash.png");
    int x,y;
	public static int player_turn;
	
	gameBoard game;
	saveGame object = new saveGame(x,y,player_turn);
    String fileName = "saved.txt";

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
                chooseCPU();
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
    private void chooseCPU() {
        image chooseMenu = new image("Sorry-Difficulty-menu.png");
        //back button
        clickSpace back = new clickSpace(700, 250, 0, 1);

        // color buttons
        clickSpace red = new clickSpace(470, 290, 108, 585);
        clickSpace green = new clickSpace(680, 290, 510, 840);
        clickSpace blue = new clickSpace(595, 290, 1099, 585);
        clickSpace yellow = new clickSpace(800, 290, 1600, 840);

        //difficulty buttons
        clickSpace nice = new clickSpace(700, 320, 270, 1520);
        clickSpace dumb = new clickSpace(700, 320, 270, 1989);
        clickSpace mean = new clickSpace(700, 320, 1417, 1520);
        clickSpace smart = new clickSpace(700, 320, 1417, 1989);

        //start new game button
        clickSpace startGame = new clickSpace(2000, 500, 1480, 2300);

        //actions performed concerning mouse + buttons
        //back button
        back.MouseEntered(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                back.button.setBorderPainted(true);
                back.button.setBorder(new LineBorder(Color.WHITE));
            }
        });
        back.MouseExited(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                back.button.setBorderPainted(false);
            }
        });
        back.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new mainMenu();
            }
        });
        //red button
        red.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                red.button.setBorderPainted(true);
                red.button.setBorder(new LineBorder(Color.RED));
                green.button.setBorderPainted(false);
                blue.button.setBorderPainted(false);
                yellow.button.setBorderPainted(false);
            }
        });
        //green button
        green.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                green.button.setBorderPainted(true);
                green.button.setBorder(new LineBorder(Color.GREEN));
                red.button.setBorderPainted(false);
                blue.button.setBorderPainted(false);
                yellow.button.setBorderPainted(false);
            }
        });
        //blue button
        blue.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                blue.button.setBorderPainted(true);
                blue.button.setBorder(new LineBorder(Color.BLUE));
                green.button.setBorderPainted(false);
                red.button.setBorderPainted(false);
                yellow.button.setBorderPainted(false);
            }
        });
        //yellow button
        yellow.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                yellow.button.setBorderPainted(true);
                yellow.button.setBorder(new LineBorder(Color.YELLOW));
                green.button.setBorderPainted(false);
                blue.button.setBorderPainted(false);
                red.button.setBorderPainted(false);
            }
        });

        //start game button
        startGame.MouseEntered(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startGame.button.setBorderPainted(true);
                startGame.button.setBorder(new LineBorder(Color.WHITE));
            }
        });
        startGame.MouseExited(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                startGame.button.setBorderPainted(false);
            }
        });
        smart.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                smart.button.setBorderPainted(true);
                smart.button.setBorder(new LineBorder(Color.CYAN));
                dumb.button.setBorderPainted(false);
            }
        });
        dumb.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dumb.button.setBorderPainted(true);
                dumb.button.setBorder(new LineBorder(Color.MAGENTA));
                smart.button.setBorderPainted(false);
            }
        });
        nice.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nice.button.setBorderPainted(true);
                nice.button.setBorder(new LineBorder(Color.ORANGE));
                mean.button.setBorderPainted(false);
            }
        });
        mean.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mean.button.setBorderPainted(true);
                mean.button.setBorder(new LineBorder(Color.PINK));
                nice.button.setBorderPainted(false);
            }
        });
//
        //START GAME HERE
        startGame.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameBoard G = new gameBoard(0, false, false);
            }
        });


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
    	try {
    		FileInputStream file = new FileInputStream(fileName);
    		ObjectInputStream in = new ObjectInputStream(file);
    		object = (saveGame)in.readObject();
    		in.close();
    		file.close();
    	} catch(IOException ex) {
    		System.out.println("IOException found");
    	}catch(ClassNotFoundException ex) {
    		System.out.println("ClassNotFoundException found");
    	}
    	
    	
    	System.out.println("x =: "+x);
		System.out.println("y =: "+y);
		System.out.println("player_turn =: "+player_turn);

    


    }
}