import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
/**
 * /- TURN -/
 * <p>
 * determines all possible pawns that can be moved, and their possible placements for each pawn
 * (within reason) The user/AI can then select which they want to do.
 * is passed a card and a color.
 */
class turn {
    ArrayList<Pawn> TEAM_PAWNS = new ArrayList<>();
    Pawn SELECTED_PAWN;
    int color;
    int cardNumber;
    boolean goAgain = false;

    private boolean Seven = false, AI = false,eleven = false;
    private ArrayList<ArrayList<Pawn>> bumpedPawns = new ArrayList<>();
    private ArrayList<int[]> bumpedLocations = new ArrayList<>();
    ArrayList<Move> moveablePositions = new ArrayList<>();

    gameBoard G;

    void findAllMoves() {
        for (Pawn P : TEAM_PAWNS) {
            switch (cardNumber) {
                case 1:
                    //can be used to get out of start
                    moveFromStart(P);
                    //or to go forward one
                    checkPosition(1, P);
                    break;
                case 2://can be used to get out of start and draw again
                    moveFromStart(P);
                    //tells it to repeat turn
                    goAgain = true;
                    //a second turn
                    //or go 2 and go again
                    checkPosition(2, P);
                    break;
                case 4://goes backwards 4
                    checkPosition(-4, P);
                    break;
                case 5:
                    checkPosition(5, P);
                    break;
                case 7://seven can be split up
                    Seven = true;
                    checkPosition(7, P);
                    checkPosition(6, P);
                    checkPosition(5, P);
                    checkPosition(4, P);
                    checkPosition(3, P);
                    checkPosition(2, P);
                    checkPosition(1, P);
                    break;
                case 10:
                    checkPosition(-1, P);
                    //forwards 10
                    checkPosition(10, P);
                    break;
                case 11://can be used to replace or moved!
                    getUnsafePawnLocation(P);
                    checkPosition(11, P);
                    eleven = true;
                    break;
                case 0://sorry!
                    if (P.isStart()) {
                        getUnsafePawnLocation(P);
                    }
                    break;
                // this is 3,8,and 12 all of which can only move forward the distance desired
                default:
                    checkPosition(cardNumber, P);
                    break;
            }
        }
    }

    //Default AI Run, determine will be overwritten by each difficulty
    void AI(gameBoard g) {
        AI = true;
        G = g;
        if (!goAgain) {
            this.color = G.getPlayer_turn();
            TEAM_PAWNS = G.getTeamPawns(color);
        }
        if (!Seven) {
            cardNumber = G.draw();
            findAllMoves();
        }

        clearHighlights();
        highlightPawns();
        determineMove();
    }

    //same as default, but is used for the special case 7
    private void AI(gameBoard g, int distance) {
        G = g;
        if (distance > 0) {
            for (Pawn PAWN : TEAM_PAWNS) {
                checkPosition(distance, PAWN);
            }
        }
        clearHighlights();
        highlightPawns();
        determineMove();
    }

    void determineMove() {
        //empty to be replaced further on by various iterations
    }

    /**
     * /- Dumb Move -/
     * commits first move without thought
     */
    void dumbMove() {
        int moveable = moveablePositions.size();
        int moveChoice = rand_gen(moveable);
        if (!moveablePositions.isEmpty()) {
            moveablePositions.get(moveChoice).start();
        }
    }
    /**
     * /-Smart Move-/
     * prioritizes:
     * 1. getting all pawns out on the board
     * 2. getting all pawns ultimately closest to home
     */
    void smartMove() {
        int count = 0;
        // 16*16 = 256, so 257 is technically impossibly far
        double minDiff=257;
        Move finalMove = null;
        for (Move m : moveablePositions) {
            //getting all pawns leaving home and prioritizing them
            if (m.getPawn().getX() == m.getPawn().getStartPosition()[0] && m.getPawn().getY() == m.getPawn().getStartPosition()[1]) {
                m.start();
                return;
            }
            if (m.getPawn().getX() == m.getPawn().getBoardEntrance()[0] && m.getPawn().getX() == m.getPawn().getBoardEntrance()[1]) {
                m.start();
                return;
            }

            //check all pawn positions in relation to home -- .getHomeEntrance()
            //calculate positions and move the pawn furthest from home IF it gets it closer

            //getting the distance of each using
            // sqrt((x1-x2)^2+(y1-y2)^2)
            // current position of the pawn in reference to the board entrance
            int pawnX = m.getPawn().getX();
            int pawnY = m.getPawn().getY();
            //if its in the ring
            if(pawnX==15||pawnX==0||pawnY==0||pawnY==15) {
                int current_pawnX = (m.getPawn().getHomeEntrance()[0] - pawnX);
                int current_pawnY = (m.getPawn().getHomeEntrance()[1] - pawnY);
                double current_pawnD = Math.sqrt((current_pawnX * current_pawnX) + (current_pawnY * current_pawnY));
                // final distance after hypothetical move is made
                int final_pawnX = (m.getPawn().getHomeEntrance()[0] - m.FinalXY[0]);
                int final_pawnY = (m.getPawn().getHomeEntrance()[0] - m.FinalXY[0]);
                double final_pawnD = Math.sqrt((final_pawnX * final_pawnX) + (final_pawnY * final_pawnY));
                //the difference of the two
                double distance = current_pawnD - final_pawnD;
                //if its the best move, prioritize it
                if (distance < minDiff) {
                    minDiff = distance;
                    finalMove = m;
                }
            }else{
                m.start();
            }
        }
        try {
            finalMove.start();
        }catch (NullPointerException N){}
    }

