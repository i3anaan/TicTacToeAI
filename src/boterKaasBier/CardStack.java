package boterKaasBier;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import boterKaasBier.util.Direction;

public class CardStack extends Stack<Card> {
    public final int x;
    public final int y;
    public final BoardState board;
    
    
    public CardStack(int x, int y, BoardState board) {
        this.x = x;
        this.y = y;
        this.board = board;
    }
    
    public CardStack getNeighbour(Direction direction) {
        return board.getCardStack(x + direction.xTranslation, y + direction.yTranslation);
    }
    
    public List<CardStack> getNeighbours(Direction direction) {
        List<CardStack> list = new ArrayList<CardStack>();
        CardStack neighbour = getNeighbour(direction);
        if (!neighbour.isEmpty()) {
            list.add(neighbour);
            list.addAll(neighbour.getNeighbours(direction));
        } // Else empty list.
        
        return list;
    }
    
    public List<CardStack> getTwoWayNeighbours(Direction direction) {
        List<CardStack> list = getNeighbours(direction);
        list.addAll(getNeighbours(direction.getInverse()));
        return list;
    }
    
    public String toString() {
        return "S(" + x + ", " + y + ")";
    }
}
