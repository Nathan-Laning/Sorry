import java.awt.event.*;
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
class gameBoard extends Main {
    private clickSpace board = null;
    private space[][] spaces;
    private Pawn[] pawns = new Pawn[16];
    int x, y;
    private Thread D, P, S;
    public java.awt.event.MouseListener M = new java.awt.event.MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            Clicked(e.getX(), e.getY());
        }
    };


    deck DECK;
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

//    int is_home(Pawn pawn) {
//        for (int i = 0; i < 16; i++) {
//            for (int j = 0; j < 16; j++) {
//                if (whole_board[Pawn.get_x()][Pawn.get_y()][2] == 2) {
//                    return 1;
//                }
//            }
//        }
//        return 0;
//    }

    private void loadAssets(){
        image LOADINGSCREEN = new image("loading4.png");
        LOADINGSCREEN.setLayer(600);
        image GAMEBOARD = new image("Sorry-board.png");
        board = new clickSpace(GAMEBOARD);
        board.disable();
        GAMEBOARD.show();
        //starting the spaces load
        S = new Thread(() -> loadSpaces());
        S.start();
        //starting the deck load
        D = new Thread(() -> loadDeck());
        D.start();
        //starting the pawn load
        P = new Thread(() -> loadPawns());
        P.start();
        try {
            P.join();
            S.join();
            D.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        board.button.addMouseListener(M);
        LOADINGSCREEN.move(5000,5000);
    }


    gameBoard() {
        loadAssets();
        //loading background first
        //adding drawing pile
        image drawPile = new image("Sorry-Card-Back-Horizontal.png");
        drawPile.move((int) (1010* ratio), (int) (710 * ratio));
        int player_turn = whosTurn();
        clickSpace c = new clickSpace(drawPile);
        c.button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                card CARD = DECK.draw();
                switch (CARD.cardNumber) {
                    case 1://can be used to get out of start
                        /**
                         * let the pawn move to start position
                         */
                        break;
                    case 2://can be used to get out of start and draw again
                        /**
                         * let the pawn move to start position
                         * call deck.draw() again for a new card
                         */
                        break;
                    case 4://goes backwards 4
                        /**
                         * four cases:
                         * 1) x = 16, y-= 4
                         * 2) y = 16, x -= 4
                         * 3) x = 0, y -=4
                         * 4) y = 0, x -=4
                         *
                         * need to check for corners ...
                         */
                        break;
                    case 5:
                        /**
                         * four cases:
                         * 1) x = 16, y += 5
                         * 2) y = 16, x += 5
                         * 3) x = 0, y +=5
                         * 4) y = 0, x +=5
                         *
                         * Can use method call to move "forward" five times, but need to check that it can be performed first...
                         */
                        break;
                    case 7://seven can be split up
                        /**
                         * Move one pawn seven spaces forward, or split the seven spaces between two pawns (such as four spaces for one pawn and three for another).
                         * This makes it possible for two pawns to enter Home on the same turn, for example.
                         * The seven cannot be used to move a pawn out of Start, even if the player splits it into a six and one or a five and two.
                         * The entire seven spaces must be used or the turn is lost. You may not move backwards with a split.
                         * oof
                         * keep counter for number of coordinates/steps taken (?)
                         * no cases here, just need to give user full reign somehow... and options to choose what they wanna do....
                         */

                        break;
                    case 10://can also go backwards 1
                        /**
                         * four basic cases if choose to move forward:
                         * 1) x = 16, y += 10
                         * 2) y = 16, x += 10
                         * 3) x = 0, y += 10
                         * 4) y = 0, x += 10
                         *
                         * four cases if choose to move back one:
                         * 1) x = 16, y-= 1
                         * 2) y = 16, x -= 1
                         * 3) x = 0, y -= 1
                         * 4) y = 0, x -= 1
                         */

                        break;
                    case 11://can be used to replace or moved!
                        /**
                         *Move 11 spaces forward, or switch the places of one of the player's own pawns and an opponent's pawn.
                         *  A player that cannot move 11 spaces is not forced to switch and instead can forfeit the turn.
                         *  An 11 cannot be used to switch a pawn that is in a Safety Zone.
                         *
                         *  if switch:
                         */

                        break;
                    case 12:
                        /**
                         * four cases :
                         * 1) x = 16, y+=12
                         * 2) y = 16, x +=12
                         * 3) x = 0, y +=12
                         * 4) y = 0, x +=12
                         * Can use method call to move "forward" 12 times, but need to check that it can be performed first...
                         */
                        break;
                    case 0://sorry!

                        break;
                }
            }


        });
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
        for (int i = 0; i < 4; i++) {
            spaces[0][1 + i] = new space(1);//green
            spaces[15][14 - i] = new space(3);//yellow
            spaces[1 + i][15] = new space(2);//blue
            spaces[14 - i][0] = new space(0);//red
        }
        //second slide
        for (int i = 0; i < 5; i++) {
            spaces[0][9 + i] = new space(1);//green
            spaces[15][6 - i] = new space(3);//yellow
            spaces[9 + i][15] = new space(2);//blue
            spaces[6 - i][0] = new space(0);//red
        }
        //home/start
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (!((j == 2 || j == 0) && i > 0)) spaces[10 + j][2 + i] = new space(0);//red
                if (!((j == 2 || j == 0) && i > 0)) spaces[12 + j][6 + i] = new space(0);//red
                if (!((j == 2 || j == 0) && i > 0)) spaces[2 + i][3 + j] = new space(1);//green
                if (!((j == 2 || j == 0) && i > 0)) spaces[6 + i][1 + j] = new space(1);//green
                if (!((j == 2 || j == 0) && i == 0)) spaces[3 + j][12 + i] = new space(2);//blue
                if (!((j == 2 || j == 0) && i == 0)) spaces[1 + j][8 + i] = new space(2);//blue
                if (!((j == 2 || j == 0) && i == 0)) spaces[12 + i][10 + j] = new space(3);//yellow
                if (!((j == 2 || j == 0) && i == 0)) spaces[8 + i][12 + j] = new space(3);//yellow
            }
        }
