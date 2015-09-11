package boterKaasBier;

import java.util.ArrayList;
import java.util.List;

import boterKaasBier.players.Player;
import boterKaasBier.util.Direction;

public class Board {
    private BoardState boardState;
    private Deck deck;

    private Player lastPlayer;
    
    public Board() {
        deck = new Deck();
        boardState = new BoardState(deck);      
    }
    
    public boolean isFinished() {
        return isWon() || isDraw();
    }
    
    public boolean isInOpenRow(CardStack base) {
        Direction[] dirs = new Direction[] { Direction.TOP, Direction.TOP_RIGHT, Direction.RIGHT, Direction.BOTTOM_LEFT };
        for (Direction dir : dirs) {
            boolean allOpen = true;
            for (CardStack stack : base.getTwoWayNeighbours(dir)) {
                //System.out.println("Checking stack " + stack + " to be open.");
                allOpen = allOpen && (!stack.isEmpty() && stack.peek().open);
            }
            
            if (allOpen) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean isWon() {
        return isInOpenRow(boardState.getLastChanged());
    }
    
    public boolean isDraw() {
        boolean allOpen = true;
        for (CardStack stack : boardState.getNonEmptyCardStacks()) {
            allOpen = allOpen && stack.peek().open;
        }
        return allOpen;
    }
    
    public Player getWinner() {
        if (isWon()) {
            return lastPlayer;
        } else {
            return null;
        }
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
    
    public Deck getDeck() {
        return deck;
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
        if (this.isFinished()) {
            throw new GameAlreadyOverException();
        }
        
        lastPlayer = player;
        CardStack stack = boardState.getCardStack(x, y);
        Card unkown = stack.peek();
        unkown.open = true;
        //TODO Check if range is valid?
        range.addConstraint(comparedToUnkown, unkown);
        Card newCard = deck.getOpenNext();
        boolean succes = range.isInRange(newCard);
        if (succes) {
            stack.pop(); //remove the now flipped card.
            player.doPutMove(this, stack.toArray(new Card[0]));
            
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
    
    public void printState() {
        for (int y = boardState.getMinY(); y < boardState.getMaxY(); y++) {
            for (int x = boardState.getMinX(); x < boardState.getMaxX(); x++) {
                String stackChar;
                CardStack stack = boardState.getCardStack(x, y);
                if (!stack.isEmpty()) {
                    if (stack.peek().open) {
                        stackChar = "X";
                    } else {
                        stackChar = stack.size() + "";
                    }
                } else {
                    stackChar = " ";
                }
                System.out.print(stackChar);
            }
            System.out.println();
        }
    }
}
