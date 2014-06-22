package player;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import Game.Board;

/**
 * Extension of the QLearningAI.
 * This extension allows the AI to store and load its knowledge table.
 * It does not alter the QLearningAI's behavior.
 * @author I3anaan
 *
 */
public class PersistentQLearningAI extends QLearningAI {

	private boolean save;

	/**
	 * Constructor
	 * @param mark	The mark this Player has.
	 * @param load	Whether or not to load from an existing QValue knowledge HashMap.
	 * @param save	Whether or not to save to an existing QValue knowledge HashMap.
	 */
	public PersistentQLearningAI(char mark, boolean load, boolean save) {
		super(mark);
		this.save = save;
		if (load) {
			try {
				knowledge = loadHashMap();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Board movesDoneBoard = new Board();
			for (int i = 0; i < 9; i++) {
				movesDoneBoard.doMove(mark, i);
			}
			double[] arr = knowledge.get(movesDoneBoard);
			if (arr != null) {
				movesDone = (int) arr[0];
			}
		}
	}

	@Override
	public int doMove(Board oldBoard) {
		int move = super.doMove(oldBoard);
		if (save && movesDone % 10000 == 0) {
			//Save every 10.000 moves
			try {
				storeHashMap(knowledge);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return move;
	}

	/**
	 * Loads in an existing QValue knowledge HashMap.
	 * Loads the file 'QLearningKnowledge.ai'
	 * @return	The existing QValue knowledge HashMap
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private HashMap<Board, double[]> loadHashMap() throws IOException,
			ClassNotFoundException {
		HashMap<Board, double[]> map;
		FileInputStream fileIn = new FileInputStream("QLearningKnowledge.ai");
		ObjectInputStream in = new ObjectInputStream(fileIn);
		map = (HashMap<Board, double[]>) in.readObject();
		in.close();
		fileIn.close();
		System.out.println("Loaded knowledge HashMap");

		return map;
	}

	/**
	 * Stores a QValue knowledge HashMap.
	 * stores in the file 'QLearningKnowledge.ai'
	 * @param map QValue knowledge HashMap to store.
	 * @throws IOException
	 */
	private void storeHashMap(HashMap<Board, double[]> map) throws IOException {
		Board movesDoneBoard = new Board();
		for (int i = 0; i < 9; i++) {
			movesDoneBoard.doMove(mark, i);
		}
		double[] arr = new double[] { movesDone, 0, 0, 0, 0, 0, 0, 0, 0 };
		knowledge.put(movesDoneBoard, arr);

		FileOutputStream fileOut = new FileOutputStream("QLearningKnowledge.ai");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(map);
		out.close();
		fileOut.close();
		//System.out.println("Saved knowledge HashMap");
	}
}
