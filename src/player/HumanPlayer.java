package player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Game.Board;

public class HumanPlayer implements Player {

	@Override
	public int doMove(Board board) {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		int move = -1;
		while(move==-1){
		    try {
				String s = bufferRead.readLine();
				move = Integer.parseInt(s);
				if(move<0 && move>8){
					move = -1;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}catch(NumberFormatException e){
				System.out.println("Enter a single digit number in the range [0,9[");
			}
		}
		return move;
	}

}
