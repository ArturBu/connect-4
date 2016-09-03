package com.game.model.exception;

public class WrongPlayerNameException extends Exception {

	private static final long serialVersionUID = -990787760205387262L;

	public WrongPlayerNameException(final String message) {
		super(message);
	}

}
