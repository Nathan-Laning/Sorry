import java.io.Serializable;

/**
 * /-SaveGame-/
 * Used to save and load games utilizing Serializable
 */
class saveGame implements Serializable {
    //variables
    private int player_turn;
    private int userColor;
    private Pawn[] pawn;
    private boolean mean;
    private boolean smart;

    //saving the variables
    saveGame(Pawn[] PAWNS, int player_turn, int userColor, boolean mean, boolean smart) {
        this.mean = mean;
        this.smart = smart;
        this.player_turn = player_turn;
        this.pawn = PAWNS;
        this.userColor = userColor;
    }

    //getters for each saved variable
    int getUserColor() {
        return userColor;
    }

    boolean isSmart() {
        return smart;
    }

    int getPlayer_turn() {
        return player_turn;
    }

    Pawn[] getPawn() {
        return pawn;
    }

    boolean isMean() {
        return mean;
    }


}