//        home walkways
        for (int i = 0; i < 5; i++) {
            spaces[13][1 + i] = new space(0);//red
            spaces[1 + i][2] = new space(1);//green
            spaces[2][10 + i] = new space(2);//blue
            spaces[10 + i][13] = new space(3);//yellow
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

    private void loadDeck() {
        DECK = new deck();
    }

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
        pawns[0].move(0, 11, .9);
    }
    private static int whosTurn() {
        Random rand = new Random();
        int num = rand.nextInt(4);
        return num;
    }

    public void Clicked(int x, int y) {

        pawns[0].moveForward();
    }

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
class space extends Main {

    private boolean slide = false, safe = false;
    private int color = 4;
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
        //snuck this in here, it creates safe areas where necessary
        if (!(x == 15 || x == 0 || y == 15 || y == 0)) safe = true;
        img.move(convertFromCooridinate(x), convertFromCooridinate(y));
    }
    //returns whether or not it is a sliding block
    public boolean isSlide() {
        return slide;
    }
    //returns the color, see above for reference
    public int getColor() {
        return color;
    }
}


/**
 * /- PAWN -/
 *     _
 *    (_)
 *    / \
 *  _/_ \_
 * (______)
 * Pawn class saves each pawn as instigated and it is only needed to be given
 * an integer to represent its color/team:
 * 0 -> red
 * 3 -> green
 * 1 -> blue
 * 2 -> yellow
 * Movement is also handled using current directions of x and y.
 * -> such as (0,1) is positive y (down on the screen)
 * -> such as (-1,0) is negative x (left on the screen)
 */
class Pawn extends Main {
    private int color;
    private int[] direction,homeEntrance,boardEntrance;
    private int x, y;
    private int originalX, originalY;
    private image PAWN, PAWN_HIGHLIGHT;
    private boolean completed=false;


