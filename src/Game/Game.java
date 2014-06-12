package Game;

public class Game {

	private Board board;
	private int turnCount = 0;
	private char Xplayer = 'X';
	private char Oplayer = 'O';
	
	public Game() {
		
		board = new Board();
		//Create game + AI
	}

	/** performs a move, method will only do the move if it is your turn */
	public void doMove(char player, int field){
		if(player == getTurnPlayer()){
		board.doMove(player, field);
		turnCount++;
		}
		else{
			System.out.println("ITS NOT YOUR TURN");
		}
	}
	
	/** returns the turncount*/
	public int getTurnCount(){
		return turnCount;
	}
	
	/** returns the turn player*/
	public char getTurnPlayer(){
		
		if(turnCount%2 ==0){
			return Xplayer;
		}else{
			return Oplayer;
		}
	}
	
	/**
	 * Method to get the board for the AI
	 * @return a clone of the current board
	 */
	public Board getBoard(){
		return board.boardClone();
	}
	
	/** Resets the game */
	public void resetGame(){
		board = new Board();
		turnCount = 0;
	}
	
}
