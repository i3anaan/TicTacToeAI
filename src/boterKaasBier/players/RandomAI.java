package boterKaasBier.players;

import java.util.List;

import boterKaasBier.Board;
import boterKaasBier.Card;
import boterKaasBier.CardRange;
import boterKaasBier.Move;
import boterKaasBier.CardRange.ComparedToCard;

public class RandomAI implements Player{
    public void doGuessMove(Board board) {
        List<Move> moves = board.getGuessMoves();
        Move rMove = moves.get((int) (Math.random() * moves.size()));
        CardRange range = new CardRange();
        ComparedToCard compared = (Math.random() < 0.5) ? ComparedToCard.HIGHER_THEN : ComparedToCard.LOWER_THEN;
        
        board.doGuessMove(this, rMove.x, rMove.y, range, compared);
    }
    
    public void doPutMove(Board board, Card[] cards) {
        for (Card card : cards) {
            List<Move> moves = board.getPutMoves();
            Move rMove = moves.get((int) (Math.random() * moves.size()));
            board.doPutMove(rMove.x, rMove.y, card);
        }
    }
}
