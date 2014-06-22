package player;

import Game.Board;
import Game.Game;

/**
 * An AI that always does the lowest available move.
 * @author I3anaan
 *
 */
public class PredictableAI implements Player {

	@Override
	public int doMove(Board board) {
		for(int i=0;i<9;i++){
			if(board.getIndex(i)==Game.MARK_EMPTY){
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public String toString(){
		return "PredictableAI";
	}

	@Override
	public void endOfGame(Board board) {
	}

	@Override
	public void startOfGame() {
	}

}
