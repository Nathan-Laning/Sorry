

import java.io.Serializable;

public class saveGame implements Serializable{
	private static int player_turn;
	private static int userColor;
	private static Pawn[] pawn;


	private static boolean mean;



	private static boolean smart;
	saveGame(Pawn[] PAWNS, int player_turn, int userColor,boolean mean, boolean smart) {
		this.mean=mean;
		this.smart=smart;
		saveGame.player_turn = player_turn;
		this.pawn = PAWNS;
		this.userColor=userColor;
	}
	public static int getUserColor() {
		return userColor;
	}
	public static boolean isSmart() {
		return smart;
	}
	public static int getPlayer_turn() {
		return player_turn;
	}
	public static Pawn[] getPawn() {
		return pawn;
	}
	public static boolean isMean() {
		return mean;
	}


}