    /**
     * /- Mean Move -/
     * prioritizes moves that will harm other players
     */
    void meanMove() {
        ArrayList<Move> MeanMoves = new ArrayList<>();
        for (Move M : moveablePositions) {
            if (containsPawn(M.getFinalPosition()) != -1 && containsPawn(M.getFinalPosition()) != color) {
                MeanMoves.add(M);
                return;
            }
        }
        if(MeanMoves.size()!=0){
            moveablePositions.clear();
            moveablePositions.addAll(MeanMoves);
        }
    }

    /**
     * /- Nice Move -/
     * prioritizes moves that will not harm moves that will not harm
     */
    void niceMove() {
        ArrayList<Move> NiceMoves = new ArrayList<>();
        for (Move M : moveablePositions) {
            if (containsPawn(M.getFinalPosition()) == -1) {
                NiceMoves.add(M);
                return;
            }
        }
        if(NiceMoves.size()!=0){
            moveablePositions.clear();
            moveablePositions.addAll(NiceMoves);
        }
    }

    /**
     * /- Bump -/
     * checks the newly moved pawns location for current residents to move home
     *
     * @param xy position
     */
    void bump(int[] xy) {
        for (int i = 0; i < bumpedLocations.size(); i++) {
            if (bumpedLocations.get(i)[0] == xy[0] && bumpedLocations.get(i)[1] == xy[1]) {
                for (Pawn PAWNS : bumpedPawns.get(i)) {
                    if(PAWNS!=SELECTED_PAWN) PAWNS.moveToStart();
                }

            }
        }
        for (Pawn P : G.getPawns()) {
            if (P.getX() == xy[0] && P.getY() == xy[1] && P.getColor() != color) {
                P.moveToStart();
                return;
            }
        }
    }
    private int rand_gen(int bound){
        int num = ThreadLocalRandom.current().nextInt(0, bound);
        return num;
    }

    void clearPawnHighlights() {
        for (Pawn P : G.getPawns()) {
            P.hideHighlight();
        }
    }

    void clearHighlights() {
        clearPawnHighlights();
        G.hideHighlightedSpaces();
    }

    //checks all pawns that can be moved from start
    void moveFromStart(Pawn P) {
        if (color != containsPawn(P.getBoardEntrance())) {
            if (P.isStart()) moveablePositions.add(new Move(P, P.getBoardEntrance()));
        }
    }

    //checks the possible position of all cards provided a distance is given
    void checkPosition(int distance, Pawn P) {
        //saving old values
        int originalX = P.getX();
        int originalY = P.getY();
        //getting new x and y via recycle pawn movement
        if (distance > 0) {
            for (int i = 0; i < distance; i++) {
                P.determinePosition();
            }
        }
        checkSlide(P);
        if (distance < 0) {
            if(!P.isStart()){
                for (int i = distance; i < 0; i++) {
                    P.determineNegativePosition();
                }
            }
        }
        int newX = P.getX();
        int newY = P.getY();
        P.setX(originalX);
        P.setY(originalY);
        if ((containsPawn(newX, newY) != color || (P.getFinishPosition()[0] == newX && P.getFinishPosition()[1] == newY))
                && (G.checkSpace(newX, newY) == color || G.checkSpace(newX, newY) != -1)) {
            Move M = new Move(P, newX, newY);
            if (Seven) M.setSeven(distance);
            moveablePositions.add(M);
        } else {
            P.setX(originalX);
            P.setY(originalY);
        }
    }

