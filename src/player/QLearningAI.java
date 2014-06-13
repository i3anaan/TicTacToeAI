package player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Game.Board;
import Game.Game;

public class QLearningAI implements Player{

	private HashMap<Board,double[]> knowledge = new HashMap<Board,double[]>(); //TODO save this knowledge.
	private static final double GAMMA = 0.9;		//Used in QValue function.
	private char mark;	//The mark this AI has
	private float movesDone = 0;
	
	
	public QLearningAI(char mark){
		this.mark = mark;
	}
	
	public int doMove(Board oldBoard){
		int bestMove = getBestAction(oldBoard); //Calculate best move
		if(Math.random()>(movesDone/(movesDone+100000))){	//Randomness for exploration.
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
		Board newBoard = updateBoard(oldBoard.boardClone(), bestMove);	//Play best move on clone board
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

		System.out.println("OldValues = "+Arrays.toString(values));
		if(values==null){
			System.out.println("Values == null");
			values = new double[9];
		}
		values[move] = recalculateQValue(board, move, reward);
		//System.out.println(Arrays.toString(values));
		knowledge.put(board, values);
	}
	
	public double recalculateQValue(Board board, int move, double reward){
		Board newBoard = updateBoard(board,move);
		if(!board.checkGameOver()){
			System.out.println("value for next state = "+getQValue(newBoard,getBestAction(newBoard)));
			return reward + GAMMA*getQValue(newBoard,getBestAction(newBoard));
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
	
	public Board updateBoard(Board board, int move){
		board.doMove(mark,move);
		return board;
	}
	
	public double getReward(Board boardOld, Board boardNew){
		if(boardOld.checkWin()==Game.MARK_EMPTY && boardNew.checkWin()==Game.MARK_PLAYER1){
			return 10;
		}else if(boardOld.checkWin()==Game.MARK_EMPTY && boardNew.checkWin()==Game.MARK_PLAYER2){
			return -10;
		}
		return 0;
	}
}
