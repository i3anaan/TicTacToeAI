package player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import Game.Board;
import Game.Game;

public class QLearningAI implements Player{
	//TODO print winrate van laatste X aantal mails
	//TODO upgrade QLearning met time-varying weighting factor (p270)
	
	
	public HashMap<Board,double[]> knowledge = new HashMap<Board,double[]>(); //TODO save this knowledge.
	public static final double GAMMA = 0.9;		//Used in QValue function.
	public static final int EXPLORATION_FACTOR = 1000; //10xEXPLORATION_FACTOR moves = 10% chance at random move
	protected char mark;	//The mark this AI has
	protected double movesDone = 0;
	protected Board previousBoard;
	protected int previousMove;
	protected boolean startOfGame;
	
	
	public QLearningAI(char mark){
		this.mark = mark;
		System.out.println("QLearning, MARK = "+mark);
	}
	
	public int doMove(Board board){
		int bestMove = getBestAction(board); //Calculate best move
		
		//Update QValue from previous move.
		//This is done in the next doMove() call, as it only now knows the result state of the action done.
		if(!startOfGame){//Ignore first move.
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
		
		//Save state and action information for next round
		previousBoard = board.getClone();
		previousMove = bestMove;
		//Save amount of moves done (used to determine explore rate)
		movesDone++;		
		startOfGame = false;
		
		//return the chosen move (best or random)
		return bestMove;
	}
	
	private double[] getQValues(Board board){
		double[] arr = knowledge.get(board);
		if(arr==null){
			arr = new double[]{0,0,0,0,0,0,0,0,0};
		}
		return arr;
	}
	
	private void updateQValues(Board board, int bestMove){
		double[] qValuesPrevious = getQValues(previousBoard);
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
	
	
	public double getQValue(Board board, int move){
		return getQValues(board)[move];
	}
	
	/**
	 * Returns the best action to perform, judging by the board state
	 * (Index of the move to perform)
	 * @param board
	 * @return
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
