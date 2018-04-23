

import java.io.Serializable;

public class saveGame implements Serializable{
	int x,y;
	public static int player_turn;
	gameBoard gameBoard;
	Pawn pawn;
	public saveGame(int x,int y,int player_turn) {
		this.x = x;
		this.y = y;
		saveGame.player_turn = player_turn;
		
		
	}
	
	
}
