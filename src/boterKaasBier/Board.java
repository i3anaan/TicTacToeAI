package boterKaasBier;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private BoardState boardState;
    private Deck deck;
    
    public Board() {
        deck = new Deck();
        boardState = new BoardState(deck);      
    }
    
    public boolean isFinished() {
        //TODO;
        return false;
    }
    
    public List<Move> getGuessMoves() {
        List<Move> moves = new ArrayList<Move>();
        for (CardStack stack : boardState.getNonEmptyCardStacks()) {
            if (!stack.peek().open) {
                moves.add(new Move(stack.x, stack.y));
            }
        }
        
        return moves;
    }
    
    public List<Move> getPutMoves() {
        //TODO add ability to extend playing field.
        
        List<Move> moves = new ArrayList<Move>();
        for (CardStack stack : boardState.getNonEmptyCardStacks()) {
            moves.add(new Move(stack.x, stack.y));
        }
        
        return moves;
    }
    
    public boolean doGuessMove(Player player, int x, int y, CardRange range, CardRange.ComparedToCard comparedToUnkown) {
        CardStack stack = boardState.getCardStack(x, y);
        Card unkown = stack.peek();
        unkown.open = true;
        range.addConstraint(comparedToUnkown, unkown);
        Card newCard = deck.getNext();
        boolean succes = range.isInRange(newCard);
        if (succes) {
            stack.pop(); //remove the now flipped card.
            player.putCards(this, stack.toArray(new Card[0]));
            
            stack.clear();
            stack.push(unkown);
            stack.push(newCard);
            return true;
        } else {
            stack.pop(); //remove the now flipped card.
            return false;
        }
    }
    
    public void doPutMove(int x, int y, Card card) {
        CardStack stack = boardState.getCardStack(x, y);
        card.open = !stack.isEmpty() && stack.peek().open;
        stack.push(card);
    }
}
