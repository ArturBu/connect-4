package com.game.rest;

import java.security.Principal;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.game.model.exception.GameplayException;
import com.game.model.exception.WrongPlayerNameException;
import com.game.model.game.ConnectFourGameplay;
import com.game.model.game.GameMove;
import com.game.model.openapi.GameState;
import com.game.model.openapi.GameStateEnum;

/**
 * Main REST controller for Connect-4 game.
 * 
 * @author Artur
 *
 */
@Controller
@RequestMapping("/game")
public class GameController {

	final static Logger logger = LoggerFactory.getLogger(GameController.class);

	/**
	 * Contains active games. It can be saved in DB.
	 */
	final HashMap<String, ConnectFourGameplay> activeGames = new HashMap<String, ConnectFourGameplay>();

	/**
	 * Start point for the game.
	 * 
	 * @param principal
	 * @param playerOneName
	 * @param playerTwoName
	 * @return
	 * @throws WrongPlayerNameException
	 */
	@RequestMapping(value = "/start", method = RequestMethod.GET)
	public ResponseEntity<GameState> startGame(Principal principal, @RequestParam String playerOneName,
			@RequestParam String playerTwoName) {
		final String name = principal.getName();
		ConnectFourGameplay game = activeGames.get(name);
		if (game == null || game.getGameState().getGameStateEnum() == GameStateEnum.FINISHED) {
			try {
				game = new ConnectFourGameplay(playerOneName, playerTwoName);
			} catch (WrongPlayerNameException e) {
				logger.error(e.getMessage(), e);
				return new ResponseEntity<GameState>(new GameState(GameStateEnum.FAILED_TO_CREATE, e.getMessage()),
						HttpStatus.BAD_REQUEST);
			}
			game.startGame();
			activeGames.put(name, game);
			logger.info("Created new game for user: {} with players: {} and {}", name, playerOneName, playerTwoName);
		}

		return new ResponseEntity<GameState>(game.getGameState(), HttpStatus.CREATED);
	}

	/**
	 * First game has to be created before you can do a move.
	 * 
	 * @param principal
	 * @param playerName
	 * @param columnId
	 * @return
	 */
	@RequestMapping("/move/{playerName}/{columnId}")
	public ResponseEntity<GameState> doMove(Principal principal, @PathVariable String playerName,
			@PathVariable int columnId) {
		final String name = principal.getName();
		ConnectFourGameplay connectFourGameplay = activeGames.get(name);
		if (connectFourGameplay == null) {
			logger.debug("No game found for user {}", name);
			return new ResponseEntity<GameState>(new GameState(GameStateEnum.FAILED_TO_MOVE, "First start the game!"),
					HttpStatus.BAD_REQUEST);
		}

		try {
			connectFourGameplay.doMove(new GameMove(columnId, connectFourGameplay.getPlayerByName(playerName)));
		} catch (GameplayException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<GameState>(new GameState(GameStateEnum.FAILED_TO_MOVE, e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<GameState>(connectFourGameplay.getGameState(), HttpStatus.ACCEPTED);
	}
}
