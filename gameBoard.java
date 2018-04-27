import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.sql.*;
/**
 * /- GAME BOARD -/
 *      _________________
 *     /                /
 *    /                /
 *   /     SORRY!     /
 *  /                /
 * /________________/
 * <p>
 * saves information about peg location, the game board as a whole,
 * and whether or not moves can be made
 * there is a different board for each color
 * the information is saved for each board as its own class below.
 * <p>
 */
class gameBoard extends Display {
    private ArrayList<Integer> DECK = new ArrayList<>();
    private Random R = new Random(System.currentTimeMillis());
    private static image discardPile;
    private static clickSpace board;
    private static space[][] spaces;
    private Pawn[] pawns = new Pawn[16];
    private boolean mean, smart;
    private int player_turn = whosTurn(),playerColor = 0;
    private static final ArrayList<int[]> highlightedSpaces = new ArrayList<>();
    private static image[] deckImages = new image[13], turnImages = new image[4];
    private java.awt.event.MouseListener DrawDeck;
    private saveGame GAME;
    private String fileName = "saved.txt";
    //Database setup
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement prepStatement = null;
    private ResultSet result = null;
    final private String host = "mysql://webdb.uvm.edu";
    final private String user = "anoor_admin";
    final private String passwd = "Q5UdcJXRvQ9ZmBMB";
    String player;
    int score;
    String playerName = System.getProperty("user.name");
    int yourScore;
    //StringBuffer outputList = new StringBuffer ("");
    
    
    /**
     * new game instance,newly determined items
     *
     * @param playerColor desired player color
     * @param smart       whether or not the ai is smart
     * @param mean        whether or not the ai is mean
     */
    gameBoard(int playerColor, boolean smart, boolean mean) {
        this.playerColor = playerColor;
        determineAI();
        loadAssets();
        optionsAndDrawingLoad();
        this.mean = mean;
        this.smart = smart;
        if(allBackHome()) {
            finishGame();
        }
        
            }

