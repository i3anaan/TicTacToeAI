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
	public static final int EXPLORATION_FACTOR = 1000; //10xEXPLORATION_FACTOR moves 10% chance at random move
	protected char mark;	//The mark this AI has
	private int markMultiplier;
	protected double movesDone = 0;
	
	
	public QLearningAI(char mark){
		this.mark = mark;
		markMultiplier = mark==Game.MARK_PLAYER1 ? 1 : -1;
		System.out.println("QLearning, MARK = "+mark);
	}
	
	public int doMove(Board oldBoard){
		int bestMove = getBestAction(oldBoard); //Calculate best move
		movesDone++;
		return bestMove;
	}
	
	
	public double getQValue(Board board, int move){
		double maxQ2 = -Double.MAX_VALUE;
		for(int opp = 0;opp<9;opp++){
			if(board.getIndex(opp)==Game.MARK_EMPTY){
				Board newBoard = board.getClone();
				newBoard.doMove(mark==Game.MARK_PLAYER1 ? Game.MARK_PLAYER2 : Game.MARK_PLAYER1, opp);
				double maxQ = -Double.MAX_VALUE;
				for(int i=0;i<9;i++){
					if(newBoard.getIndex(i)==Game.MARK_EMPTY){
						Board newNewBoard = newBoard.getClone();
						newNewBoard.doMove(mark, i);
						double[] arr = knowledge.get(newNewBoard);
						if(arr==null){
							arr = new double[]{0,0,0,0,0,0,0,0,0};
						}
						double tempQ = arr[i];
						
						if(tempQ>maxQ){
							maxQ = tempQ;
						}
					}
				}
				if(maxQ>maxQ2){
					maxQ2 = maxQ;
				}
			}
		}
		return getReward(board,move) + GAMMA * maxQ2;
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
		double[] qValues = new double[9];
		for(int i=0;i<9;i++){
			if(board.getIndex(i)==Game.MARK_EMPTY){
				double qValue = getQValue(board, i);
				qValues[i] = qValue;
				if(qValue==bestQValue){
					bestActions.add(i);
				}else if(qValue>bestQValue){
					bestActions.clear();
					bestActions.add(i);
					bestQValue = qValue;
				}
			}else{
				qValues[i] = 0;
			}
		}
		knowledge.put(board,qValues);
		
		if(Math.random()>(movesDone/(movesDone+EXPLORATION_FACTOR))){	//Randomness for exploration.
			int randomMoveFound = -1;
			while(randomMoveFound == -1){
				int randomMove = (int)(Math.random()*9);
				if(board.getIndex(randomMove)==Game.MARK_EMPTY){
					randomMoveFound=randomMove;
				}
			}
			return randomMoveFound;
		}else{
			return bestActions.get((int)(Math.random()*bestActions.size()));
		}
	}
	
	public void updateBoard(Board board, int move){
		board.doMove(mark,move);
	}
	
	public double getReward(Board boardOld, int move){
		Board boardNew = boardOld.getClone();
		boardNew.doMove(mark, move);
		
		if(!boardOld.checkGameOver()){
			if(boardNew.checkGameOver()){
				if(boardNew.checkWin()==mark){
					//Win
					return 20;
				}else if(boardNew.checkWin()!=Game.MARK_EMPTY){
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
