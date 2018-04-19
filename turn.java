import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /- TURN -/
 * <p>
 * determines all possible pawns that can be moved, and their possible placements for each pawn
 * (within reason) The user/AI can then select which they want to do.
 * is passed a card and a color.
 */
public class turn {
    private ArrayList<Pawn> TEAM_PAWNS = new ArrayList<>();
    private ArrayList<int[]> SPACES_FOR_MOVE = new ArrayList<>();
    private Pawn SELECTED_PAWN,occupant;
    private int color,cardNumber;
    boolean goAgain=false;
    gameBoard G;
    public java.awt.event.MouseListener SELECT_PAWN = new java.awt.event.MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            selectPawn(e.getX(), e.getY());
        }
    };
    public java.awt.event.MouseListener SELECT_POSITION= new java.awt.event.MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            selectSpace(e.getX(), e.getY());
        }
    };

    /**
     * NEW TURN
     * @param G Game Board
     */
    turn(gameBoard G) {
        this.G=G;
        cardNumber = G.draw();
        this.color = G.cycleTeams();
        TEAM_PAWNS = G.getTeamPawns(color);
        findAllMoves();
        clearHighlights();
        highlightPawns();
        G.addMouseClick(SELECT_PAWN);
    }
    void findAllMoves() {
        for (Pawn P : TEAM_PAWNS) {
            P.moveablePositons = new ArrayList<>();
            switch ( cardNumber) {
                case 1:
                    //can be used to get out of start
                    moveFromStart(P);
                    //or to go forward one
                    checkPosition(1,P);
                    break;
                case 2://can be used to get out of start and draw again
                    moveFromStart(P);
                    //tells it to repeat turn
                    goAgain=true;
                    //a second turn
                    //or go 2 and go again
                    checkPosition(2,P);
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
                    checkPosition(5,P);
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
                case 10:
                    /**
                     * can also go backwards 1
                     */
                    //forwards 10
                    checkPosition(10,P);
                    break;
                case 11://can be used to replace or moved!
                    getUnsafePawnLocation(P);
                    checkPosition(11,P);
                    break;
                case 0://sorry!
                    if(P.isStart()) {
                        getUnsafePawnLocation(P);
                    }
                    break;
                // this is 3,8,and 12 all of which can only move forward the distance desired
                default:
                    checkPosition( cardNumber,P);
                    break;
            }
        }
    }

    void bump(int[] xy){
        for (Pawn P : G.getPawns()) {
            if (P.getX() == xy[0] && P.getY() == xy[1] && P.getColor()!=color){
                P.moveToStart();
                return;
            }

        }
    }

    void selectPawn(int x,int y){
        G.hideHighlightedSpaces();
        x=convertToCooridinate(x);
        y=convertToCooridinate(y);
        SPACES_FOR_MOVE.clear();
        for (Pawn P:TEAM_PAWNS) {
            if(P.getX()==x && P.getY()==y){
                SELECTED_PAWN = P;
                SPACES_FOR_MOVE.addAll(P.moveablePositons);
                highlightSpaces();
                G.addMouseClick(SELECT_POSITION);
                G.removeMouseClick(SELECT_PAWN);
                return;
            }
        }
    }
    void selectSpace(int x,int y){
        clearPawnHighlights();
        x=convertToCooridinate(x);
        y=convertToCooridinate(y);
        if(goAgain) {
            for (int i = 0; i < 3; i++) {
                G.cycleTeams();
            }
        }
        for (int[] xy:SPACES_FOR_MOVE) {
            if(x==xy[0]&&y==xy[1]) {
                Thread T = new Thread(()->bump(xy));
                T.start();
                try {
                    T.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SELECTED_PAWN.move(x, y, 1);
                G.removeMouseClick(SELECT_POSITION);
                clearHighlights();
                if(goAgain){

                }
                return;
            }
        }
        clearHighlights();
        highlightPawns();
        G.addMouseClick(SELECT_PAWN);
        G.removeMouseClick(SELECT_POSITION);
    }

    private void clearPawnHighlights() {
        for (Pawn P : G.getPawns()) {
            P.hideHighlight();
        }
    }

    private void clearHighlights(){
        clearPawnHighlights();
        G.hideHighlightedSpaces();
    }
    //checks all pawns that can be moved from start
    void moveFromStart(Pawn P) {
        if (color != containsPawn(P.getBoardEntrance())) {
            if (P.isStart()) P.moveablePositons.add(P.getBoardEntrance());
        }
    }
    //checks the possible position of all cards provided a distance is given
    void checkPosition(int distance, Pawn P){
        //saving old values
        int originalX=P.getX();
        int originalY=P.getY();
        //getting new x and y via recycle pawn movement
        if(distance>0) {
            for (int i = 0; i < distance; i++) {
                P.determinePosition();
            }
        }
        int newX = P.getX();
        int newY = P.getY();
        P.setX(originalX);
        P.setY(originalY);
        if((containsPawn(newX,newY)!=color||(P.getFinishPosition()[0]==newX&&P.getFinishPosition()[1]==newY))
                &&(G.checkSpace(newX,newY)==color||G.checkSpace(newX,newY)!=-1)){
            P.moveablePositons.add(new int[]{newX,newY});
        }

    }
    void getUnsafePawnLocation(Pawn P){
        for (Pawn A :G.getPawns()) {
            //this means its on the ring, and the other team, therefor not safe.
            //only currently makes sense for free-for-all, but teammate function could be easily added
            int X=A.getX();
            int Y=A.getY();
            if((X==0||X==15||Y==0||Y==15) && A.getColor()!=color){
                P.moveablePositons.add(new int[]{X,Y});
            }
        }
    }



    int containsPawn(int x, int y) {
        for (Pawn P : G.getPawns()) {
            if (P.getX() == x && P.getY() == y){
                occupant=P;
                return P.getColor();
            }

        }
        return -1;
    }

    //returns the color of any pawns in a space, or -1 if there are none
    int containsPawn(int[] xy) {
        for (Pawn P : G.getPawns()) {
            if (P.getX() == xy[0] && P.getY() == xy[1]){
                occupant=P;
                return P.getColor();
            }

        }
        return -1;
    }

    //highlights all available pawns to be moved
    void highlightPawns() {
        for (Pawn P : TEAM_PAWNS) {
            if (P.moveablePositons.size() != 0) P.highlight();
        }
    }

    void highlightSpaces(){
        for (int[] xy : SPACES_FOR_MOVE){
            G.highlightSpace(xy);
        }
    }
    public int convertToCooridinate(int num) {
        for (int i = 0; i < 16; i++) {
            double lowerBound = i * ((G.size - (G.ratio * 61)) / 16) + (G.ratio * 30);
            double upperBound = lowerBound + ((G.size - (G.ratio * 61)) / 16) + (G.ratio * 30);
            if (num <= upperBound && num >= lowerBound) return i;
        }
        return 0;
    }

}
