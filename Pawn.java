
import java.util.ArrayList;

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
class Pawn extends Display{
    public ArrayList<int[]> moveablePositons=new ArrayList<>();
    private int color;
    private int[] homeEntrance,boardEntrance,homeSpace,finishPosition;
    private int x, y;
    private int originalX, originalY;
    private Display.image PAWN, PAWN_HIGHLIGHT;
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
        int individualNumber = 1;
        /**
         * for each color there is:
         * individualNumber -> the individual within the image pawn.png
         * boardEntrance -> where the pawn goes when it leaves home
         * homeEntrance -> the entrance to the pawns home(on the ring)
         * finishPosition -> what space is considered finished pos.
         * homeSpace -> where it will placed when it reaches home
         */
        switch (color) {
            case 0:
                individualNumber = 3;
                boardEntrance = new int[]{0, 11};
                homeEntrance = new int[]{0, 13};
                finishPosition=new int[] {6,13};
                homeSpace= new int[]{originalX+4,originalY+2};
                break;
            case 1:
                individualNumber = 4;
                boardEntrance = new int[]{4, 0};
                homeEntrance = new int[]{2, 0};
                finishPosition=new int[] {2,6};
                homeSpace= new int[]{originalX-2,originalY+4};
                break;
            case 2:
                boardEntrance = new int[]{15, 4};
                homeEntrance = new int[]{15, 2};
                finishPosition=new int[] {9,2};
                homeSpace= new int[]{originalX-4,originalY-2};
                break;
            case 3:
                individualNumber = 2;
                boardEntrance = new int[]{11, 15};
                homeEntrance = new int[]{13, 15};
                finishPosition=new int[] {13,9};
                homeSpace= new int[]{originalX+2,originalY-4};
                break;
        }
        PAWN = new image("Sorry-pawns.png", 4, individualNumber);
        PAWN_HIGHLIGHT = new image("Sorry-pawns-highlight.png", 4, individualNumber);
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


    public void highlight() {
        PAWN_HIGHLIGHT.show();
    }

    public void hideHighlight() {
        PAWN_HIGHLIGHT.hide();
    }

    //moves pawn back to its start position
    synchronized void moveToStart() {
        isStart=true;
        PAWN_HIGHLIGHT.move(convertFromCooridinate(originalX), convertFromCooridinate(originalY));
        PAWN.threadedMove(convertFromCooridinate(originalX), convertFromCooridinate(originalY),1.2);
        x = originalX;
        y=originalY;
    }


    /**
     * /- MOVE -/
     * moves pawn to specific coordinate position in a specified amount of time
     *
     * @param x       (0->15)
     * @param y       (0->15)
     * @param seconds approx. time it will take
     */
    synchronized void move(int x, int y, double seconds) {
        if(x==finishPosition[0]&&y==finishPosition[1]){
            completed=true;
            x=homeSpace[0];
            y=homeSpace[1];
        }
        isStart = false;
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
                if(x!=7) x++;
                break;
            case 1:
                if(y!=7) y++;
                break;
            case 2:
                if(x!=8) x--;
                break;
            case 3:
                if(y!=8) y--;
                break;
        }
    }

    public int[] getFinishPosition() {
        return finishPosition;
    }

    public void determinePosition() {
        //corners
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

    /**
     * /- Move Backwards -/
     * Moves a pawn backwards to a specified amount.
     * there is a bit of redundant calls here, could be optimized.
     * @param distance how many spaces to be moved
     *
     */
    public void moveBackward(int distance){
        //if already home do nothing
            for (int i = 0; i < distance; i++) {
                if (x == 15 && y == 15) {
                    y--;
                } else if (x == 0 && y == 15) {
                    x++;
                } else if (x == 15 && y == 0) {
                    x--;
                } else if (x == 0 && y == 0) {
                    y++;

                } else if (x == homeEntrance[0] && y == homeEntrance[1]) {
                    switch (color) {
                        case 0:
                            y++;
                            break;
                        case 1:
                            x--;
                            break;
                        case 2:
                            y++;
                            break;
                        case 3:
                            x++;
                            break;
                    }
                } else {
                    determinePosition();
                }
            }
            move(x, y, distance*.15);

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }


    public void setY(int y) {
        this.y = y;
    }

    public boolean isCompleted() {
        return completed;
    }
    public boolean isStart(){
        return isStart;
    }

    public int[] getBoardEntrance() {
        return boardEntrance;
    }
}