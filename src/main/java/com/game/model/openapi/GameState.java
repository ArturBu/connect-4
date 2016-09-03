package com.game.model.openapi;

/**
 * Will be send to player as json. In the next improvement all communication
 * between player and server would be in json.
 * 
 * @author Artur
 *
 */
public class GameState {

	private GameStateEnum gameState;
	/**
	 * Just to demonstrate some frontend messages.
	 */
	private String message;

	/**
	 * @param gameState
	 * @param message
	 */
	public GameState(GameStateEnum gameState, String message) {
		super();
		this.gameState = gameState;
		this.message = message;
	}

	public GameStateEnum getGameStateEnum() {
		return gameState;
	}

	public void setGameState(GameStateEnum gameState) {
		this.gameState = gameState;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
