import java.awt.*;
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
 * TODO: How would we want to save/create the board?
 * TODO: need to incorporate drawing cards? or should that be in main.....
 * TODO: need to figure out moving around the board as well, but spaces should probably be sequential?
 */
class gameBoard extends Main {
    Pawn[] pawns = new Pawn[16];
    int x,y;
    public java.awt.event.MouseListener M = new java.awt.event.MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            Clicked(e.getX(),e.getY());
        }
        };
    protected int[][][] whole_board = {
            { {4,1}, {3,5}, {3,5}, {3,5}, {3,5}, {4,1}, {4,1}, {4,1}, {4,1}, {3,4}, {3,4}, {3,4}, {3,4}, {3,4}, {4,1}, {4,1}},
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
            { {4,1}, {4,1}, {2,4}, {2,4}, {2,4}, {2,4}, {2,4}, {4,1}, {4,1}, {4,1}, {4,1}, {2,5}, {2,5}, {2,5}, {2,5}, {4,1}},};
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

    gameBoard() {
        deck DECK = new deck();
        image GAMEBOARD=new image("Sorry-board.jpg");
        image drawPile = new image("Sorry-Card-Back-Horizontal.png");
        drawPile.move(2025,1415);
        clickSpace board = new clickSpace(GAMEBOARD);

        board.disable();
        int player_turn = whosTurn();
        drawPile.show();
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
                         */
                        break;
                    case 0://sorry!

                        break;
                }
            }


        });
            int[][] pos = {
                    {2,10},{2,11},{2,12},{3,11},
                    {3,2},{4,2},{5,2},{4,3},
                    {13,3},{13,4},{13,5},{12,4},
                    {12,13},{11,13},{10,13},{11,12}
            };
            int k=0;
        for (int j = 0; j < 16; j++) {
            pawns[j]=new Pawn(k,pos[j][0],pos[j][1]);
            k=(j+1)/4;
        }
    }

    private static int whosTurn(){
        Random rand = new Random();
        int num = rand.nextInt(4);
        return num;
    }


    public void Clicked(int x,int y) { ;
        double clicked_space_x=0;
        double clicked_space_y=0;
        for (int i = 0; i < 16; i++) {
            double lowerBound = i*(size/16)+(ratio*60);
            double upperBound = lowerBound+(size/16)+(ratio*60);
            if(lowerBound<x && upperBound>x) clicked_space_x=i;
            if(lowerBound<y && upperBound>y) clicked_space_y=i;
        }
        System.out.println(clicked_space_y);
        pawns[0].move((int)clicked_space_x,(int)clicked_space_y,1);
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
    private int x,y;
    private int originalX,originalY;

    // returns color pawn
    image PAWN=null;
    Pawn(int color,int x, int y) {
        originalX=x;
        originalY=y;
        this.x=x;
        this.y=y;

        this.color = color;
        int placement=0;
        switch (color){
            case 0:
                placement=3;
                break;
            case 1:
                placement=4;
                break;
            case 2:
                placement=1;
                break;
            case 3:
                placement=2;
                break;
        }
        PAWN = new image("Sorry-pawns.png",4,placement);
        placePawn(x,y);
    }

    void placePawn(int x, int y){
        PAWN.move((x*300)+120+2*x,(y*300)+120+2*y);
        this.x=x;
        this.y=y;
    }
    //NEED TO RETURN PAWN'S POSITION IN ARRAY
    public static int get_x() {
        return 0;
    }
    public static int get_y() {
        return 0;
    }

    public void move(int x, int y, double seconds){
        PAWN.move((x*302)+120,(y*302)+120,1);
        this.x=x;
        this.y=y;
    }
    public int getColor() {
        return color;
    }
    //need image processing
    //need movement processing for image
}

