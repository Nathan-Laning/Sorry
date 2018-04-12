import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
 * TODO: How would we want to save/create the board?
 * TODO: need to incorporate drawing cards? or should that be in main.....
 * TODO: need to figure out moving around the board as well, but spaces should probably be sequential?
 */
class gameBoard extends Main {



    gameBoard() {
        deck DECK = new deck();
        image GAMEBOARD=new image("Sorry-board.jpg");
        image drawPile = new image("Sorry-Card-Back-Horizontal.png");
        drawPile.move(2025,1415);

        drawPile.show();
                    clickSpace c = new clickSpace(drawPile);
            c.button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    DECK.draw();
                }
            });



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
 * 4 -> white
 * And empty call will default to a regular space unless told otherwise
 * <p>
 * these spaces can also be populated with pawns utilizing the add and remove pawn method calls
 * as before, an add call takes an int as the team (see above) a remove call needs no input
 * and there will be the test call "isFilled"to see if a space is filled
 * <p>
 * The Space class also populates the pawns when the start is called for the respective color.
 * <p>
 * TODO:need to add positions on the board for items like pawns to navigate to (i.e. pawn.placeAt = (x,y))
 * TODO:    Also need ability to hold multiple pawns in multiple places (i.e. home)
 * TODO:Need click-ability (maybe even hover effect?)
 */
class space extends gameBoard{

    private boolean slide = false, junction = false;
    public boolean holdMultiple = true;
    private int color;
    private ArrayList<Pawn> occupants = new ArrayList<>();
    public ArrayList<space> junctionSpaces = new ArrayList<>();
    private int slideLength;
    private int size=305;
    int x,y;
    clickSpace A;
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
            { {4,1}, {3,5}, {3,5}, {3,5}, {3,5}, {4,1}, {4,1}, {4,1}, {4,1}, {3,4}, {3,4}, {3,4}, {3,4}, {3,4}, {4,1}, {4,1} },
            { {4,1}, {4,0}, {3,6}, {3,3}, {3,3}, {3,3}, {4,0}, {4,0}, {1,2}, {1,2}, {1,2}, {4,0}, {4,0}, {4,0}, {4,0}, {1,5}},
            { {0,4}, {4,0}, {3,6}, {3,3}, {3,3}, {3,3}, {4,0}, {4,0}, {1,2}, {1,2}, {1,2}, {1,6}, {1,6}, {1,6}, {1,6}, {1,5}},
            { {0,4}, {4,0}, {3,6}, {3,3}, {3,3}, {3,3}, {4,0}, {4,0}, {1,2}, {1,2}, {1,2}, {4,0}, {1,3}, {1,3}, {1,3}, {1,5}},
            { {0,4}, {4,0}, {3,6}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {1,3}, {1,3}, {1,3}, {1,5}},
            { {0,4}, {3,2}, {3,2}, {3,2}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {1,3}, {1,3}, {1,3}, {4,1}},
            { {0,4}, {3,2}, {3,2}, {3,2}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,1}},
            { {4,1}, {3,2}, {3,2}, {3,2}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,1}},
            { {4,1}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {2,2}, {2,2}, {2,2}, {4,1}},
            { {4,1}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {2,2}, {2,2}, {2,2}, {1,4}},
            { {4,1}, {0,3}, {0,3}, {0,3}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {2,2}, {2,2}, {2,2}, {1,4}},
            { {0,5}, {0,3}, {0,3}, {0,3}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {2,6}, {4,0}, {1,4}},
            { {0,5}, {0,3}, {0,3}, {0,3}, {4,0}, {0,2}, {0,2}, {0,2}, {4,0}, {4,0}, {2,3}, {2,3}, {2,3}, {2,6}, {4,0}, {1,4}},
            { {0,5}, {0,6}, {0,6}, {0,6}, {0,6}, {0,2}, {0,2}, {0,2}, {4,0}, {4,0}, {2,3}, {2,3}, {2,3}, {2,6}, {4,0}, {1,4}},
            { {0,5}, {4,0}, {4,0}, {4,0}, {4,0}, {0,2}, {0,2}, {0,2}, {4,0}, {4,0}, {2,3}, {2,3}, {2,3}, {2,6}, {4,0}, {4,1}},
            { {4,1}, {4,1}, {2,4}, {2,4}, {2,4}, {2,4}, {2,4}, {4,1}, {4,1}, {4,1}, {4,1}, {2,5}, {2,5}, {2,5}, {2,5}, {4,1} },};
    ArrayList<String> road = new ArrayList<String>();
    ArrayList<String> home = new ArrayList<String>();
    ArrayList<String> start = new ArrayList<String>();
    ArrayList<String> slider = new ArrayList<String>();
    //determine whether it is home
    int is_home(Pawn pawn){
        for (int i=0;i<16;i++){
            for (int j=0;j<16;j++){
                if(whole_board[pawn.get_x()][pawn.get_y()][2]==2){
                    return 1;

                }
            }
        }
        return 0;
    }







    space() {
    }

    //slide creation, see above for use
    space(int color, int slideLength) {
        if (color > 3 || color < 0) {
            System.out.println("Incorrect color for space!");
        } else {
            this.slideLength = slideLength;
            this.color = color;
        }
    }

    Point getPawnPlacement(){
        if(canHoldMultiple()){
            return null;
        }else return new Point(x+40,y+40);
    }
    /**
     * Basic getters, all return values within.
     *
     * @return Item Inquired
     */
    public boolean isSlide() {
        return slide;
    }


    public int getColor() {
        return color;
    }

    public void setHoldMultiple(boolean bool) {
        holdMultiple = bool;
    }

    public boolean canHoldMultiple() {
        return holdMultiple;
    }

    public void addOccupent(Pawn p) {
        occupants.add(p);
    }

    public ArrayList<Pawn> getOccupants() {
        try {
            return occupants;
        } catch (NullPointerException n) {
            System.out.println("NO PAWNS TO BE SEEN IN SPACE");
            return null;
        }
    }

    public Pawn removeOccupant() {
        try {
            return occupants.remove(0);
        } catch (NullPointerException n) {
            System.out.println("NO PAWNS TO BE REMOVED FROM SPACE");
            return null;
        }
    }
}

/**
 * /- PAWN -/
 *     _
 *    (_)
 *    / \
 *  _/_ \_
 * (______)
 * <p>
 * Pawn class saves each pawn as instigated and it is only needed to be given
 * an integer to represent its color/team:
 * 0 -> red
 * 3 -> green
 * 1 -> blue
 * 2 -> yellow
 * <p>
 * TODO: positioning based on board
 * TODO: Need click-ability, But we could just use the space its in instead
 * TODO: Imaging
 */
class Pawn extends Main {
    private int color;

    // returns color pawn
//    Display.image PAWN = DISPLAY.new image("Sorry-pawns.png",4,color++);
    Pawn(int color) {
        this.color = color;
    }

    //NEED TO RETURN PAWN'S POSITION IN ARRAY
    public static int get_x() {
        return 0;
    }
    public static int get_y() {
        return 0;
    }

    public int getColor() {
        return color;
    }
    //need image processing
    //need movement processing for image
}

