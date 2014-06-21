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
	public static final int EXPLORATION_FACTOR = 1000000; //1.000.000 = after 10.000.000 games 10% at random move
	protected char mark;	//The mark this AI has
	protected float movesDone = 0;
	
	
	public QLearningAI(char mark){
		this.mark = mark;
		System.out.println("QLearning, MARK = "+mark);
	}
	
	public int doMove(Board oldBoard){
		int bestMove = getBestAction(oldBoard); //Calculate best move
		if(Math.random()>(movesDone/(movesDone+EXPLORATION_FACTOR))){	//Randomness for exploration.
			int moveFound = -1;
			while(moveFound == -1){
				int randomMove = (int)(Math.random()*9);
				if(oldBoard.getIndex(randomMove)==Game.MARK_EMPTY){
					moveFound = randomMove;
				}
			}
			bestMove = moveFound;
		}else{
		}
		Board newBoard = oldBoard.getClone();
		updateBoard(newBoard, bestMove);	//Play best move on clone board
		double reward = getReward(oldBoard, newBoard);	//Calculate reward for the transition
		updateQValue(oldBoard,bestMove,reward);	//Update the QValue for the oldBoard + transition just done.
		movesDone++;
		return bestMove;
	}
	
	
	public double getQValue(Board board, int move){
		double[] arr = knowledge.get(board);
		if(arr==null){
			arr = new double[9];
			knowledge.put(board, arr);
		}
		return knowledge.get(board)[move];
	}
	
	public void updateQValue(Board board, int move, double reward){
		double[] values =knowledge.get(board);
		if(values==null){
			values = new double[9];
		}
		values[move] = recalculateQValue(board, move, reward);
		knowledge.put(board, values);
	}
	
	public double recalculateQValue(Board board, int move, double reward){
		Board newBoard = board.getClone();
		updateBoard(newBoard,move);
		if(!newBoard.checkGameOver()){			
			double qValue = getQValue(newBoard,getBestAction(newBoard));
			if(qValue>0){
				System.out.println("value for next state = "+qValue);
			}
			return reward + GAMMA*qValue;
		}else{
			return reward + GAMMA*0;
		}
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
	
	public void updateBoard(Board board, int move){
		board.doMove(mark,move);
	}
	
	public double getReward(Board boardOld, Board boardNew){
		if(!boardOld.checkGameOver()){
			if(boardNew.checkGameOver()){
				if(boardNew.checkWin()==mark){
					//Win
					return 20;
				}else{
					//Loss or Draw
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
