/**
 * /- PAWN -/
 *     _
 *    (_)
 *    / \
 *  _/_ \_
 * (______)
 * Pawn class saves each pawn as instigated and it is only needed to be given
 * an integer to represent its color/team:
 * 0 -> red
 * 3 -> green
 * 1 -> blue
 * 2 -> yellow
 * Movement is also handled using current directions of x and y.
 * -> such as (0,1) is positive y (down on the screen)
 * -> such as (-1,0) is negative x (left on the screen)
 */
class Pawn extends Main {
    private int color;
    private int[] direction,homeEntrance,boardEntrance,homeSpace,finishPosition;
    private int x, y;
    private int originalX, originalY;
    private image PAWN, PAWN_HIGHLIGHT;
    private boolean completed=false,isStart=true;


    /**
     * creates new pawn based on its color, and x,y position in the coordinate  plane
     *
     * @param color 0, 1, 2, or 3 for red, green, blue, or yellow
     * @param x     (0->15)
     * @param y     (0->15)
     */
    Pawn(int color, int x, int y) {
        originalX = x;
        originalY = y;
        this.x = x;
        this.y = y;
        this.color = color;
        int placement = 0;
        switch (color) {
            case 0:
                placement = 3;
                direction = new int[]{0, -1};
                boardEntrance = new int[]{0, 11};
                homeEntrance = new int[]{0, 13};
                finishPosition=new int[] {6,13};
                homeSpace= new int[]{originalX+4,originalY+2};
                break;
            case 1:
                placement = 4;
                direction = new int[]{1, 0};
                boardEntrance = new int[]{4, 0};
                homeEntrance = new int[]{2, 0};
                finishPosition=new int[] {2,6};
                homeSpace= new int[]{originalX-2,originalY+4};
                break;
            case 2:
                placement = 1;
                direction = new int[]{0, 1};
                boardEntrance = new int[]{15, 4};
                homeEntrance = new int[]{15, 2};
                finishPosition=new int[] {9,2};
                homeSpace= new int[]{originalX-4,originalY-2};
                break;
            case 3:
                placement = 2;
                direction = new int[]{-1, 0};
                boardEntrance = new int[]{13, 15};
                homeEntrance = new int[]{11, 15};
                finishPosition=new int[] {13,9};
                homeSpace= new int[]{originalX+2,originalY-4};
                break;
        }
        PAWN = new image("Sorry-pawns.png", 4, placement);
        PAWN_HIGHLIGHT = new image("Sorry-pawns-highlight.png", 4, placement);
        placePawn(x, y);
        PAWN_HIGHLIGHT.hide();
    }


    /**
     * /- PLACE PAWN -/
     *
     * Places pawn at a specified coordinate location
     *
     * @param x (0->15)
     * @param y (0->15)
     */
    void placePawn(int x, int y) {
        PAWN_HIGHLIGHT.move(convertFromCooridinate(x), convertFromCooridinate(y));
        PAWN.move(convertFromCooridinate(x), convertFromCooridinate(y));
        this.x = x;
        this.y = y;
    }

    //NEED TO RETURN PAWN'S POSITION IN ARRAY
    int get_x() {
        return x;
    }

    int get_y() {
        return y;
    }

    public void highlight() {
        PAWN_HIGHLIGHT.show();
    }

    public void hideHighlight() {
        PAWN_HIGHLIGHT.hide();
    }

    void moveToHome(){
        completed=true;
        move(homeSpace[0],homeSpace[1],.2);
    }
    //moves pawn back to its start position
    void moveToStart() {
        isStart=true;
        PAWN_HIGHLIGHT.move(convertFromCooridinate(originalX), convertFromCooridinate(originalY));
        PAWN.move(convertFromCooridinate(originalX), convertFromCooridinate(originalY));
    }
    //moves to initial position
    void EnterBoard(){
        move(boardEntrance[0],boardEntrance[1],.2);
    }


    /**
     * /- MOVE -/
     * moves pawn to specific coordinate position in a specified amount of time
     *
     * @param x       (0->15)
     * @param y       (0->15)
     * @param seconds approx. time it will take
     */
    public void move(int x, int y, double seconds) {
        isStart=false;
        PAWN_HIGHLIGHT.move(convertFromCooridinate(x), convertFromCooridinate(y));
        PAWN_HIGHLIGHT.hide();
        PAWN.threadedMove(convertFromCooridinate(x), convertFromCooridinate(y), seconds);
        this.x = x;
        this.y = y;
    }

    public int getColor() {
        return color;
    }
    public void homeMovement(){
        switch (color) {
            case 0:
                x++;
                break;
            case 1:
                y++;
                break;
            case 2:
                x--;
                break;
            case 3:
                y--;
                break;
        }
    }

    public int[] getFinishPosition() {
        return finishPosition;
    }

    public void determinePosition() {
        //corners
        int X=0;
        int Y=0;
        if(!completed) {
            if (x == homeEntrance[0] && y == homeEntrance[1]){
                homeMovement();
            }else{
                //corners
                if (x == 15 && y == 15) {
                    x--;
                } else if (x == 0 && y == 15) {
                    y--;
                } else if (x == 15 && y == 0) {
                    y++;
                } else if (x == 0 && y == 0) {
                    x++;

                }
                //edges
                else if (x == 15) {
                    y++;
                } else if (x == 0) {
                    y--;
                } else if (y == 15) {
                    x--;
                } else if (y == 0) {
                    x++;
                } else {
                    //homes
                    homeMovement();
                }
            }
        }
    }

    /**
     * /- Move Backwards -/
     * Moves a pawn backwards to a specified amount.
     * @param distance how many spaces to be moved
     */
    public void moveBackward(int distance){
        //if already home do nothing
        if(!completed) {
            for (int i = 0; i < distance; i++) {
//                determineDirection();
                x+=direction[0]*-1;
                y+=direction[1]*-1;
            }
            move(x, y, distance*.15);
        }

    }
    /**
     * /- MOVE FORWARD -/
     *
     * determines local surroundings and saved personal team data to determine the next location to travel
     * in, then increments by one in that direction. To be used in conjunction with some type of moving plot.
     *
     */
    public void moveForward(int distance) {
        //if its at start, move out then continue move forward

        if (isStart) {
            isStart=false;
            EnterBoard();
            distance--;
            moveForward(distance);
        } else {
            for (int i = 0; i < distance; i++) {
                determinePosition();
            }

            if(finishPosition[0]==x&&y==finishPosition[1]){
                moveToHome();
            }else{
                move(x, y, distance * .15);
            }
        }
    }
}