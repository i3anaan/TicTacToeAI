package player;

import Game.Board;
import Game.Game;

public class RandomAI implements Player{

	public int doMove(Board board){
		int moveFound = -1;
		while(moveFound == -1){
			int randomMove = (int)(Math.random()*9);
			if(board.getIndex(randomMove)==Game.MARK_EMPTY){
				moveFound = randomMove;
			}
		}
		return moveFound;
	}
}
