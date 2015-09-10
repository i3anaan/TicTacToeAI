package boterKaasBier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class BoardState {
    // row first, then column. (x first, then y)
    private Map<Integer, Map<Integer, CardStack>> boardState;
    private Deck deck;
    
    public BoardState() {
        boardState = new HashMap<Integer, Map<Integer, CardStack>>();
    }
    
    public BoardState(Deck deck) {
        for (int r = 1; r < 3; r++) {
            putCard(deck.getNext(), 0, 0);
            putCard(deck.getNext(), 0, 1);
            putCard(deck.getNext(), 0, 2);
            putCard(deck.getNext(), 1, 2);
            putCard(deck.getNext(), 2, 2);
            putCard(deck.getNext(), 1, 2);
            putCard(deck.getNext(), 0, 2);
            putCard(deck.getNext(), 0, 1);
        }
        putCard(deck.getNext(), 1, 1);
    }

    public CardStack getCardStack(int x, int y) {
        Map<Integer, CardStack> column = boardState.get(x);
        if (column == null) {
            column = new HashMap<Integer, CardStack>();
            boardState.put(x, column);
        }
        
        CardStack stack = column.get(y);
        if (stack != null) {
            return stack;
        } else {
            stack = new CardStack(x, y, this);
            column.put(y, stack);
            return stack;
        }
    }
    
    public List<CardStack> getNonEmptyCardStacks() {
        List<CardStack> list = new ArrayList<CardStack>();
        for (Map<Integer, CardStack> column : boardState.values()) {
            for (CardStack stack : column.values()) {
                if (!stack.isEmpty()) {
                    list.add(stack);
                }
            }
        }
        
        return list;
    }
    
    public void putCard(Card card, int x, int y) {
        CardStack stack = getCardStack(x, y);
        stack.push(card);
    }
    
    public Card peekTopCard(int x, int y) {
        CardStack stack = getCardStack(x, y);
        if (!stack.isEmpty()) {
            return stack.peek();
        } else {
            return null;
        }
    }
    
    public Card removeTopCard(int x, int y) {
        CardStack stack = getCardStack(x, y);
        if (!stack.isEmpty()) {
            return stack.pop();
        } else {
            return null;
        }        
    }
}
