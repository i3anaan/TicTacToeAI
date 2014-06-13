package player;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import Game.Board;

public class QLearningTest {

	@Test
	public void test() {
		QLearningAI ai = new QLearningAI('X');
		Board board = new Board();
		Board board2 = new Board();
		System.out.println(board);
		System.out.println(board2);
		//assertEquals(board,board2);
		//assertEquals(board.hashCode(),board2.hashCode());
		assertEquals(board.hashCode(),(new Board()).hashCode()); 
		ai.updateQValue(board, 1, 2);
		ai.updateQValue(board2, 1, 2);
		
		assertEquals(2.0,ai.getQValue(board, 1),0.1);
		System.out.println(ai.getQValue(board, 1));
		System.out.println(ai.getQValue(board2, 1));
		assertEquals(2.0,ai.getQValue(board2, 1),0.1);
	}

	
	@Test
	public void testHashMap(){
		HashMap<Integer, String> map = new HashMap<Integer,String>();
		Integer integer = new Integer(9);
		map.put(integer, "Bob");
		assertEquals("Bob",map.get(9));
	}
}
