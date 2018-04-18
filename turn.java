import java.util.ArrayList;

/**
 * /- TURN -/
 * <p>
 * determines all possible pawns that can be moved, and their possible placements for each pawn
 * (within reason) The user/AI can then select which they want to do.
 * is passed a card and a color.
 */
public class turn{
    ArrayList<Pawn> TEAM_PAWNS = new ArrayList<>();
    private static final deck DECK = new deck();
    int color;
    card CARD;
    gameBoard G;

    turn(int color,gameBoard G) {

        this.G=G;
        CARD = DECK.draw();
        this.color = color;
        TEAM_PAWNS = G.getTeamPawns(color);
        findAllMoves();
        clearHighlights();
        highlightPawns();
        highlightSpaces();
    }
    void clearHighlights(){
        for (Pawn P : G.getPawns()) {
            P.hideHighlight();
        }
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
        //saving old values incase it failes
        int originalX=P.getX();
        int originalY=P.getY();
        if(distance>0) {
            for (int i = 0; i < distance; i++) {
                P.determinePosition();
            }
        }
        int newX = P.getX();
        int newY = P.getY();
        if(containsPawn(newX,newY)!=color&&(G.checkSpace(newX,newY)==color||G.checkSpace(newX,newY)!=-1)){
            P.moveablePositons.add(new int[]{newX,newY});
        }
            P.setX(originalX);
            P.setY(originalY);



    }

    void findAllMoves() {
        for (Pawn P : TEAM_PAWNS) {
            P.moveablePositons = new ArrayList<>();
            switch (CARD.cardNumber) {
                case 1:
                    //can be used to get out of start
                    moveFromStart(P);
                    //or to go forward one
                    checkPosition(1,P);
                    break;
                case 2://can be used to get out of start and draw again
                    moveFromStart(P);
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
                    //can also go backwards 1
                    //forwards 10
                    checkPosition(10,P);
                    break;
                case 11://can be used to replace or moved!
                    /**
                     *Move 11 spaces forward, or switch the places of one of the player's own pawns and an opponent's pawn.
                     *  A player that cannot move 11 spaces is not forced to switch and instead can forfeit the turn.
                     *  An 11 cannot be used to switch a pawn that is in a Safety Zone.
                     *
                     *  if switch:
                     */
                    checkPosition(11,P);
                    break;
                case 0://sorry!
                    //swap with anyone
                    break;
                    // this is 3,8,and 12 all of which can only move forward the distance desired
                default:
                    checkPosition(CARD.cardNumber,P);
                    break;
            }
        }
    }

    //looks at all possible moves for each pawns and determines if it can be moved
    void DetermineMoveable() {
//            if(P.isCompleted()) TEAM_PAWNS.remove(P);
//            else{
//                if(P.isStart()){
//                    if(CARD.isLeaveStart()&&(color!=containsPawn(P.getBoardEntrance()))){//need to check that ths works this way
//                        //need move here for out of start
//                    }else{
//
//                    }
//                }
//            }

    }

    //returns the color of any pawns in a space, or -1 if there are none
    int containsPawn(int x, int y) {
        for (Pawn P : G.getPawns()) {
            if (P.getX() == x && P.getY() == y) return P.getColor();
        }
        return -1;
    }

    //returns the color of any pawns in a space, or -1 if there are none
    int containsPawn(int[] xy) {
        for (Pawn P : G.getPawns()) {
            if (P.getX() == xy[0] && P.getY() == xy[1]) return P.getColor();
        }
        return -1;
    }

    //highlights all available pawns to be moved
    void highlightPawns() {
        for (Pawn P : TEAM_PAWNS) {
            if (P.moveablePositons.size() != 0) P.highlight();
        }
    }

    //un-highlights all previous highlighted pawns.
    void hideHighlightPawns() {
        for (Pawn P : TEAM_PAWNS) {
            P.hideHighlight();
        }
    }
    void highlightSpaces(){
        for (Pawn P : TEAM_PAWNS) {
            for(int[] xy: P.moveablePositons){
                G.highlightSpace(xy);
            }
        }
    }

    void HidehighlightSpaces(){

    }

}
