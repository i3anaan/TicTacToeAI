package player;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.junit.Test;

import Game.Board;
import Game.Game;

public class QLearningTest {
	
	@Test
	public void testHashMap() {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		Integer integer = new Integer(9);
		map.put(integer, "Bob");
		assertEquals("Bob", map.get(9));
		
		HashMap<Board, double[]> map2 = new HashMap<Board, double[]>();
		double[] arr1 = new double[] { 1, 2, 3 };
		double[] arr2 = new double[] { 4, 5, 6 };
		Board board1 = new Board();
		Board board2 = new Board();
		board2.doMove('X', 0);

		assertTrue(arr1 != arr2);
		assertTrue(board1 != board2);
		assertFalse(board1.equals(board2));
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

	@SuppressWarnings("unchecked")
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
		char mark = Game.MARK_PLAYER1;
		QLearningAI ai = new QLearningAI(mark);		
		Board board1 = new Board();
		board1.doMove(mark, 0);
		board1.doMove(mark, 1);
		Board board2 = board1.getClone();
		board2.doMove(mark,2);
		
		
		assertEquals(20,ai.getReward(board1, board2),0.1);
		QLearningAI ai2 = new QLearningAI(Game.MARK_PLAYER2);
		assertEquals(-20,ai2.getReward(board1, board2),0.1);
		
		ai.knowledge.put(new Board(), new double[]{0,0,0,5,0,0,4,0,0});
		assertArrayEquals(new double[]{0,0,0,5,0,0,4,0,0},ai.getQValues(new Board()),0.1);
		assertEquals(4,ai.getQValue(new Board(), 6),0.1);
		assertEquals(3,ai.getBestAction(new Board()));
	}
}