    /**
     * Gets all unsafe pawns for sorry and 11
     *
     * @param P Pawn to be moved
     */
    void getUnsafePawnLocation(Pawn P) {
        for (Pawn A : G.getPawns()) {
            //this means its on the ring, and the other team, therefor not safe.
            //only currently makes sense for free-for-all, but teammate function could be easily added
            int X = A.getX();
            int Y = A.getY();
            if ((X == 0 || X == 15 || Y == 0 || Y == 15) && A.getColor() != color) {
                moveablePositions.add(new Move(P, X, Y));
            }
        }
    }

    /**
     * checks if pawns are in the sliding pawn
     *
     * @param P Pawn being moved across slide
     */
    void checkSlide(Pawn P) {
        int Y = P.getY();
        int X = P.getX();
        try {
            int slide = G.getSpace(X, Y).getSlide();
            if (G.getSpace(X, Y).getColor() != this.color) {
                ArrayList<Pawn> pawns = new ArrayList<>();
                for (int i = 0; i < slide; i++) {
                    P.determinePosition();
                    pawns.add(bumpablePawns(P.getX(), P.getY()));
                }
                if (!pawns.isEmpty()) {
                    bumpedPawns.add(pawns);
                    bumpedLocations.add(new int[]{X, Y});
                }
            }
        } catch (NullPointerException N) {

        }
    }


    /**
     * checks pawns
     *
     * @param x x
     * @param y y
     * @return Pawn being bumped
     */
    Pawn bumpablePawns(int x, int y) {
        for (Pawn P : G.getPawns()) {
            if (P.getX() == x && P.getY() == y) {
                return P;
            }
        }
        return null;
    }

    //returns the color of any pawns in a space, or -1 if there are none
    int containsPawn(int x, int y) {
        for (Pawn P : G.getPawns()) {
            if (P.getX() == x && P.getY() == y) {
                return P.getColor();
            }

        }
        return -1;
    }

    //returns the color of any pawns in a space, or -1 if there are none
    int containsPawn(int[] xy) {
        for (Pawn P : G.getPawns()) {
            if (P.getX() == xy[0] && P.getY() == xy[1]) {
                return P.getColor();
            }

        }
        return -1;
    }

    //highlights all available pawns to be moved
    void highlightPawns() {
        for (Move m : moveablePositions) {
            m.highlightPawn();
        }
    }

    void highlightSpaces() {
        for (Move m : moveablePositions) {
            if (m.getPawn() == SELECTED_PAWN) m.highlightSpace();
        }
    }

    //converts to co-ordinates
    public int convertToCooridinate(int num) {
        for (int i = 0; i < 16; i++) {
            double lowerBound = i * ((Display.size - (Display.ratio * 61)) / 16) + (Display.ratio * 30);
            double upperBound = lowerBound + ((Display.size - (Display.ratio * 61)) / 16) + (Display.ratio * 30);
            if (num <= upperBound && num >= lowerBound) return i;
        }
        return 0;
    }

    /**
     * /- Move -/
     *       _
     *   ---(_)
     *  --- / \
     * ---_/_ \_
     *   (______)
     * <p>
     * Saves information on move-able objects such as the pawn to be moved, where it goes, all pawns it interacts with
     * and committing the move itself
     */
    class Move {
        int[] CurrentXY;
        int[] FinalXY;
        int sevenDistance = 0;
        Pawn PAWN;

        Move(Pawn P, int x, int y) {
            FinalXY = new int[]{x, y};
            PAWN = P;
            CurrentXY = new int[]{P.getX(), P.getY()};
        }

        Move(Pawn P, int[] xy) {
            FinalXY = xy;
            PAWN = P;
            CurrentXY = new int[]{P.getX(), P.getY()};

        }

        void highlightPawn() {
            PAWN.highlight();
        }

        void highlightSpace() {
            G.highlightSpace(FinalXY);
        }

        int[] getFinalPosition() {
            return FinalXY;
        }


