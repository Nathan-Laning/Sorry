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
public class deck extends Main{
    private ArrayList<card> DECK = new ArrayList<>();
    private Random R = new Random(System.currentTimeMillis());

    // image location
    private image[] deckImages = new image[13];

    deck() {

        //adding 5 "one cards"
        card CARD = new card(1);
        for (int i = 0; i < 5; i++) {
            DECK.add(CARD);
        }
        //adding all other cards
        for (int j = 0; j < 12; j++) {
            if (j != 1 && j != 6&& j!=9) {
                for (int i = 0; i < 4; i++) {
                    card C = new card(j);
                    DECK.add(C);
                }
            }

        }
        loadDeckImages();
    }
    //loads all images to cutback on loading in-game
    private void loadDeckImages(){
        String name = "Sorry-cards-Enlarged-Shadow.png";
        deckImages[0]=new image(name,12,12);
        deckImages[1]=new image(name,12,2);
        deckImages[2]=new image(name,12,3);
        deckImages[3]=new image(name,12,4);
        deckImages[4]=new image(name,12,5);
        deckImages[5]=new image(name,12,6);
        deckImages[7]=new image(name,12,7);
        deckImages[8]=new image(name,12,8);
        deckImages[10]=new image(name,12,9);
        deckImages[11]=new image(name,12,10);
        deckImages[12]=new image(name,12,11);
        for (int j = 0; j < deckImages.length; j++) {
            if(j!=6&&j!=9){
                deckImages[j].move(1990,1820);
                deckImages[j].hide();
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
       }else {
           card newCard = DECK.remove(R.nextInt(DECK.size()));
           System.out.println(newCard.cardNumber);
           deckImages[newCard.cardNumber].show();
           return newCard;
       }

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
    public int cardNumber = 0;
    private int distanceForward = 0, distanceBackward = 0;
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
            case 5:
                break;
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
            case 12:
                break;
            case 0://sorry!
                replace = true;
                break;
        }

//        cardFace=DISPLAY.new image("Sorry_cards.png",12,individual);
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