import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//A+ Computer Science  -  www.apluscompsci.com
//Name - Victor Li
//Date - 4/03/25
//Class - Computer Science 2 K
//Lab  - Block Project
public class BlockManager {
    /*
        The block manager class stores a list of blocks
        and allows code in the other classes to manipulate them
        by calling methods such as raise(), lower, addBlock, etc on
        a block manager object
     */
    private int speedOfExplosion = 10;
    private ArrayList<Block>blocks;

    /**
     * Instantiate the list. Initially it stores no blocks.
     */
    public BlockManager(){
        blocks = new ArrayList<>();
    }

    /**
     * Add the block to the list of blocks.
     * @param block-the block to be added
     */
    public void addBlock(Block block){
        blocks.add(block);
    }

    /**
     * Draws all the blocks.
     * @param g
     */
    public void drawAll(Graphics g){
        for(Block b: blocks){
            b.draw(g);
        }
    }
    /**
     * Invoked when the user left clicks and passes the x/y coordinates of the mouse click. If these
     * coordinates touch a block, then move it to the very top (end) of the list.
     */
    public void raise(int x, int y){

        for(int i = 0; i<blocks.size(); i++){
            Block b = blocks.get(i);
            if(b.contains(x, y)){
                blocks.remove(i);
                blocks.add(b);
                break;
            }
        }

    }

    /**
     * Invoked when the user shift-left clicks. Moves only the topmost block at these coordinates, if applicable, to the very
     * bottom (beginning) of the list.
     */
    public void lower(int x, int y){
        for(int i = blocks.size()-1; i>=0; i--){
            Block b = blocks.get(i);
            if(b.contains(x, y)){
                blocks.remove(i);
                blocks.add(0, b);
                break;
            }
        }
    }
    // removes block at the top at the point(x, y)
    public void delete(int x, int y){

        for(int i = blocks.size()-1; i>=0; i--){
            Block b = blocks.get(i);
            if(b.contains(x, y)){
                blocks.remove(i);
                break;
            }
        }
    }
    // deletes all blocks at the point(x, y)
    public void deleteAll(int x, int y){
        for(int i = 0; i<blocks.size(); i++){
            Block b = blocks.get(i);
            if(b.contains(x, y)){
                blocks.remove(b);
                i--;
            }
        }
    }
    /*
    This removes exploding only and is for when the simulation
    is not running (start has not been pressed), this way
    only the exploding blocks are moved and the
    regular blocks stay in place
     */
    public void moveExplodingOnly(int width, int height){
        ArrayList<Block>removeBlocks = new ArrayList<>();
        for (Block b: blocks){

            if(b instanceof ExplodingBlock){
                b.move();
                if(b.getX()>width-b.getWidth()||b.getX()<0){
                    removeBlocks.add(b);
                }
                if(b.getY()>height-b.getHeight()||b.getY()<0){
                    removeBlocks.add(b);
                }
            }
        }
        blocks.removeAll(removeBlocks);
    }
    // moves all blocks onscreen
    public void moveAll(int width, int height){
        // Can't remove the blocks while in the loop
        // because that would cause concurrent modification errors
        // if the timer was running and exploding blocks
        // were being created and destroyed
        ArrayList<Block>removeBlocks = new ArrayList<>();
        for (Block b: blocks){
            b.move();
            if(b instanceof ExplodingBlock  ){
                if(b.getX()>width-b.getWidth()||b.getX()<0){
                    removeBlocks.add(b);
                }
                if(b.getY()>height-b.getHeight()||b.getY()<0){
                    removeBlocks.add(b);
                }
            }
            else{
                if(b.getX()>width-b.getWidth()||b.getX()<0){
                    b.setDx((b.getDx()*-1));
                }
                if(b.getY()>height-b.getHeight()||b.getY()<0){
                    b.setDy((b.getDy())*-1);
                }
            }

        }
        blocks.removeAll(removeBlocks);
    }

