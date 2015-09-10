package boterKaasBier.util;

public class Direction {
    public final int xTranslation;
    public final int yTranslation;
    
    public static final Direction TOP = new Direction(0,1);
    public static final Direction TOP_RIGHT = new Direction(1,1);
    public static final Direction RIGHT = new Direction(1,0);
    public static final Direction BOTTOM_RIGHT = new Direction(1,-1);
    public static final Direction BOTTOM = new Direction(0,-1);
    public static final Direction BOTTOM_LEFT = new Direction(-1,-1);
    public static final Direction LEFT = new Direction(-1,0);
    public static final Direction TOP_LEFT = new Direction(-1,1);
    
    public Direction(int x, int y) {
        this.xTranslation = x;
        this.yTranslation = y;
    }
    
    public Direction getInverse() {
        return new Direction(-xTranslation, -yTranslation);
    }
}
