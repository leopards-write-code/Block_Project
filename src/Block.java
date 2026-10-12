//A+ Computer Science  -  www.apluscompsci.com
//Name - Victor Li
//Date - 4/03/25
//Class - Computer Science 2 K
//Lab  - Block Project

import java.awt.*;



public class Block implements Locatable {

    //instance variables
    private int xPos;
    private int yPos;

    private int width;
    private int height;

    private Color color;

    private int dx;
    private int dy;



    /**
     * blank constructor (creates a Block at position (100, 1500)
     */
    Block() {

        color = Color.BLACK;
        xPos = 100;
        yPos = 1500;
        width = 10;
        height = 10;
        initializeDxDy();
    }
    /*
    Creates a block with the specified parameters and color black
     */
    Block(int x, int y, int width, int height) {
        xPos = x;
        yPos = y;
        this.width = width;
        this.height = height;
        color = Color.BLACK;
        //blockTwo.fill(xPos, yPos, width, height);
        initializeDxDy();
    }

    /**
     * Creates a block with the specified parameters
     * @param x-x coordinate of block
     * @param y-y coordinate of block
     * @param width - width of block
     * @param height - height of block
     * @param color - block color
     */
    Block(int x, int y, int width, int height, java.awt.Color color) {
        xPos = x;
        yPos = y;
        this.width = width;
        this.height = height;
        this.color = color;
        initializeDxDy();
    }
    /**
     * Sets x and y to parameters
     * @param x-the value xPos is to be set to
     * @param y-the value yPos is to be set to
     */
    public void setPos(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    /**
     * sets xPos to argument
     * @param x-the value xPos will be set to
     */
    public void setX(int x) {
        this.xPos = x;
    }
    // changes width of block
    public void setWidth(int width) {
        this.width = width;
    }
    // changes height of block
    public void setHeight(int height) {
        this.height = height;
    }
    // changes yPos of block
    public void setY(int yPos) {
        this.yPos = yPos;
    }
    // changes color of block
    public void setColor(Color color) {
        this.color = color;
    }
    // changes xSpeed of block
    public void setDx(int dx){
        this.dx = dx;
    }
    // changes ySpeed of block
    public void setDy(int dy){
        this.dy = dy;
    }
    // changes xSpeed and ySpeed of block
    public void setDxDy(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }
    // moves the position fo the block by dx and dy, respectively
    public void move(){
        xPos+=dx;
        yPos+=dy;
    }
    // returns the xSpeed
    public int getDx(){
        return dx;
    }
    //returns the xSpeed
    public int getDy(){
        return dy;
    }
    // draws the block, to be called in Blocks.Main
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }
    // sets dx and dy to -2, -1, 1, or 2 with equal probability
    private void initializeDxDy(){
        int rand = (int)(Math.random()*4)+1;
        if(rand==1){
            setDxDy(-2, -2);
        }
        else if(rand == 2){
            setDxDy(-1, -1);
        }
        else if(rand == 3){
            setDxDy(1, 1);
        }
        else{
            setDxDy(2, 2);
        }
    }
    //get methods
    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    //Checks whether a point is inside a block
    public boolean contains(int xPos, int yPos) {

        return (xPos >= this.xPos && xPos <= this.xPos + this.width &&
                yPos >= this.yPos && yPos <= this.yPos + this.height);
    }

    //Part two methods
    //

    /**
     * Converts the Block to a standard representation with positive width and height
     * Given a block with x = 10, y = 10, h = -4 then executing canonicalize results in
     * x = 10, y = 6, w = 3, h = 4
     **/
    public void canonicalize() {
        if (width < 0) {
            xPos += width;
            width = Math.abs(width);
        }
        if (height < 0) {
            yPos += height;
            height = Math.abs(height);
        }


    }

    /**
     * Returns a new block that is the tightest bounding region
     * that encompasses both blocks
     *
     * @param other A different block
     * @return A new block
     */
    public Block union(Block other) {

        int unionWidth = calculateUnion(xPos, width, other.getX(), other.getWidth());

        int unionHeight = calculateUnion(yPos, height, other.getY(), other.getHeight());


        return new Block(xPos, yPos, unionWidth, unionHeight);
    }
    private int calculateUnion(int pos, int size, int otherPos, int otherSize) {
        if (pos > otherPos) {
            return Math.max((pos - otherPos) + size, otherSize);
        } else if (pos == otherPos) {
            return Math.max(size, otherSize);
        } else {
            return Math.max((otherPos - pos) + otherSize, size);
        }
    }
    /**
     * returns a new block that is the intersection of both blocks or null if none exists
     *
     * @param other-the second block
     * @return a block thats the intersection or null if N/A
     */
    public Block intersection(Block other) {
        int otherX = 0;
        int otherY = 0;
        int otherWidth = 0;
        int otherHeight = 0;
        boolean noIntersection = false;
        /** if1- Determines if original block is to the left of "other"
         *  if2- determines if the x2 of original is greater than x1 of other
         * **/
        if (xPos <= other.getX()) {
            if (xPos + width > other.getX()) {
                otherX = other.getX();
                otherWidth = Math.min(other.getWidth(), xPos + width - other.getX());
            } else {
                noIntersection = true;
            }
        }
        /** if1- Determines if "other" is to the left of "original block"
         *  if2- determines if the x2 of other is greater than x1 of original
         * **/
        if (other.getX() <= xPos) {
            if (other.getX() + other.getWidth() > xPos) {
                otherX = xPos;
                otherWidth = Math.min(width, other.getX() + other.getWidth() - xPos);
            } else {
                noIntersection = true;
            }
        }
        /** if1- Determines if original block is above "other"
         *  if2- Determines if the y2 of original is greater than y1 of other (yPos + height = y2)
         *
         * **/
        if (yPos <= other.getY()) {
            if (yPos + height > other.getY()) {
                otherY = other.getY();
                otherHeight = Math.min(other.getHeight(), yPos + height - other.getY());
            } else {
                noIntersection = true;
            }
        }
        /** if1- Determines if "other" block is above original
         *  if2- Determines if the y2 of other is greater than y1 of original
         * (other.getY() + height = y2 of other)
         * **/
        if (other.getY() <= yPos) {
            if (other.getY() + other.getHeight() > yPos) {
                otherY = yPos;
                otherHeight = Math.min(height, other.getY() + other.getHeight() - yPos);
            } else {
                noIntersection = true;
            }
        }

        if (noIntersection) {
            return null;
        } else {
            return new Block(otherX, otherY, otherWidth, otherHeight);
        }
    }


    //Returns a string representation of the block, including the coordinates, dimensions, and color
    public String toString() {

        return getX() + " " + getY() + " " + getWidth() + " " + getHeight() + " " + getColor();
    }
}