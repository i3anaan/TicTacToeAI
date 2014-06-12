package ai;

import java.util.ArrayList;
import java.util.HashMap;

import Game.Board;

public class QLearningAI implements AI{

	private HashMap<Board,double[]> knowledge = new HashMap<Board,double[]>(); //TODO save this knowledge.
	private static final double GAMMA = 0.9;		//Used in QValue function.
	private static final char MARK_PLAYER = 'X';	//The mark this AI has
	private static final char MARK_EMPTY = ' ';		//Empty place mark
	
	public int doMove(Board oldBoard){
		int bestMove = getBestAction(oldBoard); //Calculate best move
		if(Math.random()>0.9){	//Randomness for exploration.
			int moveFound = -1;
			while(moveFound == -1){
				int randomMove = (int)(Math.random()*9);
				if(oldBoard.getIndex(randomMove)==MARK_EMPTY){
					moveFound = randomMove;
				}
			}
			bestMove = moveFound;
		}
		Board newBoard = updateBoard(oldBoard.boardClone(), bestMove);	//Play best move on clone board
		double reward = getReward(oldBoard, newBoard);	//Calculate reward for the transition
		updateQValue(oldBoard,bestMove,reward);	//Update the QValue for the oldBoard + transition just done.
		return bestMove;
	}
	
	
	public double getQValue(Board board, int move){
		return knowledge.get(board)[move];
	}
	
	public void updateQValue(Board board, int move, double reward){
		double[] values =knowledge.get(board);
		values[move] = recalculateQValue(board, move, reward);
		knowledge.put(board, values);
	}
	
	public double recalculateQValue(Board board, int move, double reward){
		Board newBoard = updateBoard(board,move);
		return reward + GAMMA*getQValue(newBoard,getBestAction(newBoard));
	}
	
	public int getBestAction(Board board){
		ArrayList<Integer> bestActions = new ArrayList<Integer>();
		double bestQValue = Double.MIN_VALUE;
		for(int i=0;i<9;i++){
			if(board.getIndex(i)!=MARK_EMPTY){
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
	
	public Board updateBoard(Board board, int move){
		board.doMove(MARK_PLAYER,move);
		return board;
	}
	
	public double getReward(Board boardOld, Board boardNew){
		//TODO
		return 0;
	}
}
