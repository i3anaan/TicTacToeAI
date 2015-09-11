package boterKaasBier.players;

import java.util.List;

import boterKaasBier.Board;
import boterKaasBier.Card;
import boterKaasBier.CardRange;
import boterKaasBier.Move;
import boterKaasBier.CardRange.ComparedToCard;

public interface Player {
    
    public void doGuessMove(Board board);
    
    public void doPutMove(Board board, Card[] cards);
}