    /**
     * Shuffles the list and relocates all blocks. Each relocated block must entirely fit into the given width
     * and height parameters.
     * @param width-width of panel
     * @param height-height of panel
     */
    public void shuffle(int width, int height){
        Collections.shuffle(blocks);
        for(Block b:blocks){
            int x = (int)(Math.random()*(width-b.getWidth()));
            int y = (int)(Math.random()*(height-b.getHeight()));
            b.setPos(x, y);
        }
    }

    /**
     * Invoked when the user middle clicks. If these coordinates touch any block, then delete the block
     * from the list and create exploding blocks. An exploding block will be 1 pixel and will radiate outwards
     * from the center. Create len * width exploding blocks from the clicked block. An exploding block is a
     * child of block with 3 double attributes, x, y and angle.
     * @param x-x coordinate of mouse click
     * @param y-y coordinate of mouse click
     */
    public void explode(int x, int y){
        for(int i = 0; i<blocks.size(); i++){
            if(blocks.get(i).contains(x, y)&&!(blocks.get(i) instanceof ExplodingBlock)){
                Block b = blocks.get(i);
                blocks.remove(i--);
                createExplodingBlocks(b);
            }
        }
    }
    private void createExplodingBlocks(Block b){

        double centerX = b.getX() + (double) b.getWidth() / 2;
        double centerY = b.getY() + (double) b.getHeight() / 2;

        for (int py = b.getY(); py < b.getY() + b.getHeight(); py++) {
            for (int px = b.getX(); px < b.getX() + b.getWidth(); px++) {
                double angle = Math.atan2(py - centerY, px - centerX);
                ExplodingBlock eb = new ExplodingBlock(px, py, 1, 1, angle);
                eb.setColor(b.getColor());
                eb.setDx(speedOfExplosion);
                eb.setDy(speedOfExplosion);

                blocks.add(eb);
            }
        }
    }

    /**
     * Invoked when the user presses the 'E' key. All non-exploding blocks will be removed and turned into
     * exploding blocks. b instanceof ExplodingBlock will be useful.
     */
    public void explodeAll(){
        ArrayList<Block> nonExplodingBlocks = new ArrayList<>();
        for (Block b : blocks) {
            if (!(b instanceof ExplodingBlock)) {
                nonExplodingBlocks.add(b);
            }
        }
        for (Block b : nonExplodingBlocks) {
            createExplodingBlocks(b);
            blocks.remove(b);
        }
    }

    /**
     * Invoked when the user presses the 'I' key. All non-exploding blocks will be removed and turned into
     * exploding blocks. These exploding blocks will radiate inward toward the center of the original block.
     */
    public void implodeAll(){
        ArrayList<Block> nonExplodingBlocks = new ArrayList<>();
        for (Block b : blocks) {
            if (!(b instanceof ExplodingBlock)) {
                nonExplodingBlocks.add(b);
            }
        }
        for (Block b : nonExplodingBlocks) {
            createImplodingBlocks(b);
            blocks.remove(b);
        }
    }
    // private helper method to create imploding blocks
    private void createImplodingBlocks(Block b) {
        double centerX = b.getX() + (double) b.getWidth() / 2;
        double centerY = b.getY() + (double) b.getHeight() / 2;
        for (int py = b.getY(); py < b.getY() + b.getHeight(); py++) {
            for (int px = b.getX(); px < b.getX() + b.getWidth(); px++) {
                double angle = Math.atan2(centerY - py, centerX - px); // Opposite of explode, so i t implodes :>)
                ExplodingBlock eb = new ExplodingBlock(px, py, 1, 1, angle);
                eb.setColor(b.getColor());
                eb.setDx(speedOfExplosion);
                eb.setDy(speedOfExplosion);
                blocks.add(eb);
            }
        }
    }
    /**
     * Clears everything from the blocks list
     */
    public void clear(){
        blocks.clear();
    }
    // sets the speed of the exploding blocks
    public void setSpeedOfExplosion(int speed){
        this.speedOfExplosion = speed;
    }
    public ArrayList<Block> getBlocks(){
        return blocks;
    }
    // For debugging purposes
    public String toString(){
        String s = "";
        for (Block block : blocks) {
            s += block.toString();
        }
        return s;
    }
}
