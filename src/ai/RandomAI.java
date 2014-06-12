package ai;

public class RandomAI implements AI{

	public int doMove(char[] board){
		return (int)Math.random()*9;
	}
}
