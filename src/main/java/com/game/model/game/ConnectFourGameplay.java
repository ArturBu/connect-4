package com.game.model.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.model.exception.GameplayException;
import com.game.model.exception.WrongPlayerNameException;
import com.game.model.openapi.GameState;
import com.game.model.openapi.GameStateEnum;

/**
 * The game manager class.
 * 
 * @author Artur
 *
 */
public class ConnectFourGameplay {

	final static Logger logger = LoggerFactory.getLogger(ConnectFourGameplay.class);

	
	public static final int GAME_BOARD_WIDTH = 7;
	public static final int GAME_BOARD_HEIGHT = 6;
	public static final int MOVES_TO_WIN = 4;
	private final Player playerOne;
	private final Player playerTwo;
	private Player playerToMove;
	private final int[][] boardGame;
	private final GameState gameState;
	private final List<GameMove> moveHistory = new ArrayList<>();

	public ConnectFourGameplay(String playerOneName, String playerTwoName) throws WrongPlayerNameException {
		if (playerOneName == null || playerTwoName == null) {
			throw new WrongPlayerNameException("All players names has to be set!");
		}
		if (playerOneName.equals(playerTwoName)) {
			throw new WrongPlayerNameException("Players cannot have the same names!");
		}
		this.playerOne = new Player(MoveTypeEnum.PLAYER_ONE, playerOneName);
		this.playerTwo = new Player(MoveTypeEnum.PLAYER_TWO, playerTwoName);
		this.boardGame = new int[GAME_BOARD_WIDTH][GAME_BOARD_HEIGHT];
		this.gameState = new GameState(GameStateEnum.CREATED, "Game created!");
	}

	public boolean startGame() {
		this.playerToMove = playerOne;
		this.gameState.setGameState(GameStateEnum.STARTED);
		prepareBoardGame();
		setMessage();
		return true;
	}

	/**
	 * The board will be filled up with the empty values.
	 * 
	 * @see com.game.model.game.MoveTypeEnum.EMPTY.move
	 * 
	 */
	private void prepareBoardGame() {
		for (int[] row : boardGame) {
			Arrays.fill(row, MoveTypeEnum.EMPTY.move);
		}
	}

	public synchronized GameState doMove(GameMove gameMove) throws GameplayException {
		if (gameState.getGameStateEnum() != GameStateEnum.STARTED) {
			String message = "Game not started!";
			if (gameState.getGameStateEnum() == GameStateEnum.FINISHED) {
				message = "Game is finished! The winner is: " + moveHistory.get(moveHistory.size()-1).getPlayer().getPlayerName();
			}
			throw new GameplayException(message);
		}

		if (gameMove.getMoveColumnId() >= GAME_BOARD_WIDTH) {
			throw new GameplayException("Wrong move! The game board has maximal width: " + GAME_BOARD_WIDTH);
		}

		if (gameMove.getPlayer().getMoveType() != this.playerToMove.getMoveType()) {
			throw new GameplayException(this.playerToMove.getPlayerName() + " has to make a move!");
		}

		return executeMove(gameMove.getMoveColumnId());
	}

	private GameState executeMove(final int columnID) throws GameplayException {
		int rowToInsert = getCoinFellRow(columnID);
		if (rowToInsert < 0) {
			String message = "Wrong move! This column is already Full. Choose another one.";
			gameState.setMessage(message);
			throw new GameplayException(message);
		}

		boardGame[columnID][rowToInsert] = playerToMove.getMoveType().move;
		GameMove gameMove = new GameMove(playerToMove, rowToInsert, columnID, moveHistory.size() + 1);
		moveHistory.add(gameMove);
		if (checkGameState(gameMove) == GameStateEnum.FINISHED) {
			gameState.setGameState(GameStateEnum.FINISHED);
			gameState.setMessage("Game finished. The winner is: " + playerToMove.getPlayerName());
			return gameState;
		}
		logger.debug("Executed move for player {}", playerToMove.getPlayerName());
		forwardMoveToNextPlayer();
		setMessage();		
		return gameState;
	}

