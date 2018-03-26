import javax.swing.*;
import java.util.ArrayList;

/**
 * /- GAMEBOARD -/
 *      _________________
 *     /                /
 *    /                /
 *   /     SORRY!     /
 *  /                /
 * /________________/
 *
 * saves information about peg location, the game board as a whole,
 * and whether or not moves can be made
 * there is a different board for each color
 * the information is saved for each board as its own class below.
 *
 * TODO: How would we want to save/create the board?
 * TODO: need to incorporate drawing cards? or should that be in main.....
 * TODO: need to figure out moving around the board as well, but spaces should probably be sequential.
 *
 */
public class gameBoard {

}
/**
 * /- SPACE -/
 * ________
 * |      |
 * |(type)|
 * |______|
 *
 * Space class represents the individual spaces that can be populated by a peg
 * there are four types, normal (default call), "slide", "start", and "home".
 * creating these is as simple as passing the strings seen above ^
 * and passing which team it will be associated with 0,1,2, or 3 where
 * 0 -> red
 * 1 -> blue
 * 2 -> yellow
 * 3 -> green
 * And empty call will default to a regular space unless told otherwise
 *
 * these spaces can also be populated with pawns utilizing the add and remove pawn method calls
 * as before, an add call takes an int as the team (see above) a remove call needs no input
 * and there will be the test call "isFilled"to see if a space is filled
 *
 * The Space class also populates the pawns when the start is called for the respective color.
 *
 * TODO:need to add positions on the board for items like pawns to navigate to (i.e. pawn.placeAt = (x,y))
 * TODO:    Also need ability to hold multiple pawns in multiple places (i.e. home)
 * TODO:Need click-ability (maybe even hover effect?)
 *
 */
class space{

    private boolean slide=false,safe=false;
    private int color;
    private ArrayList<pawn> occupants;
    //Space creation, see above for use
    space(String type,int color){
        if(color > 3||color<0) System.out.println("Incorrect color for space!");
        else{
            this.color=color;
            switch (type){
                case "slide":
                    slide=true;
                    break;
                case "start":
                    for (int i = 0; i < 4; i++) {
                        occupants.add(new pawn(color));
                    }
                    safe=true;
                    break;
                case "home":
                    safe=true;
                    break;
                default:
                    System.out.println("Incorrect tile information!");
                    break;

            }
        }
    }

    /**
     * Basic getters, all return values within.
     * @return Item Inquired
     */
    public boolean isSlide() {
        return slide;
    }
    public boolean isSafe() {
        return safe;
    }
    public int getColor() {
        return color;
    }
    public ArrayList<pawn> getOccupants() {
        return occupants;
    }
    public pawn removeOccupant(){
        try {
            return occupants.remove(0);
        }catch (NullPointerException n){
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
 *
 * Pawn class saves each pawn as instigated and it is only needed to be given
 * an integer to represent its color/team:
 * 0 -> red
 * 1 -> blue
 * 2 -> yellow
 * 3 -> green
 *
 * TODO: positioning based on board
 * TODO: Need click-ability, But we could just use the space its in instead
 * TODO: Imaging
 *
 */
class pawn{
    private int color;
    // returns color pawn
    pawn(int color){
        this.color=color;
    }
    public int getColor(){
        return color;
    }
    //need image processing
    //need movement processing for image
}