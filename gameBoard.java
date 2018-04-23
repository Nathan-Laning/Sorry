import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * testing for yongyi
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
    private static Pawn[] pawns = new Pawn[16];
    private static int player_turn = whosTurn();
    private static final ArrayList<int[]> highlightedSpaces = new ArrayList<>();
    private static image[] deckImages = new image[13];
    private static image[] turnImages = new image[4];

    private image StartingPage = new image("Sorry-splash.png");


    //String[][] whole_board=new String[16][16];
    //1:road
    //2:home
    //3:start
    //4:slider
    //5:junction
    //6:safe
    //0:null
    //basic layout for gameboard


    protected int[][][] whole_board = {
            {{4, 1}, {3, 5}, {3, 5}, {3, 5}, {3, 5}, {4, 1}, {4, 1}, {4, 1}, {4, 1}, {3, 4}, {3, 4}, {3, 4}, {3, 4}, {3, 4}, {4, 1}, {4, 1}},
            {{4, 1}, {4, 0}, {3, 6}, {3, 3}, {3, 3}, {3, 3}, {4, 0}, {4, 0}, {1, 2}, {1, 2}, {1, 2}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {1, 5}},
            {{0, 4}, {4, 0}, {3, 6}, {3, 3}, {3, 3}, {3, 3}, {4, 0}, {4, 0}, {1, 2}, {1, 2}, {1, 2}, {1, 6}, {1, 6}, {1, 6}, {1, 6}, {1, 5}},
            {{0, 4}, {4, 0}, {3, 6}, {3, 3}, {3, 3}, {3, 3}, {4, 0}, {4, 0}, {1, 2}, {1, 2}, {1, 2}, {4, 0}, {1, 3}, {1, 3}, {1, 3}, {1, 5}},
            {{0, 4}, {4, 0}, {3, 6}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {1, 3}, {1, 3}, {1, 3}, {1, 5}},
            {{0, 4}, {3, 2}, {3, 2}, {3, 2}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {1, 3}, {1, 3}, {1, 3}, {4, 1}},
            {{0, 4}, {3, 2}, {3, 2}, {3, 2}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 1}},
            {{4, 1}, {3, 2}, {3, 2}, {3, 2}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 1}},
            {{4, 1}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {2, 2}, {2, 2}, {2, 2}, {4, 1}},
            {{4, 1}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {2, 2}, {2, 2}, {2, 2}, {1, 4}},
            {{4, 1}, {0, 3}, {0, 3}, {0, 3}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {2, 2}, {2, 2}, {2, 2}, {1, 4}},
            {{0, 5}, {0, 3}, {0, 3}, {0, 3}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {2, 6}, {4, 0}, {1, 4}},
            {{0, 5}, {0, 3}, {0, 3}, {0, 3}, {4, 0}, {0, 2}, {0, 2}, {0, 2}, {4, 0}, {4, 0}, {2, 3}, {2, 3}, {2, 3}, {2, 6}, {4, 0}, {1, 4}},
            {{0, 5}, {0, 6}, {0, 6}, {0, 6}, {0, 6}, {0, 2}, {0, 2}, {0, 2}, {4, 0}, {4, 0}, {2, 3}, {2, 3}, {2, 3}, {2, 6}, {4, 0}, {1, 4}},
            {{0, 5}, {4, 0}, {4, 0}, {4, 0}, {4, 0}, {0, 2}, {0, 2}, {0, 2}, {4, 0}, {4, 0}, {2, 3}, {2, 3}, {2, 3}, {2, 6}, {4, 0}, {4, 1}},
            {{4, 1}, {4, 1}, {2, 4}, {2, 4}, {2, 4}, {2, 4}, {2, 4}, {4, 1}, {4, 1}, {4, 1}, {4, 1}, {2, 5}, {2, 5}, {2, 5}, {2, 5}, {4, 1}},};

    gameBoard() {
        loadAssets();
        optionsAndDrawingLoad();
    }

    private void optionsAndDrawingLoad() {
        final gameBoard G = this;
        //drawpile loading
        image drawPile = new image("Sorry-Card-Back-Horizontal.png");
        drawPile.move((int) (1010 * ratio), (int) (710 * ratio));
        clickSpace DRAW = new clickSpace(drawPile);
        DRAW.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new DumbMeanAITurn(G);

            }
        });
        //options loading
        image options = new image("Sorry-options.png");
        options.move((int) (1700 * ratio), (int) (1000 * ratio));
        clickSpace OPTIONS = new clickSpace(options);
        OPTIONS.addClick(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("testing here ");
                //menu here
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

    /**
     * /- Threaded Load Assests -/
     * loads all assets to be used using threads
     * much faster, more unstable
     * optimally we would get this working always! or at least have it restart if it fails
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
            spaces[0][1] = new space(1,3);//green
            spaces[15][14] = new space(3,3);//yellow
            spaces[1][15] = new space(2,4);//blue
            spaces[14][0] = new space(0,3);//red
        //second slide
            spaces[0][9] = new space(1,4);//green
            spaces[15][6] = new space(3,4);//yellow
            spaces[9][15] = new space(2,4);//blue
            spaces[6][0] = new space(0,4);//red
//        home walkways
        for (int i = 0; i < 6; i++) {
            spaces[13][1 + i] = new space(0);//red
            spaces[1 + i][2] = new space(1);//green
            spaces[2][9 + i] = new space(2);//blue
            spaces[9 + i][13] = new space(3);//yellow
        }
//        spaces[][]
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                try {
                    out(spaces[i][j].getColor());
                    spaces[i][j].moveImage(j, i);
                } catch (NullPointerException N) {
                    out(9);
                }
                System.out.print(" ");

            }
            System.out.println();
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

    // shuffles a "new" deck
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
        pawns[0].placePawn(0,0);
        pawns[7].placePawn(4,0);
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

    space getSpace(int x, int y){
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
        private int color = 4,slideLength=0;
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
        space(int color, int slideLength){
            this.slideLength=slideLength;
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

        public int getSlide(){
        return slideLength;
        }
    }
}

