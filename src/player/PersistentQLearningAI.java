package player;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;

import Game.Board;

public class PersistentQLearningAI extends QLearningAI {

	private boolean save;
	public PersistentQLearningAI(char mark,boolean save) {
		super(mark);
		this.save =save;
		try {
			knowledge = loadHashMap();
			System.out.println(Arrays.toString(knowledge.get(new Board())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public int doMove(Board oldBoard){
		int move = super.doMove(oldBoard);
		if(save && movesDone%10000==0){
			try {
				storeHashMap(knowledge);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return move;
	}

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

	private void storeHashMap(HashMap<Board, double[]> map) throws IOException {
		FileOutputStream fileOut = new FileOutputStream("QLearningKnowledge.ai");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(map);
		out.close();
		fileOut.close();
		System.out.println("Saved knowledge HashMap");
	}
}
