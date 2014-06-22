package player;

import Game.Board;

public interface Player {
	public int doMove(Board board);
	public void endOfGame(Board board);
	public void startOfGame();
}
