package ai;

import java.util.ArrayList;
import java.util.HashMap;

public class QLearningAI implements AI{

	private HashMap<char[],double[]> knowledge = new HashMap<char[],double[]>();
	private static final double GAMMA = 0.9;		//Used in QValue function.
	private static final char MARK_PLAYER = 'X';	//The mark this AI has
	private static final char MARK_EMPTY = ' ';		//Empty place mark
	
	public int doMove(char[] oldBoard){
		int bestMove = getBestAction(oldBoard); //Calculate best move
		if(Math.random()>0.9){	//Randomness for exploration.
			int moveFound = -1;
			while(moveFound == -1){
				int randomMove = (int)(Math.random()*9);
				if(oldBoard[randomMove]==MARK_EMPTY){
					moveFound = randomMove;
				}
			}
			bestMove = moveFound;
		}
		char[] newBoard = updateBoard(oldBoard, bestMove);	//Play best move on clone board
		double reward = getReward(oldBoard, newBoard);	//Calculate reward for the transition
		updateQValue(oldBoard,bestMove,reward);	//Update the QValue for the oldBoard + transition just done.
		return 3;
	}
	
	
	public double getQValue(char[] board, int move){
		return knowledge.get(board)[move];
	}
	
	public void updateQValue(char[] board, int move, double reward){
		double[] values =knowledge.get(board);
		values[move] = recalculateQValue(board, move, reward);
		knowledge.put(board, values);
	}
	
	public double recalculateQValue(char[] board, int move, double reward){
		char[] newBoard = updateBoard(board,move);
		return reward + GAMMA*getQValue(newBoard,getBestAction(newBoard));
	}
	
	public int getBestAction(char[] board){
		ArrayList<Integer> bestActions = new ArrayList<Integer>();
		double bestQValue = Double.MIN_VALUE;
		for(int i=0;i<9;i++){
			if(board[i]!=MARK_EMPTY){
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
	
	public char[] updateBoard(char[] board, int move){
		board[move] = MARK_PLAYER;
		return board;
	}
	
	public double getReward(char[] boardOld, char[] boardNew){
		return 0;
	}
}
