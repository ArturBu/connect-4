package com.game.model.game;

/** 
 * @author Artur
 *
 */
public class Player {

	private final MoveTypeEnum player;
	private final String playeName;

	/**
	 * @param player
	 * @param playeName
	 */
	public Player(MoveTypeEnum player, String playeName) {
		super();
		this.player = player;
		this.playeName = playeName;
	}

	public MoveTypeEnum getMoveType() {
		return player;
	}

	public String getPlayerName() {
		return playeName;
	}

}
