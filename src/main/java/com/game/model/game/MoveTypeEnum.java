package com.game.model.game;

import java.util.HashMap;
import java.util.Map;

/**
 * Each player move should be identificated by one of MoveTypeEnum.
 * 
 * @author Artur
 *
 */
public enum MoveTypeEnum {

	PLAYER_ONE(1), 
	PLAYER_TWO(2),
	/**
	* Describes the empty field on game board.
	*/
	EMPTY(0);

	/**
	 * Describes the player move ID in board.
	 */
	public int move;

	private String playerName;

	MoveTypeEnum(int move) {
		this.move = move;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	private static final Map<Integer, MoveTypeEnum> playerValueMap = new HashMap<Integer, MoveTypeEnum>();
	static {
		playerValueMap.put(PLAYER_ONE.move, PLAYER_ONE);
		playerValueMap.put(PLAYER_TWO.move, PLAYER_TWO);
		playerValueMap.put(EMPTY.move, EMPTY);
	}

	public static MoveTypeEnum getPlayerByValue(final int key) {
		return playerValueMap.get(key);
	}

}
