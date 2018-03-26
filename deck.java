import java.util.ArrayList;
import java.util.Random;

/**
 * /- DECK -/
 *   __________
 *  /|        |
 * | | SORRY! |
 * | |        |
 * | |        |
 * | |        |
 * | |________|
 * |/________/
 *
 * CREATES A NEW DECK OF SORRY CARDS THAT CAN BE DRAWN.
 * TODO: Need image processing? if so:
 * TODO:  ->  positioning processes for above i.e. deck.graphicslocation = (x,y)
 * TODO:  ->  need click-ability
 *
 */
public class deck {
    private ArrayList<card> DECK = new ArrayList<>();
    private Random R = new Random(System.currentTimeMillis());
    deck() {
        //adding 5 "one cards"
        card CARD = new card(1);
        for (int i = 0; i < 5; i++) {
            DECK.add(CARD);
        }
        //adding all other cards
        for (int j = 0; j < 12; j++) {
            if (j != 1 && j != 6) {
                for (int i = 0; i < 4; i++) {
                    card C = new card(j);
                    DECK.add(C);
                }
            }

        }
    }

    /**
     * Draw class draws a card randomly and removes it.
     * once empty returns null to indicate the deck is in need of reshuffling.
     */
    public card draw() {
       if(DECK.size()==0) {
           return null;
       }else return DECK.remove(R.nextInt(DECK.size()));

    }
}


/**
 * /- CARD -/
 * __________
 * |        |
 * | SORRY! |
 * |        |
 * |        |
 * |        |
 * |________|
 *
 * HOLDS INFORMATION FOR EACH "CARD" TO BE CALLED AND USED
 * TODO: need image processing???
 * TODO: positioning processes for above i.e. card.graphicslocation = (x,y)
 */
class card {
    private int cardNumber = 0, distanceForward = 0, distanceBackward = 0;
    private boolean leaveStart = false,
            replace = false,
            split = false;
    public boolean drawAgain = false;

    //changes creation based on cards face value
    card(int number) {
        cardNumber = number;
        distanceForward = cardNumber;
        switch (cardNumber) {
            case 1://can be used to get out of start
                leaveStart = true;
                break;
            case 2://can be used to get out of start and draw again
                leaveStart = true;
                drawAgain = true;
                break;
            // no need for 3
            case 4://goes backwards 4
                distanceBackward = 4;
                break;
            // no need for 5
            case 7://seven can be split up
                split = true;
                break;
            // no need for 8
            case 10://can also go backwards 1
                distanceBackward = 1;
                break;
            case 11://can be used to replace or moved!
                replace = true;
                break;
            // no need for 12
            case 0://sorry!
                replace = true;
                break;
        }
    }

    /**
     * checks that a card can be used to move to a given space
     *
     * @param num relative position
     * @return whether or not it can be moved
     */
    public boolean moveCheck(int num) {
        if (num == distanceForward) {
            return true;
        }
        if ((-1) * num == distanceBackward) {
            return true;
        }
        if (num == 0 && replace) {
            return true;
        }
        if(num>0 && split)
            return true;
        distanceForward -= num;
        return false;

    }
}