package com.game.model.game;

/**
 * Moves on game board are described by this class.
 * 
 * @author Artur
 *
 */
public class GameMove {

	private Player player;
	private int moveRowId;
	private int moveColumnId;
	private int moveNo;

	/**
	 * @param player
	 * @param moveRowId
	 * @param moveY
	 * @param moveId
	 */
	public GameMove(Player player, int moveRowId, int moveColumnId, int moveNo) {
		this.player = player;
		this.moveRowId = moveRowId;
		this.moveColumnId = moveColumnId;
		this.moveNo = moveNo;
	}

	public GameMove(int moveColumnId, Player player) {
		super();
		this.player = player;
		this.moveColumnId = moveColumnId;
	}

	public Player getPlayer() {
		return player;
	}

	public int getMoveRowId() {
		return moveRowId;
	}

	public int getMoveColumnId() {
		return moveColumnId;
	}

	public int getMoveNo() {
		return moveNo;
	}

}
