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
 * <p>
 * CREATES A NEW DECK OF SORRY CARDS THAT CAN BE DRAWN.
 */
public class deck extends Main {
    private ArrayList<card> DECK = new ArrayList<>();
    private Random R = new Random(System.currentTimeMillis());
    image discardPile = null;
    // image location
    private image[] deckImages = new image[13];

    deck() {
        discardPile = new image("Sorry-Card-Back-Horizontal.png");
        discardPile.move((int) (1010 * ratio), (int) (1490 * ratio));
        shuffle();
        loadDeckImages();
    }

    //refreshes the deck with all new cards, but skips re-rendering the images.
    void shuffle() {
        discardPile.hide();
        //adding 5 "one cards"
        card CARD = new card(1);
        for (int i = 0; i < 5; i++) {
            DECK.add(CARD);
        }
        //adding all other cards
        for (int j = 0; j < 13; j++) {
            if (j != 1 && j != 6 && j != 9) {
                for (int i = 0; i < 4; i++) {
                    card C = new card(j);
                    DECK.add(C);
                }
            }

        }
    }

    //loads all images to cutback on loading in-game
    private void loadDeckImages() {
        String name = "Sorry-cards-Enlarged-Shadow.png";
        deckImages[0] = new image(name, 12, 12);
        deckImages[1] = new image(name, 12, 2);
        deckImages[2] = new image(name, 12, 3);
        deckImages[3] = new image(name, 12, 4);
        deckImages[4] = new image(name, 12, 5);
        deckImages[5] = new image(name, 12, 6);
        deckImages[7] = new image(name, 12, 7);
        deckImages[8] = new image(name, 12, 8);
        deckImages[10] = new image(name, 12, 9);
        deckImages[11] = new image(name, 12, 10);
        deckImages[12] = new image(name, 12, 11);
        for (int j = 0; j < deckImages.length; j++) {
            if (j != 6 && j != 9) {
                deckImages[j].move((int) (995 * ratio), (int) (ratio * 910));
                deckImages[j].hide();
            }
        }
    }

    /**
     * Draw class draws a card randomly and removes it.
     * once empty returns null to indicate the deck is in need of reshuffling.
     */
    public card draw() {
        System.out.println(DECK.size());
        if (DECK.size() == 0) {

            shuffle();
        }
        if (DECK.size() == 44) {
            discardPile.show();
        }
        card newCard = DECK.remove(R.nextInt(DECK.size()));
        deckImages[newCard.cardNumber].show();
        return newCard;
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
 * <p>
 * HOLDS INFORMATION FOR EACH "CARD" TO BE CALLED AND USED
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
                //no need for 12
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
        if (num > 0 && split)
            return true;
        distanceForward -= num;
        return false;

    }

    public boolean isLeaveStart() {
        return leaveStart;
    }
}