	/**
	 * Check if last move was the winning move.
	 * 
	 * @param gameMove
	 * @return if true then the last move was the winning move.
	 */
	private GameStateEnum checkGameState(GameMove gameMove) {

		List<int[]> arraysToCheck = new ArrayList<>();
		int[] columnsToCheck = boardGame[gameMove.getMoveColumnId()];
		int[] rowToCheck = new int[GAME_BOARD_WIDTH];
		int[] diagonal1ToCheck = new int[GAME_BOARD_WIDTH];
		int[] diagonal2ToCheck = new int[GAME_BOARD_WIDTH];
		arraysToCheck.add(columnsToCheck);
		arraysToCheck.add(rowToCheck);
		arraysToCheck.add(diagonal1ToCheck);
		arraysToCheck.add(diagonal2ToCheck);

		for (int i = 0; i < GAME_BOARD_WIDTH; i++) {
			rowToCheck[i] = boardGame[i][gameMove.getMoveRowId()];
		}

		int startRow1 = gameMove.getMoveRowId();
		int startRow2 = gameMove.getMoveRowId();
		for (int startColumn = gameMove
				.getMoveColumnId(); startColumn < GAME_BOARD_WIDTH; startColumn++, startRow1++, startRow2--) {
			if (startRow1 < GAME_BOARD_HEIGHT) {
				diagonal1ToCheck[startColumn] = boardGame[startColumn][startRow1];
			}
			if (startRow2 >= 0) {
				diagonal2ToCheck[startColumn] = boardGame[startColumn][startRow2];
			}
		}
		startRow1 = gameMove.getMoveRowId();
		startRow2 = gameMove.getMoveRowId();
		for (int startColumn = gameMove.getMoveColumnId()
				- 1; startColumn >= 0; startColumn--, startRow1--, startRow2++) {
			if (startRow1 >= 0) {
				diagonal1ToCheck[startColumn] = boardGame[startColumn][startRow1];
			}
			if (startRow2 < GAME_BOARD_HEIGHT) {
				diagonal2ToCheck[startColumn] = boardGame[startColumn][startRow2];
			}
		}

		for (int[] array : arraysToCheck) {
			if (compareStates(array)) {
				return GameStateEnum.FINISHED;
			}
		}
		return GameStateEnum.STARTED;
	}

	private boolean compareStates(int[] array) {
		if (array.length < MOVES_TO_WIN) {
			return false;
		}

		int lastElement = array[0];
		int[] arrayToCompare = Arrays.copyOfRange(array, 1, array.length);
		int counter = 1;
		for (int element : arrayToCompare) {
			if (lastElement == element && lastElement != MoveTypeEnum.EMPTY.move) {
				counter++;
				if (counter == MOVES_TO_WIN) {
					return true;
				}
			} else {
				counter = 1;
			}
			lastElement = element;
		}
		return false;
	}

	private void forwardMoveToNextPlayer() {
		switch (playerToMove.getMoveType()) {
		case PLAYER_ONE:
			playerToMove = playerTwo;
			break;
		case PLAYER_TWO:
			playerToMove = playerOne;
			break;
		default:
			break;
		}
	}

	private int getCoinFellRow(final int columnID) {
		int rowID = -1;
		for (int i = 0; i < GAME_BOARD_HEIGHT; i++) {
			int field = boardGame[columnID][i];

			if (field != MoveTypeEnum.EMPTY.move) {
				return rowID;
			}
			rowID = i;
		}
		return rowID;
	}

	/**
	 * Just to demonstrate some frontend messages.
	 */
	private void setMessage() {
		this.gameState.setMessage("Waiting for player: " + playerToMove.getPlayerName() + " to move.");
	}

	public Player getPlayerOne() {
		return playerOne;
	}

	public Player getPlayerTwo() {
		return playerTwo;
	}

	public GameState getGameState() {
		return gameState;
	}

	public Player getPlayerToMove() {
		return playerToMove;
	}

	public void setPlayerToMove(Player playerToMove) {
		this.playerToMove = playerToMove;
	}

	public List<GameMove> getMoveHistory() {
		return moveHistory;
	}

	public Player getPlayerByName(final String playerName) throws GameplayException {
		if (playerOne.getPlayerName().equals(playerName)) {
			return playerOne;
		} else if (playerTwo.getPlayerName().equals(playerName)) {
			return playerTwo;
		} else {
			throw new GameplayException(playerName + " player not found in this game!");
		}
	}
}
