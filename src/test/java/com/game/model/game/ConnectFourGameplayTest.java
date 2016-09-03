package com.game.model.game;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.game.model.exception.GameplayException;
import com.game.model.exception.WrongPlayerNameException;
import com.game.model.openapi.GameState;
import com.game.model.openapi.GameStateEnum;

public class ConnectFourGameplayTest {

	private ConnectFourGameplay connectFour;

	@Before
	public void setUp() throws WrongPlayerNameException {
		connectFour = new ConnectFourGameplay("Player1", "Player2");
	}

	@Test
	public void gameplayTest() throws GameplayException, WrongPlayerNameException {
		setUp();
		connectFour.startGame();
		connectFour.doMove(new GameMove(0, connectFour.getPlayerOne()));
		connectFour.doMove(new GameMove(1, connectFour.getPlayerTwo()));
		List<GameMove> moveHistory = connectFour.getMoveHistory();
		Assert.assertTrue(moveHistory.size() == 2);
	}

	@Test
	public void winningGameTest() throws GameplayException, WrongPlayerNameException {
		
		// horizontal
		setUp();
		connectFour.startGame();		
		Player playerOne = connectFour.getPlayerOne();
		Player playerTwo = connectFour.getPlayerTwo();
		for(int i = 0; i < ConnectFourGameplay.MOVES_TO_WIN-1; i++) {
			GameState state1 = connectFour.doMove(new GameMove(1, playerOne));
			GameState state2 = connectFour.doMove(new GameMove(0, playerTwo));
			Assert.assertTrue(state1.getGameStateEnum() == GameStateEnum.STARTED);
			Assert.assertTrue(state2.getGameStateEnum() == GameStateEnum.STARTED);
		}
		GameState winState = connectFour.doMove(new GameMove(1, playerOne));
		Assert.assertTrue(winState.getGameStateEnum() == GameStateEnum.FINISHED);
		
		//vertical
		setUp();
		connectFour.startGame();
		int i = 0;
		for(; i < ConnectFourGameplay.MOVES_TO_WIN-1; i++) {
			GameState state1 = connectFour.doMove(new GameMove(i, playerOne));
			GameState state2 = connectFour.doMove(new GameMove(i, playerTwo));
			Assert.assertTrue(state1.getGameStateEnum() == GameStateEnum.STARTED);
			Assert.assertTrue(state2.getGameStateEnum() == GameStateEnum.STARTED);
		}
		winState = connectFour.doMove(new GameMove(i, playerOne));
		Assert.assertTrue(winState.getGameStateEnum() == GameStateEnum.FINISHED);
		
		//diagonal
		setUp();
		connectFour.startGame();
		
		i = 0;
		while(i < ConnectFourGameplay.MOVES_TO_WIN-1) {			
			for(int ii = 0; ii < ConnectFourGameplay.GAME_BOARD_WIDTH; ii++) {
				GameMove gameMove = new GameMove(ii,connectFour.getPlayerToMove());
				connectFour.doMove(gameMove);
			}
			i++;
		}
		winState = connectFour.doMove(new GameMove(0, connectFour.getPlayerToMove()));
		Assert.assertTrue(winState.getGameStateEnum() == GameStateEnum.FINISHED);	
	}

	@Test(expected = GameplayException.class)
	public void playerForwardRoundTest() throws GameplayException, WrongPlayerNameException {
		final String playerOneName = "player1";
		final String playerTwoName = "player2";
		ConnectFourGameplay game = new ConnectFourGameplay(playerOneName, playerTwoName);
		game.startGame();
		Assert.assertTrue(game.getPlayerToMove().getPlayerName().equals(game.getPlayerOne().getPlayerName()));
		GameMove gameMove = new GameMove(0, game.getPlayerOne());
		game.doMove(gameMove);
		Assert.assertTrue(game.getPlayerToMove().getPlayerName().equals(playerTwoName));
		game.doMove(gameMove); // same player tries to do again move
	}

	@Test(expected = GameplayException.class)
	public void gameMovesTest() throws GameplayException, WrongPlayerNameException {
		ConnectFourGameplay game = new ConnectFourGameplay("player1", "player2");
		game.startGame();
		GameMove gameMove1 = new GameMove(0, game.getPlayerOne());
		GameMove gameMove2 = new GameMove(0, game.getPlayerTwo());
		for (int i = 0; i < ConnectFourGameplay.GAME_BOARD_HEIGHT; i += 2) {
			game.doMove(gameMove1);
			game.doMove(gameMove2);
		}

		game.doMove(gameMove1); // one move too much
	}

	@Test(expected = GameplayException.class)
	public void gameStartStatusTest() throws GameplayException, WrongPlayerNameException {
		ConnectFourGameplay game = new ConnectFourGameplay("player1", "player2");
		game.doMove(new GameMove(game.getPlayerOne().getMoveType().move, game.getPlayerOne()));
	}

	@Test(expected = WrongPlayerNameException.class)
	public void gameCreationPlayersSameNameTest() throws WrongPlayerNameException {
		new ConnectFourGameplay("player1", "player1");
	}

	@Test(expected = WrongPlayerNameException.class)
	public void gameCreationNoPlayerNameSetTest() throws WrongPlayerNameException {
		new ConnectFourGameplay(null, "player1");
	}

}
