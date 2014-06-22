package player;

import Game.Board;

public interface Player {
	/**
	 * The Player will return the move it wants to do, based upon the given board.
	 * @param board	Current board
	 * @return	Move the Player wants to do on the current board.
	 */
	public int doMove(Board board);
	/**
	 * Should be called when the game is over.
	 * This is to give the Player the ability to see the final state of the Board.
	 * @param board	Final state of the board, when the game is over.
	 */
	public void endOfGame(Board board);
	/**
	 * Indicate a new game has started.
	 */
	public void startOfGame();
}
