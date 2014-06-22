package player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Game.Board;

/**
 * A human player.
 * When this Player gets asked for a move, input is requested on the standard input.
 * @author I3anaan
 *
 */
public class HumanPlayer implements Player {

	@Override
	public int doMove(Board board) {
		System.out.println(board);
		System.out.print("Enter your move: ");
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

	@Override
	public void endOfGame(Board board) {
		System.out.println("Game over, winning mark: "+board.getWinner());
		System.out.println(board);
	}

	@Override
	public void startOfGame() {
	}

}