        void swap(Pawn PAWN, int[] newXY) {
            for (Pawn P : G.getPawns()) {
                if (P.getX() == newXY[0] && P.getY() == newXY[1]) {
                    int X = PAWN.getX();
                    int Y = PAWN.getY();
                    if (!(X == 0 || Y == 0 || X == 15 || Y == 15)) {
                        P.moveToStart();
                    } else {
                        P.move(X, Y, .5);
                    }
                    Thread T = new Thread(()->PAWN.move(newXY[0],newXY[1],.4));
                    T.start();
                    try {
                        T.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

        public void start() {
            if (eleven) {
                checkSlide(PAWN);
                swap(PAWN, FinalXY);

            } else {
                Thread BUMP = new Thread(() -> bump(FinalXY));
                BUMP.start();
                checkSlide(PAWN);
                PAWN.move(FinalXY[0], FinalXY[1], .4);
                if (sevenDistance > 0) {
                    if (AI) {
                        AI(G, sevenDistance);
                    } else {
                        new UserTurn(G, sevenDistance);
                    }
                }
            }
        }

        public void setSeven(int distance) {
            sevenDistance = 7 - distance;
        }


        Pawn getPawn() {
            return PAWN;
        }
    }
}

/**
 * /- User Turn -/
 * Allows user full control of selected options using various click events
 * can be called from game board when necessary
 */
class UserTurn extends turn {
    private java.awt.event.MouseListener SELECT_PAWN = new java.awt.event.MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            selectPawn(e.getX(), e.getY());
        }
    };
    private java.awt.event.MouseListener SELECT_POSITION = new java.awt.event.MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            selectSpace(e.getX(), e.getY());
        }
    };

    UserTurn(gameBoard g) {
        if (!goAgain) {
            G = g;
            this.color= G.getPlayer_turn();
            TEAM_PAWNS = G.getTeamPawns(color);
        }
        cardNumber = G.draw();
        findAllMoves();
        clearHighlights();
        highlightPawns();
        G.addMouseClick(SELECT_PAWN);
    }

    UserTurn(gameBoard g, int distance) {
        G = g;
        TEAM_PAWNS = G.getTeamPawns(color);
        for (Pawn PAWN : TEAM_PAWNS) {
            checkPosition(distance, PAWN);
        }
        clearHighlights();
        highlightPawns();
        G.addMouseClick(SELECT_PAWN);
    }

    private void selectPawn(int x, int y) {
        G.hideHighlightedSpaces();
        x = convertToCooridinate(x);
        y = convertToCooridinate(y);
        for (Pawn P : TEAM_PAWNS) {
            if (P.getX() == x && P.getY() == y) {
                SELECTED_PAWN = P;
                highlightSpaces();
                G.addMouseClick(SELECT_POSITION);
                G.removeMouseClick(SELECT_PAWN);
                return;
            }
        }
    }

    private void selectSpace(int x, int y) {
        clearPawnHighlights();
        x = convertToCooridinate(x);
        y = convertToCooridinate(y);
        for (Move M : moveablePositions) {
            int[] xy = M.getFinalPosition();
            if (x == xy[0] && y == xy[1]) {
                Thread B = new Thread(() -> bump(xy));
                B.start();
                M.start();
                G.removeMouseClick(SELECT_POSITION);
                clearHighlights();
                return;
            }
        }
        clearHighlights();
        highlightPawns();
        G.addMouseClick(SELECT_PAWN);
        G.removeMouseClick(SELECT_POSITION);
    }
}
/**
 * Smart & Nice AI
 */
class SmartNiceAITurn extends turn{
    SmartNiceAITurn(gameBoard G){
        AI(G);
    }
    void determineMove(){
        niceMove();
        smartMove();
    }
}
/**
 * Smart & Mean aI
 */
class SmartMeanAITurn extends turn{
    SmartMeanAITurn(gameBoard G){
        AI(G);
    }
    void determineMove(){
        meanMove();
        smartMove();
    }

}

/**
 * Dumb & Nice AI
 */
class DumbNiceAITurn extends turn {
    DumbNiceAITurn(gameBoard G) {
        AI(G);
    }

    @Override
    void determineMove() {
        niceMove();
        dumbMove();
    }
}

/**
 * Dumb & Mean AI
 */
class DumbMeanAITurn extends turn {
    DumbMeanAITurn(gameBoard G) {
        AI(G);
    }

    @Override
    void determineMove() {
        meanMove();
        dumbMove();
    }


}