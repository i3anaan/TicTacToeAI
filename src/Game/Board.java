package Game;

public class Board {

	private char[] board = new char[8];

	public Board() {

		for(int i = 0; i< board.length; i++){
			board[i] = ' ';
		}
	}

	/**
	 * Performs a move if it is valid and prints the new board status to console,
	 * else it prints ILLEGAL MOVE
	 * @param player who performs the move
	 * @param field to perform the move on
	 */
	public void doMove(char player, int field){
		if(board[field] == ' '){
			board[field] = player;
			System.out.println(toString());
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
	 * @return if someone has won
	 */
	public boolean checkWin(){

		if(board[0] == board[1] && board[1] == board [2] ||
				board[3] == board[4] && board[4] == board[5] ||
				board[6] == board[7] && board[7] == board[8] ||

				board[0] == board[3] && board[3] == board[6] ||
				board[1] == board[4] && board[4] == board[7] ||
				board[2] == board[5] && board[5] == board[8] ||

				board[0] == board[4] && board[4] == board[8] ||
				board[2] == board[4] && board[4] == board[6]){

			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * returns a clone of this board
	 * @return board
	 */
	public Board boardClone(){	
		
			try {
				return (Board)this.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				System.out.println("CANT CLONE BOARD");
				return null;
			}
		
	}
	
	/**
	 * Returns the value of the selected index
	 * @param index
	 * @return value of the index
	 */
	public char getIndex(int index){
		return board[index];
	}

	/**
	 * Returns the current status of the board
	 */
	public String toString(){

		String tostring = ("|" + board[0] + "|" + board[1] + "|" + board[2] + "|\n"
				+"-------"
				+ "|" + board[3] + "|" + board[4] + "|" + board[5] + "|\n"
				+"-------"
				+ "|" + board[6] + "|" + board[7] + "|" + board[8] + "|\n");

		return tostring;
	}

}
