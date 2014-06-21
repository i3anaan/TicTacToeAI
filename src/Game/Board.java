package Game;

import java.io.Serializable;
import java.util.Arrays;

public class Board implements Serializable{

	private char[] board;

	public Board() {
		board = new char[9];
		for(int i = 0; i< board.length; i++){
			board[i] = ' ';
		}
	}
	
	public Board(char[] grid){
		board = new char[9];
		for(int i = 0; i< board.length; i++){
			board[i] = grid[i];
		}
	}

	/**
	 * Performs a move if it is valid and prints the new board status to console,
	 * else it prints ILLEGAL MOVE
	 * @param player who performs the move
	 * @param field to perform the move on
	 */
	public void doMove(char player, int field){
		if(board[field] == Game.MARK_EMPTY){
			board[field] = player;
		}
		else{
			System.out.println("ILLEGAL MOVE!!!");
		}
	}

	/**
	 * Checks if the board is full or not
	 * @return full status
	 */
	public boolean checkFull(){

		boolean full = true;

		for(int i = 0; i< board.length; i++){
			if(board[i] == ' '){
				full = false;
			}
		}
		return full;
	}

	/**
	 * Checks if either players has made a winning move
	 * @return the winner
	 */
	public char checkWin(){

		if(		board[0] == Game.MARK_PLAYER1 && board[0] == board[1] && board[1] == board [2] ||
				board[3] == Game.MARK_PLAYER1 && board[3] == board[4] && board[4] == board[5] ||
				board[6] == Game.MARK_PLAYER1 &&	board[6] == board[7] && board[7] == board[8] ||

				board[0] == Game.MARK_PLAYER1 &&	board[0] == board[3] && board[3] == board[6] ||
				board[1] == Game.MARK_PLAYER1 &&	board[1] == board[4] && board[4] == board[7] ||
				board[2] == Game.MARK_PLAYER1 &&	board[2] == board[5] && board[5] == board[8] ||

				board[0] == Game.MARK_PLAYER1 &&	board[0] == board[4] && board[4] == board[8] ||
				board[2] == Game.MARK_PLAYER1 &&	board[2] == board[4] && board[4] == board[6]){

			return Game.MARK_PLAYER1;
		}
		if(		board[0] == Game.MARK_PLAYER2 && board[0] == board[1] && board[1] == board [2] ||
				board[3] == Game.MARK_PLAYER2 && board[3] == board[4] && board[4] == board[5] ||
				board[6] == Game.MARK_PLAYER2 &&	board[6] == board[7] && board[7] == board[8] ||

				board[0] == Game.MARK_PLAYER2 &&	board[0] == board[3] && board[3] == board[6] ||
				board[1] == Game.MARK_PLAYER2 &&	board[1] == board[4] && board[4] == board[7] ||
				board[2] == Game.MARK_PLAYER2 &&	board[2] == board[5] && board[5] == board[8] ||

				board[0] == Game.MARK_PLAYER2 &&	board[0] == board[4] && board[4] == board[8] ||
				board[2] == Game.MARK_PLAYER2 &&	board[2] == board[4] && board[4] == board[6]){

			return Game.MARK_PLAYER2;
		}
		else{
			return Game.MARK_EMPTY;
		}
	}
	
	public boolean checkGameOver(){
		return checkFull() || checkWin()!=Game.MARK_EMPTY;
	}
	
	/**
	 * returns a clone of this board
	 * @return board
	 */
	public Board getClone(){	
		return new Board(board);
	}
	
	/**
	 * Returns the value of the selected index
	 * @param index
	 * @return value of the index
	 */
	public char getIndex(int index){
		return board[index];
	}
	
	
	@Override
	public int hashCode(){
		int value =0;
		for(int i=0;i<board.length;i++){
			value = value + board[i]*((int)(Math.pow(256, i)));
		}
		//return value;
		return Arrays.hashCode(board);
	}
	
	
	@Override
	public boolean equals(Object object){
		if(object instanceof Board){
			Board board2 = (Board) object;
			boolean same = true;
			for(int i=0;i<board.length;i++){
				//System.out.println(i);
				if(board2.board[i]!=this.board[i]){
					same = false;
				}
			}
			return same;
		}else{
			return false;
		}
	}
	

	/**
	 * Returns the current status of the board
	 */
	public String toString(){

		String tostring = ("\n|" + board[0] + "|" + board[1] + "|" + board[2] + "|\n"
				//+"-------\n"
				+ "|" + board[3] + "|" + board[4] + "|" + board[5] + "|\n"
				//+"-------\n"
				+ "|" + board[6] + "|" + board[7] + "|" + board[8] + "|");

		return tostring;
	}

}
