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

public class QLearningTest {

	@Test
	public void test() {
		QLearningAI ai = new QLearningAI('X');
		Board board = new Board();
		Board board2 = new Board();
		// assertEquals(board,board2);
		// assertEquals(board.hashCode(),board2.hashCode());
		assertEquals(board.hashCode(), (new Board()).hashCode());
		ai.updateQValue(board, 1, 2);
		ai.updateQValue(board2, 1, 2);

		assertEquals(2.0, ai.getQValue(board, 1), 0.1);
		assertEquals(2.0, ai.getQValue(board2, 1), 0.1);
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

		/*
		try {
			FileOutputStream fileOut = new FileOutputStream(
					"QLearningKnowledge.ai");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(map);
			out.close();
			fileOut.close();
			System.out.println("Written map.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		 */
		map.clear();
		
		try {
			FileInputStream fileIn = new FileInputStream(
					"QLearningKnowledge.ai");
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

		//assertEquals(1,map.get(new Board())[0],0.1);
		//assertEquals(2, map.get(board2)[0],0.1);
		for(Board b : map.keySet()){
			System.out.println(Arrays.toString(map.get(b)));
		}
	}
}
