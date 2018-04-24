import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
    private boolean Seven = false, AI = false, eleven = false;
    private ArrayList<ArrayList<Pawn>> bumpedPawns = new ArrayList<>();
    private ArrayList<int[]> bumpedLocations = new ArrayList<>();
    ArrayList<Move> moveablePositons = new ArrayList<>();
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
        if (!moveablePositons.isEmpty()) {
            moveablePositons.get(0).start();
        }
    }

    /**
     * /- Mean Move -/
     * prioritizes moves that will harm other players
     */
    void meanMove() {
        for (Move M : moveablePositons) {
            if (containsPawn(M.getFinalPosition()) != -1 && containsPawn(M.getFinalPosition()) != color) {
                M.start();
                return;
            }
        }
    }

    /**
     * /- Nice Move -/
     * prioritizes moves that will not harm moves that will not harm
     */
    void niceMove() {
        for (Move M : moveablePositons) {
            if (containsPawn(M.getFinalPosition()) == -1) {
                M.start();
                return;
            }

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
                    PAWNS.moveToStart();
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
            if (P.isStart()) moveablePositons.add(new Move(P, P.getBoardEntrance()));
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
            moveablePositons.add(M);
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
                moveablePositons.add(new Move(P, X, Y));
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
        for (Move m : moveablePositons) {
            m.highlightPawn();
        }
    }

    void highlightSpaces() {
        for (Move m : moveablePositons) {
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


        void swap(int[] originalXY, int[] newXY) {
            for (Pawn P : G.getPawns()) {
                if (P.getX() == newXY[0] && P.getY() == newXY[1]) {
                    int X = originalXY[0];
                    int Y = originalXY[1];
                    if (!(X == 0 || Y == 0 || X == 15 || Y == 15)) {
                        P.moveToStart();
                    } else {
                        P.move(originalXY[0], originalXY[1], .5);
                    }
                }
            }
        }

        public void start() {
            System.out.print(" ");
            System.out.println(sevenDistance);
            if (eleven) {
                Thread E = new Thread(() -> swap(new int[]{PAWN.getX(), PAWN.getY()}, FinalXY));
                E.start();

            } else {
                Thread B = new Thread(() -> bump(FinalXY));
                B.start();
            }
            checkSlide(PAWN);
            PAWN.move(FinalXY[0], FinalXY[1], .4);
            if (sevenDistance > 0) {
                if (eleven) {

                }
                if (AI) {
                    AI(G, sevenDistance);
                } else {
                    new UserTurn(G, sevenDistance);
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
            G.getPlayer_turn();
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
        for (Move M : moveablePositons) {
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