package boterKaasBier;

import java.util.ArrayList;
import java.util.List;

import boterKaasBier.players.Player;
import boterKaasBier.players.RandomAI;

public class Game {
    private List<Player> players;
    private Board board;
    private int wins;
    private int draws;
    
    public static void main(String[] args) {
        List<Player> players = new ArrayList<>();
        players.add(new RandomAI());
        players.add(new RandomAI());
        players.add(new RandomAI());
        
        Game game = new Game(players);
        //game.playGame();
        game.playGames(10000);
    }
    
    
    public Game(List<Player> players) {
        this.players = players;
    }
    
    public void playGames(int amount) {
        int printThreshold = amount / 20;
        int printCounter = 0;
        for (int i = 0; i < amount; i++) {
            this.playGame();
            printCounter++;
            if (printCounter >= printThreshold) {
                printCounter = 0;
                System.out.println(String.format("Games played: %d, wins: %d, draws: %d", i + 1, wins, draws));
            }
        }
    }
    
    public void playGame() {
        board = new Board();
        boolean gameOver = false;
        int playerTurn = -1; // will start at 0;
        int gameTurn = 0;
        
        while (!gameOver) {
            //board.printState();
            gameTurn++;
            playerTurn = (playerTurn + 1) % players.size();

            //System.out.println("Turn: " + gameTurn + " | Player " + (playerTurn + 1) + " | Deck " + board.getDeck());
            Player player = players.get(playerTurn);
            gameOver = playTurn(player);
        }
        //board.printState();
        
        if (board.isWon()) {
            wins++;
            //System.out.println("Player " + (playerTurn + 1) + " (" + board.getWinner() + ")  Has won!");
        } else {
            draws++;
            //System.out.println("Nobody won, game ended in a draw.");
        }
    }
    
    private boolean playTurn(Player player) {
        player.doGuessMove(board);
        return board.isFinished();
    }
}
