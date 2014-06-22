package player;

import java.util.ArrayList;
import java.util.HashMap;
import Game.Board;
import Game.Game;

/**
 * An AI that makes use of QLearning to learn to play TicTacToe.
 * QLearning has the advantage of not having to know the entire model of the world.
 * In other words, it does require to be able to decide the state an action leads to.
 * It can get the resulting state later, while still learning from it.
 * This makes QLearning a useful machine learning algorithm for TicTacToe.
 * @author I3anaan
 *
 */
public class QLearningAI implements Player{
	/**
	 * The HashMap containing all the QValues.
	 * Board is the key (state), index in the resulting double[] is the action.
	 */
	public HashMap<Board,double[]> knowledge = new HashMap<Board,double[]>();
	/**
	 * Gamma factor in QLearning Algorithm.
	 */
	public static final double GAMMA = 0.9;
	/**
	 * Determines how much exploration should be done.
	 * 10xEXPLORATION_FACTOR moves = 10% chance at random move
	 */
	public static final int EXPLORATION_FACTOR = 1000; //
	/**
	 * The mark this QLearningAI got assigned by the game.
	 */
	protected char mark;
	/**
	 * How many moves this QLearningAi has done.
	 */
	protected double movesDone = 0;
	/**
	 * The Board state from the previous iteration.
	 * This is used in calculating rewards and assigning QValues.
	 * The previousBoard and current Board get compared to calculate the reward.
	 * The previousBoard and current Board are used to calculate the new QValue for Q(previousBoard,previousMove)
	 */
	protected Board previousBoard;
	/**
	 * The move from the previous iteration.
	 * This is used in calculating QValues.
	 */
	protected int previousMove;
	/**
	 * Whether or not this AI's first move has to be played.
	 */
	protected boolean startOfGame;
	
	/**
	 * Constructor for QLearningAI
	 * @param mark	The mark assigned from the Game.
	 */
	public QLearningAI(char mark){
		this.mark = mark;
		System.out.println("QLearningAI, MARK = "+mark+"  Exploration factor: "+EXPLORATION_FACTOR);
		startOfGame();
	}
	
	public int doMove(Board board){
		//Calculate best move based on QValues for the actions from the current state (Board)
		int bestMove = getBestAction(board);
		
		//Update QValue from previous move.
		//This is done in this doMove() call as until now it did not know the result state of the previous action.
		if(!startOfGame){//Ignore first move. (Do not try to update QValues for non existing pre-game states)
			updateQValues(board, bestMove);
		}
		
		
		//Randomness for exploration.
		if(Math.random()>(movesDone/(movesDone+EXPLORATION_FACTOR))){
			int randomMoveFound = -1;
			while(randomMoveFound == -1){
				int randomMove = (int)(Math.random()*9);
				if(board.getIndex(randomMove)==Game.MARK_EMPTY){
					randomMoveFound=randomMove;
				}
			}
			bestMove = randomMoveFound;
		}
		
		//Save state and action information for next round.
		previousBoard = board.getClone();
		previousMove = bestMove;
		//Save amount of moves done (used to determine explore rate)
		movesDone++;
		//Definitely has done its first move now.
		startOfGame = false;
		
		//return the chosen move (best or random)
		return bestMove;
	}
	
	/**
	 * @param board	State requested
	 * @return All the QValues for each action from the state requested.
	 *  QValues are initialised to 0;
	 */
	public double[] getQValues(Board board){
		double[] arr = knowledge.get(board);
		if(arr==null){
			arr = new double[]{0,0,0,0,0,0,0,0,0};
		}
		return arr;
	}
	
	/**
	 * Updates the QValue for the previous state and move. (Q(previousBoard,previousMove))
	 * @param board	The resulting state of the previousMove.
	 * @param bestMove	The bestMove to do on the current Board.
	 */
	private void updateQValues(Board board, int bestMove){
		double[] qValuesPrevious = getQValues(previousBoard);
		
		//QLearning algorithm as described in Ertel page 268.
		qValuesPrevious[previousMove] = getReward(previousBoard, board) + GAMMA*getQValues(board)[bestMove];
		
		knowledge.put(previousBoard, qValuesPrevious);
	}
	
	public void endOfGame(Board board){
		updateQValues(board,0);
		//0 can be given as the QValues of a gameOver-Board will all be 0;
	}
	
	public void startOfGame(){
		startOfGame = true;
	}
	
	/**
	 * @param board	The state in the state/action pair requested
	 * @param move	The action in the state/action pair requested
	 * @return	The QValue for the given state and action.
	 */
	public double getQValue(Board board, int move){
		return getQValues(board)[move];
	}
	
	/**
	 * Returns the best action to perform, judging by the board state.
	 * (action = Index of the move to perform)
	 * @param board	The current board state.
	 * @return The best action to perform, judging by the board state.
	 * When multiple moves are the best it will select random from the best moves.
	 */
	public int getBestAction(Board board){
		ArrayList<Integer> bestActions = new ArrayList<Integer>();
		double bestQValue = -Double.MAX_VALUE;
		for(int i=0;i<9;i++){
			if(board.getIndex(i)==Game.MARK_EMPTY){
				double qValue = getQValue(board, i);
				if(qValue==bestQValue){
					bestActions.add(i);
				}else if(qValue>bestQValue){
					bestActions.clear();
					bestActions.add(i);
					bestQValue = qValue;
				}
			}
		}
		return bestActions.get((int)(Math.random()*bestActions.size()));
	}
	
	/**
	 * @param boardOld
	 * @param boardNew
	 * @return The reward for transitioning from boardOld to boardNew.
	 * Game In Progress = 0;
	 * Win = 20;
	 * Loss = -20;
	 * Draw = 0;
	 */
	public double getReward(Board boardOld, Board boardNew){
		
		if(!boardOld.isGameOver()){
			if(boardNew.isGameOver()){
				if(boardNew.getWinner()==mark){
					//Win
					return 20;
				}else if(boardNew.getWinner()!=Game.MARK_EMPTY){
					//Loss
					return -20;
				}else{
					//Draw
					return 0;
				}
			}else{
				//Game still in progress.
				return 0;
			}
		}else{
			//Error, boardOld already full.
			System.err.println("SHOULD NEVER HAPPEN");
			return -1;
		}
	}
	
	@Override
	public String toString(){
		return "QLearningAI";
	}
}
