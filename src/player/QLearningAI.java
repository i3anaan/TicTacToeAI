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
	
	
	protected HashMap<Board,double[]> knowledge = new HashMap<Board,double[]>(); //TODO save this knowledge.
	private static final double GAMMA = 0.9;		//Used in QValue function.
	private char mark;	//The mark this AI has
	protected float movesDone = 0;
	
	
	public QLearningAI(char mark){
		this.mark = mark;
	}
	
	public int doMove(Board oldBoard){
		int bestMove = getBestAction(oldBoard); //Calculate best move
		//if(Math.random()>(movesDone/(movesDone+100000))){	//Randomness for exploration.
		/*if(Math.random()>0.8){	//Randomness for exploration.
			int moveFound = -1;
			while(moveFound == -1){
				int randomMove = (int)(Math.random()*9);
				if(oldBoard.getIndex(randomMove)==Game.MARK_EMPTY){
					moveFound = randomMove;
				}
			}
			bestMove = moveFound;
		}else{
		}*/
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
			//System.out.println("No previous Array Found, made new");
		}else{
			//System.out.println("Previous entry found");
		}
		//System.out.println(Arrays.toString(knowledge.get(board)));
		return knowledge.get(board)[move];
	}
	
	public void updateQValue(Board board, int move, double reward){
		double[] values =knowledge.get(board);
		if(values==null){
			values = new double[9];
		}
		values[move] = recalculateQValue(board, move, reward);
		//System.out.println(Arrays.toString(values));
		knowledge.put(board, values);
	}
	
	public double recalculateQValue(Board board, int move, double reward){
		Board newBoard = updateBoard(board.boardClone(),move);
		if(!newBoard.checkGameOver()){
			if(getQValue(newBoard,getBestAction(newBoard))>0){
				//System.out.println("QVALUE EXISTING VALUE DETECTED");
			}
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
		//System.out.println("getBestAction()");
		//System.out.println(board);
		ArrayList<Integer> bestActions = new ArrayList<Integer>();
		double bestQValue = -Double.MAX_VALUE;
		for(int i=0;i<9;i++){
			if(board.getIndex(i)==Game.MARK_EMPTY){
				//System.out.println(bestActions);
				double qValue = getQValue(board, i);
				//System.out.println(qValue);
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
			return 20;
		}else if(boardOld.checkWin()==Game.MARK_EMPTY && boardNew.checkWin()==Game.MARK_PLAYER2){
			return 0;
		}
		return 0;
	}
	
	@Override
	public String toString(){
		return "QLearningAI";
	}
}
