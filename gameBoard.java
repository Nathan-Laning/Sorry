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
//        c.clicked();

//        boolean item_clicked=false;
//        while(!item_clicked){
//            if(!c.isEnabled()){
//                item_clicked=true;
//                System.out.println("clicked");
//            }
//        }



        //creating the board spaces around the board
        ArrayList<space> SPACES = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            //creating home for color
            ArrayList<space> home = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                home.add(new space());
            }
            //allowing final space to hold multiple
            home.get(home.size() - 1).setHoldMultiple(true);
            //creating start for color
            ArrayList<space> start = new ArrayList<>();
            start.add(new space());
            //allowing start space to hold multiple pawns
            start.get(0).setHoldMultiple(true);
            //creating side for color
            ArrayList<space> side = new ArrayList<>();
            side.add(new space(i, 4));//first slide
            side.add(new space(i, home));//home junction, note its index is 1 + i*15
            side.add(new space());//one empty space
            side.add(new space(i, start));//start junction, note its index is 3 + i*15
            //4 empty spaces
            for (int j = 0; j < 4; j++) {
                side.add(new space());
            }
            side.add(new space(i, 5));//slide of length 5
            //6 empty spaces
            for (int j = 0; j < 6; j++) {
                side.add(new space());
            }
            //adding each pawn to their home
            for (int j = 0; j < 4; j++) {
                side.get(3).junctionSpaces.get(0).addOccupent(new pawn(i));
            }
            //adding the total finished side to the entire array (board)
            SPACES.addAll(side);
        }

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
    private ArrayList<pawn> occupants = new ArrayList<>();
    public ArrayList<space> junctionSpaces = new ArrayList<>();
    private int slideLength;
    private int size=305;
    int x,y;
    clickSpace A;

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

    //reserved for after junction spaces
    space(int color, ArrayList<space> E) {
        if (color > 3 || color < 0) {
            System.out.println("Incorrect color for space!");
        } else {
            junction = true;
            junctionSpaces.addAll(E);
        }
    }
    Point getPawnPlacement(){
        if(canHoldMultiple()){
            return null;
        }else return new Point(x+40,y+40);
    }

    void activateClickSpace(int xpos,int ypos){
        x=xpos;
        y=ypos;
        A = new clickSpace(size,size,x,y);
        A.button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               //do something
                A.disable();
            }
        });
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

    public void addOccupent(pawn p) {
        occupants.add(p);
    }

    public ArrayList<pawn> getOccupants() {
        try {
            return occupants;
        } catch (NullPointerException n) {
            System.out.println("NO PAWNS TO BE SEEN IN SPACE");
            return null;
        }
    }

    public pawn removeOccupant() {
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
class pawn extends Main {
    private int color;

    // returns color pawn
//    Display.image PAWN = DISPLAY.new image("Sorry-pawns.png",4,color++);
    pawn(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
    //need image processing
    //need movement processing for image
}