    /**
     * creates new pawn based on its color, and x,y position in the coordinate  plane
     *
     * @param color 0, 1, 2, or 3 for red, green, blue, or yellow
     * @param x     (0->15)
     * @param y     (0->15)
     */
    Pawn(int color, int x, int y) {
        originalX = x;
        originalY = y;
        this.x = x;
        this.y = y;
        this.color = color;
        int placement = 0;
        switch (color) {
            case 0:
                placement = 3;
                direction = new int[]{0, -1};
                boardEntrance = new int[]{0, 11};
                homeEntrance = new int[]{0, 13};
                break;
            case 1:
                placement = 4;
                direction = new int[]{1, 0};
                boardEntrance = new int[]{4, 0};
                homeEntrance = new int[]{2, 0};
                break;
            case 2:
                placement = 1;
                direction = new int[]{0, 1};
                boardEntrance = new int[]{15, 4};
                homeEntrance = new int[]{15, 2};
                break;
            case 3:
                placement = 2;
                direction = new int[]{-1, 0};
                boardEntrance = new int[]{13, 15};
                homeEntrance = new int[]{11, 15};
                break;
        }
        PAWN = new image("Sorry-pawns.png", 4, placement);
        PAWN_HIGHLIGHT = new image("Sorry-pawns-highlight.png", 4, placement);
        placePawn(x, y);
        PAWN_HIGHLIGHT.hide();
    }


    /**
     * /- PLACE PAWN -/
     *
     * Places pawn at a specified coordinate location
     *
     * @param x (0->15)
     * @param y (0->15)
     */
    void placePawn(int x, int y) {
        PAWN_HIGHLIGHT.move(convertFromCooridinate(x), convertFromCooridinate(y));
        PAWN.move(convertFromCooridinate(x), convertFromCooridinate(y));
        this.x = x;
        this.y = y;
    }

    //NEED TO RETURN PAWN'S POSITION IN ARRAY
    int get_x() {
        return x;
    }

    int get_y() {
        return y;
    }

    public void finish(){
        completed=true;

    }
    public void highlight() {
        PAWN_HIGHLIGHT.show();
    }

    public void hideHighlight() {
        PAWN_HIGHLIGHT.hide();
    }

    //moves pawn back to its start position
    void moveToStart() {
        PAWN_HIGHLIGHT.move(convertFromCooridinate(originalX), convertFromCooridinate(originalY));
        PAWN.move(convertFromCooridinate(originalX), convertFromCooridinate(originalY));
    }
    //moves to initial position
    void EnterBoard(){
        move(boardEntrance[0],boardEntrance[1],.2);
    }


    /**
     * /- MOVE -/
     * moves pawn to specific coordinate position in a specified amount of time
     *
     * @param x       (0->15)
     * @param y       (0->15)
     * @param seconds approx. time it will take
     */
    public void move(int x, int y, double seconds) {
        PAWN_HIGHLIGHT.move(convertFromCooridinate(x), convertFromCooridinate(y));
        PAWN_HIGHLIGHT.hide();
        PAWN.threadedMove(convertFromCooridinate(x), convertFromCooridinate(y), seconds);

        this.x = x;
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    /**
     * /- MOVE FORWARD -/
     *
     * determines local surroundings and saved personal team data to determine the next location to travel
     * in, then increments by one in that direction. To be used in conjunction with some type of moving plot.
     *
     */
    public void moveForward() {
        if(!completed) {
            if ((x + 1) > 15 && (y - 1) < 0) {
                direction = new int[]{0, 1};
            } else if ((x - 1) < 0 && (y - 1) < 0) {
                direction = new int[]{1, 0};
            } else if ((x - 1) < 0 && (y + 1) > 15) {
                direction = new int[]{0, -1};
            } else if ((x + 1) > 15 && (y + 1) > 15) {
                direction = new int[]{-1, 0};
            }
            if (x == homeEntrance[0] && y == homeEntrance[1]) {
                if (x - 1 < 0) {
                    direction = new int[]{1, 0};
                }
                if (x + 1 > 15) {
                    direction = new int[]{-1, 0};
                }
                if (y - 1 < 0) {
                    direction = new int[]{0, 1};
                }
                if (y + 1 > 15) {
                    direction = new int[]{0, -1};
                }
            }
            move(x + direction[0], y + direction[1], .2);
        }
    }
}

