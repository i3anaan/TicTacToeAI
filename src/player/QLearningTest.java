package player;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

import Game.Board;
import Game.Game;

public class QLearningTest {

	@Test
	public void test() {
		QLearningAI ai = new QLearningAI(Game.MARK_PLAYER1);
		Board board = new Board();
		Board board2 = new Board();
		assertEquals(board.hashCode(), (new Board()).hashCode());
		ai.updateQValue(board, 1, 2);
		ai.updateQValue(board2, 1, 2);

		assertEquals(2.0, ai.getQValue(board, 1,1,0), 0.1);
		assertEquals(2.0, ai.getQValue(board2, 1,1,0), 0.1);
	}

	@Test
	public void testHashMap() {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		Integer integer = new Integer(9);
		map.put(integer, "Bob");
		assertEquals("Bob", map.get(9));

		HashMap<Integer, double[]> map1 = new HashMap<Integer, double[]>();
		HashMap<Board, double[]> map2 = new HashMap<Board, double[]>();
		double[] arr1 = new double[] { 1, 2, 3 };
		double[] arr2 = new double[] { 4, 5, 6 };
		Board board1 = new Board();
		Board board2 = new Board();
		board2.doMove('X', 0);

		assertTrue(arr1 != arr2);
		assertTrue(board1 != board2);
		assertFalse(board1.equals(board2));
		// assertEquals(board1.hashCode(),board2.hashCode());
		map2.put(board1, arr1);
		map2.put(board2, arr2);
		assertEquals(map2.get(board1), arr1);
		assertEquals(map2.get(new Board()), arr1);
		assertEquals(map2.get(board2), arr2);
	}

	@Test
	public void testCharEquals() {
		assertFalse(new char[] { 'a', 'b', 'c' }.equals(new char[] { 'a', 'b',
				'c' }));
		assertEquals(new String(new char[] { 'a', 'b', 'c' }), new String(
				new char[] { 'a', 'b', 'c' }));
		assertEquals(new String(new char[] { 'a', 'b', 'c' }).hashCode(),
				new String(new char[] { 'a', 'b', 'c' }).hashCode());
	}

	@Test
	public void testSerialization() {
		HashMap<Board, double[]> map = new HashMap<Board, double[]>();
		map.put(new Board(), new double[] { 1 });
		Board board2 = new Board();
		board2.doMove('X', 3);
		map.put(board2, new double[] { 2 });

		
		try {
			FileOutputStream fileOut = new FileOutputStream(
					"QLearningKnowledgeTest.ai");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(map);
			out.close();
			fileOut.close();
			System.out.println("Written map.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		map.clear();
		
		try {
			FileInputStream fileIn = new FileInputStream(
					"QLearningKnowledgeTest.ai");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			map = (HashMap<Board, double[]>) in.readObject();
			in.close();
			fileIn.close();
			System.out.println("FileRead");
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("class not found");
			c.printStackTrace();
			return;
		}

		assertEquals(1,map.get(new Board())[0],0.1);
		assertEquals(2, map.get(board2)[0],0.1);
	}
	
	
	@Test
	public void testQValueAlgorithm(){
		Board board = new Board();
		char mark = Game.MARK_PLAYER1;
		QLearningAI ai = new QLearningAI(mark);
		assertEquals(Game.MARK_EMPTY,board.getIndex(3));
		ai.updateBoard(board, 3);
		assertEquals(mark,board.getIndex(3));
		
		Board board1 = new Board();
		board1.doMove(mark, 0);
		board1.doMove(mark, 1);
		Board board2 = board1.getClone();
		board2.doMove(mark,2);
		
		
		assertEquals(20,ai.getReward(board1, board2),0.1);
		QLearningAI ai2 = new QLearningAI(Game.MARK_PLAYER2);
		assertEquals(20,ai2.getReward(board1, board2),0.1);
		
		
		
		//System.out.println("\n\n\nTESTING STORING QVALUES");
		ai = new QLearningAI(mark);
		assertEquals(10, ai.recalculateQValue(new Board(), 3, 10),0.1);
		board = new Board();
		board.doMove(mark, 0);
		//System.out.println("Made board:\n"+board);
		ai.updateQValue(board, 1, 100);
		ai.updateQValue(new Board(), 0, 0);
		System.out.println("Asking QValue1");
		assertEquals(100,ai.knowledge.get(board)[1],0.1);
		System.out.println("Asking QValue2");
		//assertEquals(100*ai.GAMMA,ai.getQValue(new Board(), 0,1,1),0.1);
		
		
		ai = new QLearningAI(mark);
		Board boardWon = new Board();
		boardWon.doMove(mark, 0);
		boardWon.doMove(mark, 1);
		boardWon.doMove(mark, 2);
		Board board2Moves = new Board();
		board2Moves.doMove(mark, 0);
		board2Moves.doMove(mark, 1);
		Board board1Moves = new Board();
		board1Moves.doMove(mark, 0);
		Board boardEmpty = new Board();
		ai.updateQValue(board2Moves, 2, ai.getReward(board2Moves, boardWon));
		ai.updateQValue(board1Moves, 1, ai.getReward(board1Moves, board2Moves));
		ai.updateQValue(boardEmpty, 0, ai.getReward(boardEmpty, board1Moves));
		System.out.println(Arrays.toString(ai.knowledge.get(boardWon)));
		System.out.println(Arrays.toString(ai.knowledge.get(board2Moves)));
		System.out.println(Arrays.toString(ai.knowledge.get(board1Moves)));
		System.out.println(Arrays.toString(ai.knowledge.get(boardEmpty)));
		
		assertEquals(ai.getReward(board2Moves, boardWon),ai.knowledge.get(board2Moves)[2],0.1);
		//assertEquals(ai.GAMMA*ai.getReward(board2Moves, boardWon),ai.knowledge.get( board1Moves)[1],0.1);
		//assertEquals(ai.GAMMA*ai.GAMMA*ai.getReward(board2Moves, boardWon),ai.knowledge.get(boardEmpty)[0],0.1);
		
		
	}
}
