package Game;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;

import com.sun.java.swing.plaf.windows.resources.windows_zh_CN;

import player.HumanPlayer;
import player.PersistentQLearningAI;
import player.Player;
import player.PredictableAI;
import player.QLearningAI;
import player.RandomAI;

public class Game {

	public static final int GAMES_TO_PLAY = 50000;
	private Board board;
	private int turnCount = 0;
	public static final char MARK_PLAYER1 = 'X';
	public static final char MARK_PLAYER2 = 'O';
	public static final char MARK_EMPTY = ' ';
	
	//Charts
	private static int graphFidelity = 1000;
	private static double[][] maxWins;
	private static double[][] player1WinsData;
	private static double[][] player2WinsData;
	private static double[][] player1WinRate;
	private static double[][] player2WinRate;
	private static double[][] player1RecentWinRateData;
	private static double[][] player2RecentWinRateData;
	private static char[] recentResults;
	
	private Player player1;
	private Player player2;
	
	public static void main(String[] args){
		maxWins = new double[2][graphFidelity];
		player1WinsData = new double[2][graphFidelity];
		player2WinsData = new double[2][graphFidelity];
		player1WinRate = new double[2][graphFidelity];
		player2WinRate = new double[2][graphFidelity];
		player1RecentWinRateData = new double[2][graphFidelity];
		player2RecentWinRateData = new double[2][graphFidelity];
		recentResults = new char[100];
		
		
		
		
		
		System.out.println("TicTacToe, playing "+GAMES_TO_PLAY+" games.");
		Player player1 = new PersistentQLearningAI(MARK_PLAYER1,false,true);
		//Player player1 = new QLearningAI(MARK_PLAYER1);
		//Player player2 = new PredictableAI();
		Player player2 = new RandomAI();
		//Player player2 = new HumanPlayer();
		//Player player2 = new QLearningAI(MARK_PLAYER2);
		int games = 0;
		int player1Wins = 0;
		int player2Wins = 0;
		
		for(int i = 0;i<GAMES_TO_PLAY;i++){
			Game game = new Game(player1,player2);
			
			player1.startOfGame();
			player2.startOfGame();
			char winner = game.playGame();
			player1.endOfGame(game.board);
			player2.endOfGame(game.board);
			
			if((games%(GAMES_TO_PLAY/graphFidelity))==0){
				int number = (int) Math.floor((games/(GAMES_TO_PLAY/graphFidelity)));
				maxWins[0][number] = games;
				maxWins[1][number] = games;
				player1WinsData[0][number] = games;
				player1WinsData[1][number] = player1Wins;
				player2WinsData[0][number] = games;
				player2WinsData[1][number] = player2Wins;
				player1WinRate[0][number] =games;
				player1WinRate[1][number] =games==0 ? 0 : ((((double)player1Wins)/((double)games))*GAMES_TO_PLAY);
				player2WinRate[0][number] =games;
				player2WinRate[1][number] =games==0 ? 0 : ((((double)player2Wins)/((double)games))*GAMES_TO_PLAY);
				player1RecentWinRateData[0][number] = games;
				player1RecentWinRateData[1][number] = (((double)winCount(MARK_PLAYER1, recentResults))/((double)100))*GAMES_TO_PLAY;
				player2RecentWinRateData[0][number] = games;
				player2RecentWinRateData[1][number] = (((double)winCount(MARK_PLAYER2, recentResults))/((double)100))*GAMES_TO_PLAY;
			
			
			}
			recentResults[games%recentResults.length] = winner;
			games++;
			if(winner==MARK_PLAYER1){
				player1Wins++;
			}else if(winner==MARK_PLAYER2){
				player2Wins++;
			}
			if(games%(GAMES_TO_PLAY/100)==0){
				//System.out.println("<#GAME OVER#>");
				//System.out.println("Winner: "+winner);
				System.out.println(String.format("Winrate Player1 (%s): %.3f%%\tWinrate Player2 (%s): %.3f%% \tGames:%d",player1.toString(),(((double)player1Wins) / ((double)games))*100,player2.toString(),(((double)player2Wins) / ((double)games))*100,games));
			}
		}
		//Build chart
		DefaultXYDataset data = new DefaultXYDataset();
		data.addSeries("maxWins", maxWins);
		data.addSeries(player1.toString(), player1WinsData);
		data.addSeries(player2.toString(), player2WinsData);
		data.addSeries("P1Winrate", player1WinRate);
		data.addSeries("P2Winrate", player2WinRate);
		data.addSeries("P1RecentWinrate", player1RecentWinRateData);
		data.addSeries("P2RecentWinrate", player2RecentWinRateData);
		JFreeChart chart = ChartFactory.createXYLineChart("TicTacToe", "Games Played", "Games won", data);
		try {
			ChartUtilities.saveChartAsPNG(new File("TicTacToeChart.png"), chart, 600, 600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static int winCount(char mark, char[] arr){
		int wins = 0;
		for(int i=0;i<arr.length;i++){
			if(arr[i]==mark){
				wins++;
			}
		}
		return wins;
	}
	
	
	public Game(Player p1, Player p2) {
		this.player1 = p1;
		this.player2 = p2;
		//Create game + AI
		
		
	}
	public char playGame(){
		board = new Board();
		while(!(board.isFull() || board.getWinner()!=MARK_EMPTY)){
			char turn = getTurnPlayer();
			if(turn==MARK_PLAYER1){
				doMove(turn,player1.doMove(board.getClone()));
			}else if(turn==MARK_PLAYER2){
				doMove(turn,player2.doMove(board.getClone()));
			}
		}
		
		return board.getWinner();
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
		return board.getClone();
	}
	
	/** Resets the game */
	public void resetGame(){
		board = new Board();
		turnCount = 0;
	}
	
}
