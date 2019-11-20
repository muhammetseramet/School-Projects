import java.util.Scanner;

public class Monopoly {
	PairOfDice die = new PairOfDice();
	Board board;
	
	public Monopoly(int totalPlayers) {
		board = new Board(totalPlayers); 
	}
	
	public static void main(String[] args) {
                System.out.println("--- MONOPOLY GAME STARTED ---");
		Scanner scanner = new Scanner(System.in);  //Scanner for read number of players from user
		int totalPlayersNumber = 0; //how many player will play the game
		while (totalPlayersNumber < 2 || totalPlayersNumber > 8) {
			try {
				System.out.println("How many players will play?");
				System.out.print("Please choose total player number between 2 and 8 : ");
				totalPlayersNumber = scanner.nextInt(); //read number of players from user
			}
			catch(Exception e) {
				System.err.println("Please Enter a Valid Input!");
				continue;
			}
			if(totalPlayersNumber > 8) {
				System.err.println("Please Enter a Valid Input!");
			}
		}
		scanner.close();
		Monopoly game = new Monopoly(totalPlayersNumber);
		game.startGame();
	}
	
	public void startGame() {
		//control if game ended
		while (board.activePlayers != 1 && isGameOver() == false){
			if(board.getCurrentPlayer().isBankrupt() == false){
				//call face values of dice and move player with that value
				int faceValue = board.getCurrentPlayer().tossDie();
				board.movePlayer(board.getCurrentPlayer(), faceValue);
			}
			board.nextTurn();  //other player continues to play
                        board.decideWinner(); //end the game if a player won
		}    
	}
	
	public boolean isGameOver() {  //control bankrupts to end game
	    for(Player player : board.getAllPlayers()){  //all players loop
		if(player.isBankrupt() == false){ 
                    return false; //continue to play
                }
            }
        return true;
	}
}