    //reads Database
    public String readDB() throws Exception{
    	String failed = "System Failed";
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    		connect = DriverManager.getConnection("jdbc:"+host+"/ANOOR_Sorry",
    				user,passwd);
    		
    		statement = connect.createStatement();
    		result = statement.executeQuery("SELECT * FROM Stat;");
    		while(result.next()) {
    			playerName = result.getString(player);
    			yourScore = result.getInt(score);
    			//outputList.append(playerName);
    			//outputList.append(yourScore);
    			
    		}
    	
    	}catch(Exception e) {
    		System.err.println(e);
    		return failed;
    	}finally {
    		close();
    	}
    	//return outputList.toString();
    	return null;
    	
    }
    public void write(String player, int score)throws SQLException{
    	String query = " INSERT into Stat(player, score)" + "values(?, ?)";
    	prepStatement = connect.prepareStatement(query);
    	prepStatement.setString (1,playerName);
    	prepStatement.setInt(2, score);
    	prepStatement.executeQuery();
    	close();
    	
    }
    	private void close() {
    	    try {
    	      if (result != null) {
    	        result.close();
    	      }

    	      if (statement != null) {
    	        statement.close();
    	      }

    	      if (connect != null) {
    	        connect.close();
    	      }
    	    } catch (Exception e) {

    	    }
    	  }
    
    	
    

    /**
     * load game instance, where pawns and current turn is also passed
     *
     * @param playerColor desired player color
     * @param smart       whether or not the ai is smart
     * @param mean        whether or not the ai is mean
     * @param pawns       the Pawns, for relocating all pawns
     * @param currentTurn the current turn
     */
    gameBoard(int playerColor, boolean smart, boolean mean, Pawn[] pawns, int currentTurn) {
        determineAI();
        loadAssets();
        optionsAndDrawingLoad();
        for (int i = 0; i < 16; i++) {
            this.pawns[i].placePawn(pawns[i].getX(), pawns[i].getY());
        }
        this.playerColor = playerColor;
        this.player_turn = currentTurn;
        this.mean = mean;
        this.smart = smart;
    }


    void checkHomes(){
        backHome=new int[4];
        for (Pawn P:pawns) {
            if(P.isCompleted()) {
                backHome[P.getColor()]++;
            }
        }
        for(int i=0;i<backHome.length;i++){
            teamScore[i]=addScore(i);
            if(backHome[i]==4){
                teamScore[i]+=winnerScore(i);
            }
        }
    }
    int addScore(int color){
        for(int i=0;i<backHome.length;i++){
            if(backHome[i]==color){
                return backHome[i]*5;
            }
        }
        return 0;
    }
    int winnerScore(int color){
        int max=0;
        for(int i=0;i<backHome.length;i++){
            if(backHome[i]>backHome[i+1]){
                max=backHome[i];
            }
        }
        if(max==3){
            return (16-backHome[1]+backHome[2]+backHome[3]+backHome[0])*5;
        }
        else if(max==2){
            return 25;
        }
        else if(max==1){
            return 50;
        }else if(max==0){
            return 100;
        }
        return 0;
    }
    boolean allBackHome(){
        for (int i=0;i<backHome.length;i++){
            if(backHome[i]==4){
                return true;
            }
        }
        return false;
    }


    /**
     * Determines which AI will be loaded using previously loaded values
     */
    private void determineAI() {
        if (!smart) {
            if (mean) {
                DrawDeck = new java.awt.event.MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        cycleTeams();
                        if (player_turn != playerColor) {
                            new DumbMeanAITurn(gameBoard.this);
                        } else {
                            new UserTurn(gameBoard.this);
                        }
                    }
                };
            } else {
                DrawDeck = new java.awt.event.MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        cycleTeams();
                        if (player_turn != playerColor) {
                            new DumbNiceAITurn(gameBoard.this);
                        } else {
                            new UserTurn(gameBoard.this);
                        }
                    }
                };
            }
        }
        if (smart) {
            if (mean) {
                DrawDeck = new java.awt.event.MouseAdapter() {

                    public void mouseClicked(MouseEvent e) {
                        cycleTeams();
                        if (player_turn != playerColor) {
                            new SmartMeanAITurn(gameBoard.this);
                        } else {
                            new UserTurn(gameBoard.this);
                        }
                    }
                };
            } else {
                DrawDeck = new java.awt.event.MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        cycleTeams();
                        if (player_turn - 1 != playerColor) {
                            new SmartNiceAITurn(gameBoard.this);
                        } else {
                            new UserTurn(gameBoard.this);
                        }
                    }
                };
            }
        }
    }

    void finishGame(){
        JTextArea Scores = new JTextArea("Red Score:"+teamScore[0]+"\n"+"Blue score: "+teamScore[1]+"\n"+"Yellow score: "+teamScore[2]+"\n"+"Green score: "+teamScore[3],6,8);
//        Scores.setText("Hello");
        //testing
        panel.add(Scores);
        panel.setLayer(Scores, 9999);
        Scores.setFont(Scores.getFont().deriveFont((float) (100*ratio)));
        Scores.setForeground(Color.WHITE);
        Scores.setBounds((int)(875*ratio),(int)(875*ratio),(int)(750*ratio),(int)(750*ratio));
        Scores.setBackground(Color.BLACK);
    }

    /**
     * Pre-loading all buttons on screen including options and draw card
     */
    private void optionsAndDrawingLoad() {
        // draw pile loading
        image drawPile = new image("Sorry-Card-Back-Horizontal.png");
        drawPile.move((int) (1010 * ratio), (int) (710 * ratio));
        clickSpace DRAW = new clickSpace(drawPile);
        DRAW.addClick(DrawDeck);
        //options loading
        image options = new image("Sorry-options.png");
        options.move((int) (1700 * ratio), (int) (1000 * ratio));
        clickSpace OPTIONS = new clickSpace(options);
        OPTIONS.MouseEntered(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                OPTIONS.button.setBorderPainted(true);
                OPTIONS.button.setBorder(new LineBorder(Color.WHITE));
            }

        });
        OPTIONS.MouseExited(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                OPTIONS.button.setBorderPainted(false);
            }

        });
        OPTIONS.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Thread T = new Thread(() -> optionsMenu());
                T.start();
            }
        });
    }

    /**
     * /- Options Menu -/
     * Ingame option menu where the user can:
     * -> resume
     * -> save game
     * -> quit to main menu
     */
    private void optionsMenu() {
        image pauseMenu = new image("Pause-Menu.png");
        clickSpace resume = new clickSpace(1747, 343, 366, 677);
        clickSpace saveGame = new clickSpace(1747, 343, 366, 1020);
        clickSpace quit = new clickSpace(1747, 343, 366, 1363);
        resume.MouseEntered(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                resume.button.setBorderPainted(true);
                resume.button.setBorder(new LineBorder(Color.WHITE));

            }

        });
        saveGame.MouseEntered(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveGame.button.setBorderPainted(true);
                saveGame.button.setBorder(new LineBorder(Color.WHITE));

            }

        });
        quit.MouseEntered(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                quit.button.setBorderPainted(true);
                quit.button.setBorder(new LineBorder(Color.WHITE));
            }

        });
        resume.MouseExited(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                resume.button.setBorderPainted(false);
            }

        });
        saveGame.MouseExited(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                saveGame.button.setBorderPainted(false);
            }

        });
        quit.MouseExited(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                quit.button.setBorderPainted(false);
            }

        });
        resume.addClick(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                pauseMenu.move(5000, 5000);
                resume.button.setBorderPainted(false);
                saveGame.button.setBorderPainted(false);
                quit.button.setBorderPainted(false);
                resume.hide();
                saveGame.hide();
                quit.hide();

            }
        });
        saveGame.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //save game
                //save game
                GAME = new saveGame(pawns, player_turn, playerColor, mean, smart);
                try {
                    FileOutputStream file = new FileOutputStream(fileName);
                    ObjectOutputStream out = new ObjectOutputStream(file);
                    out.writeObject(GAME);
                    out.close();
                    file.close();
                } catch (IOException ex) {
                    System.out.println("IOException found");
                }
                pauseMenu.move(5000, 5000);
                resume.button.setBorderPainted(false);
                saveGame.button.setBorderPainted(false);
                quit.button.setBorderPainted(false);
                resume.hide();
                saveGame.hide();
                quit.hide();


            }
        });
        quit.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainMenu menu = new mainMenu();
                resume.button.setBorderPainted(false);
                saveGame.button.setBorderPainted(false);
                quit.button.setBorderPainted(false);
                resume.disable();
                saveGame.disable();
                quit.disable();


            }
        });

    }

    /**
     * /- Load Assests -/
     * loads all assets to be used
     */
    private void loadAssets() {
        image GAMEBOARD = new image("Sorry-board.png");
        board = new clickSpace(GAMEBOARD);
        board.disable();
        loadTurnImages();
        GAMEBOARD.show();
        //starting the deck load
        loadDeck();
        //starting the spaces load
        loadSpaces();
        //starting the pawn load
        loadPawns();
    }

    int getPlayer_turn() {
        return player_turn;
    }

    /**
     * /- Threaded Load Assests -/
     * loads all assets to be used using threads
     * much faster, much more unstable
     * optimally we would get this working always! But its fast enough for now to not bother
     */
    private void threadedLoadAssets() {

        image GAMEBOARD = new image("Sorry-board.png");
        board = new clickSpace(GAMEBOARD);
        board.disable();
        GAMEBOARD.show();
        loadTurnImages();
        //starting the deck load
        Thread D = new Thread(() -> loadDeck());
        D.start();
        //starting the spaces load
        Thread S = new Thread(() -> loadSpaces());
        S.start();
        //starting the pawn load
        Thread P = new Thread(() -> loadPawns());
        P.start();
        try {
            P.join();
            S.join();
            D.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    //simple loading of images for turn changing
    private void loadTurnImages() {
        for (int i = 0; i < 4; i++) {
            turnImages[i] = new image("Sorry-turn.png", 4, i + 1);
            turnImages[i].move((int) (290 * ratio), (int) (1240 * ratio));
            turnImages[i].hide();
        }

    }

    /**
     * /- LOAD SPACES -/
     * Loading all of the spaces to be interacted with.
     */
    private void loadSpaces() {
        spaces = new space[16][16];
        //ring of board
        for (int i = 0; i < 16; i++) {
            spaces[0][i] = new space();
            spaces[15][i] = new space();
            spaces[i][0] = new space();
            spaces[i][15] = new space();
        }
        //first slide
        spaces[0][1] = new space(1, 3);//green
        spaces[15][14] = new space(3, 3);//yellow
        spaces[1][15] = new space(2, 3);//blue
        spaces[14][0] = new space(0, 3);//red
        //second slide
        spaces[0][9] = new space(1, 4);//green
        spaces[15][6] = new space(3, 4);//yellow
        spaces[9][15] = new space(2, 4);//blue
        spaces[6][0] = new space(0, 4);//red
//        home walkways
        for (int i = 0; i < 6; i++) {
            spaces[13][1 + i] = new space(0);//red
            spaces[1 + i][2] = new space(1);//green
            spaces[2][9 + i] = new space(2);//blue
            spaces[9 + i][13] = new space(3);//yellow
        }
//        moving all images to spaces and outputting small ascii version for checking
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                try {
                    out(spaces[i][j].getColor());//to be removed
                    spaces[i][j].moveImage(j, i);
                } catch (NullPointerException N) {
                    out(9);//to be removed
                }
                System.out.print(" ");//to be removed
            }
            System.out.println();//to be removed
        }
    }

    /**
     * /- Load Deck -/
     * primes the deck to be used, and pre-loads all images to be used
     */
    private void loadDeck() {
        discardPile = new image("Sorry-Card-Back-Horizontal.png");
        discardPile.move((int) (1010 * ratio), (int) (1460 * ratio));
        shuffleDeck();
        String name = "Sorry-cards-Enlarged-Shadow.png";
        deckImages[0] = new image(name, 12, 12);
        deckImages[1] = new image(name, 12, 2);
        deckImages[2] = new image(name, 12, 3);
        deckImages[3] = new image(name, 12, 4);
        deckImages[4] = new image(name, 12, 5);
        deckImages[5] = new image(name, 12, 6);
        deckImages[7] = new image(name, 12, 7);
        deckImages[8] = new image(name, 12, 8);
        deckImages[10] = new image(name, 12, 9);
        deckImages[11] = new image(name, 12, 10);
        deckImages[12] = new image(name, 12, 11);
        for (int j = 0; j < deckImages.length; j++) {
            if (j != 6 && j != 9) {
                deckImages[j].move((int) (995 * ratio), (int) (ratio * 910));
                deckImages[j].hide();
            }
        }
    }

    // Loads pawns into default location (starting spot)
    private void loadPawns() {
        int[][] pos = {
                {2, 10}, {2, 11}, {2, 12}, {3, 11},
                {3, 2}, {4, 2}, {5, 2}, {4, 3},
                {13, 3}, {13, 4}, {13, 5}, {12, 4},
                {12, 13}, {11, 13}, {10, 13}, {11, 12}
        };
        int k = 0;
        for (int j = 0; j < 16; j++) {
            pawns[j] = new Pawn(k, pos[j][0], pos[j][1]);
            k = (j + 1) / 4;
        }
    }

    /**
     * Draws a new card, to be used by the turn module
     *
     * @return int card number
     */
    public int draw() {
        if (DECK.size() == 0) {

            shuffleDeck();
        }
        if (DECK.size() == 44) {
            discardPile.show();
        }
        int cardNumber = DECK.remove(R.nextInt(DECK.size()));
        deckImages[cardNumber].show();
        return cardNumber;
    }

    //picks a random player to go first
    void shuffleDeck() {
        discardPile.hide();
        //adding 5 "one cards"
        for (int i = 0; i < 5; i++) {
            DECK.add(1);
        }
        //adding all other cards
        for (int j = 0; j < 13; j++) {
            if (j != 1 && j != 6 && j != 9) {
                for (int i = 0; i < 4; i++) {
                    DECK.add(j);
                }
            }

        }
    }

    //loads all pawns in home position and images
    private static int whosTurn() {
        Random rand = new Random();
        int num = rand.nextInt(4);
        return num;
    }

    //returns all pawns, to be used by turn
    public Pawn[] getPawns() {
        return pawns;
    }

    //cycles to the next team
    public int cycleTeams() {
        turnImages[player_turn].hide();
        player_turn++;
        if (player_turn == 4) player_turn = 0;
        turnImages[player_turn].show();
        System.out.println(player_turn);
        return player_turn;
    }

    //gets your "teammate" pawns
    public ArrayList<Pawn> getTeamPawns(int color) {
        ArrayList<Pawn> P = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            P.add(pawns[(color * 4) + i]);
        }
        return P;
    }

    //hides all previously highlighted spaces
    void hideHighlightedSpaces() {
        for (int[] xy : highlightedSpaces) {
            spaces[xy[1]][xy[0]].hideHighlight();
        }
        highlightedSpaces.clear();
    }

    //highlights specific spaces using co-ordinate system (int[])
    void highlightSpace(int[] xy) {
        spaces[xy[1]][xy[0]].highlight();
        highlightedSpaces.add(xy);
    }

    //highlights specific spaces using co-ordinate system (int x, int y)
    void highlightSpace(int x, int y) {
        spaces[y][x].highlight();
        highlightedSpaces.add(new int[]{x, y});
    }

    //returns the color of a space
    int checkSpace(int x, int y) {
        try {
            return spaces[y][x].getColor();
        } catch (NullPointerException N) {
            return -1;
        }
    }

    //displays board for checking of construction, to be removed
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

    //adds a mouse click to the board (entire board) to be used by turn
    void addMouseClick(MouseListener ML) {
        board.addClick(ML);
    }

    space getSpace(int x, int y) {
        return spaces[y][x];
    }

    //removes a mouse click to the board (entire board) to be used by turn
    void removeMouseClick(MouseListener ML) {
        board.removeClick(ML);
    }
    
   

    /**
     * /- SPACE -/
     * ________
     * |      |
     * |(type)|
     * |______|
     * <p>
     * Space class represents the individual spaces that can be populated by a peg
     * there are four types, normal (default call), "slide", "start", and "home".
     * creating these is as simple as passing the strings seen above ^
     * and passing which team it will be associated with 0,1,2, or 3 where
     * 0 -> red
     * 3 -> green
     * 1 -> blue
     * 2 -> yellow
     * 4 -> white/no team
     * And empty call will default to a regular space unless told otherwise
     * <p>
     * these spaces can also be populated with pawns utilizing the add and remove pawn method calls
     * as before, an add call takes an int as the team (see above) a remove call needs no input
     * and there will be the test call "isFilled"to see if a space is filled
     * <p>
     */
    class space {
        private int color = 4, slideLength = 0;
        private image img;

        space() {
            img = new image("space-highlight.png");
            img.hide();
        }

        space(int color) {
            this.color = color;
            img = new image("space-highlight.png");
            img.hide();
        }

        space(int color, int slideLength) {
            this.slideLength = slideLength;
            this.color = color;
            img = new image("space-highlight.png");
            img.hide();
        }

        //displays highlight for tile
        public void highlight() {
            img.show();
        }

        //hides highlight for tile
        public void hideHighlight() {
            img.hide();
        }

        //moving the highlight images (hidden)
        void moveImage(int x, int y) {
            img.move(convertFromCooridinate(x), convertFromCooridinate(y));
        }
        //returns whether or not it is a sliding block

        //returns the color, see above for reference
        public int getColor() {
            return color;
        }

        public int getSlide() {
            return slideLength;
        }
    }
}

