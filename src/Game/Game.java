package Game;

import player.Player;
import player.PredictableAI;
import player.QLearningAI;
import player.RandomAI;

public class Game {

	private Board board;
	private int turnCount = 0;
	public static final char MARK_PLAYER1 = 'X';
	public static final char MARK_PLAYER2 = 'O';
	public static final char MARK_EMPTY = ' ';
	
	private Player player1;
	private Player player2;
	
	public static void main(String[] args){
		Player player1 = new QLearningAI(MARK_PLAYER1);
		Player player2 = new PredictableAI();
		int games = 0;
		int player1Wins = 0;
		int player2Wins = 0;
		
		//while(true){
		for(int i=0;i<10;i++){
			Game game = new Game(player1,player2);
			char winner = game.playGame();
			games++;
			if(winner==MARK_PLAYER1){
				player1Wins++;
			}else if(winner==MARK_PLAYER2){
				player2Wins++;
			}
			System.out.println("<#GAME OVER#>");
			System.out.println("Winner: "+winner);
			System.out.println(String.format("Winrate Player1 (QLearning): %.3f%%\tWinrate Player2 (Random): %.3f%%\tGames:%d",(((double)player1Wins) / ((double)games))*100,(((double)player2Wins) / ((double)games))*100,games));
		}
	}
	
	
	public Game(Player p1, Player p2) {
		this.player1 = p1;
		this.player2 = p2;
		//Create game + AI
		
		
	}
	public char playGame(){
		board = new Board();
		while(!(board.checkFull() || board.checkWin()!=MARK_EMPTY)){
			char turn = getTurnPlayer();
			if(turn==MARK_PLAYER1){
				doMove(turn,player1.doMove(board.boardClone()));
			}else if(turn==MARK_PLAYER2){
				doMove(turn,player2.doMove(board.boardClone()));
			}
		}
		
		return board.checkWin();
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
		//System.out.println(board.toString());
	}
	
	/** returns the turncount*/
	public int getTurnCount(){
		return turnCount;
	}
	
	/** returns the turn player*/
	public char getTurnPlayer(){
		
		if(turnCount%2 == 0){
			return MARK_PLAYER1;
		}else{
			return MARK_PLAYER2;
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
