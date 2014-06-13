package player;

import Game.Board;
import Game.Game;

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

}
