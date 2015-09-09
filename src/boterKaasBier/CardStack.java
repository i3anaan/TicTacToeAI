package boterKaasBier;

import java.util.Stack;

public class CardStack extends Stack<Card> {
    public final int x;
    public final int y;
    
    public CardStack(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
