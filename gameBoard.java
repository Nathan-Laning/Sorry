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
class gameBoard extends Display{
    private static clickSpace board = null;
    private static space[][] spaces;
    private static Pawn[] pawns = new Pawn[16];
    private Thread D, P, S;
    private int player_turn = whosTurn();

    private static final ArrayList<int[]> highlightedSpaces=new ArrayList<>();


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

        LOADINGSCREEN.hide();
    }


    gameBoard() {
        loadAssets();
        //loading background first
        //adding drawing pile
        final gameBoard G = this;
        image drawPile = new image("Sorry-Card-Back-Horizontal.png");
        drawPile.move((int) (1010* ratio), (int) (710 * ratio));

        clickSpace c = new clickSpace(drawPile);
        c.button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new turn(G);
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
        pawns[0].move(0, 9, .9);
//        pawns[0].determineDirection();
    }
    private static int whosTurn() {
        Random rand = new Random();
        int num = rand.nextInt(4);
        return num;
    }


    public Pawn[] getPawns() {
        return pawns;
    }

    public int cycleTeams(){
       if(player_turn+1==4)return 0;
       return player_turn+1;
    }
    public ArrayList<Pawn> getTeamPawns(int color){
        ArrayList<Pawn> P = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            P.add(pawns[(color*4)+i]);
        }
        return P;
    }
    void highlightSpace(int x, int y){
        spaces[y][x].highlight();
        highlightedSpaces.add(new int[]{x,y});
    }
    void hideHighlightedSpaces(){
        for (int[] xy:highlightedSpaces) {
            spaces[xy[1]][xy[0]].hideHighlight();
        }
        highlightedSpaces.clear();
    }
    void highlightSpace(int[] xy){
        spaces[xy[1]][xy[0]].highlight();
        highlightedSpaces.add(xy);
    }
    int checkSpace(int x, int y){
        try{
            return spaces[y][x].getColor();
        } catch (NullPointerException N){
            return -1;
        }
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

    void addMouseClick(MouseListener ML){
        board.button.addMouseListener(ML);
    }

    void removeMouseClick(MouseListener ML){
        board.button.removeMouseListener(ML);
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

