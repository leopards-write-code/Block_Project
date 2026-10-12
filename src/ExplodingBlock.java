import java.awt.*;

class ExplodingBlock extends Block{
    private double x;
    private  double y;
    private double angle;
    private Color c;
    public ExplodingBlock(double x, double y, int width, int height, double angle){
        super((int)x, (int)y, width, height);
        this.x = x;
        this.y = y;
        this.angle = angle;
        c= Color.WHITE;
    }
    public int getX(){
        return (int)x;
    }
    public int getY(){
        return (int)y;
    }
    public void setColor(Color c){
        this.c = c;
    }
    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }
    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(c);
        g.fillRect((int) x, (int) y, getWidth(), getHeight());
    }
    @Override
    public void move(){
        x+=Math.cos(angle)*getDx();
        y+=Math.sin(angle)*getDy();
    }

    @Override
    public String toString() {
        return "x: "+x+" y:"+y+" angle"+angle;
    }
}
