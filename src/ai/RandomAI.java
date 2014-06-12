package ai;

import Game.Board;

public class RandomAI implements AI{

	public int doMove(Board board){
		return (int)Math.random()*9;
	}
}
