package com.xclenter.test.controller;

public class CallResult {
	private boolean state;
	
	private String message;

	public CallResult(boolean state, String message) {
		super();
		this.state = state;
		this.message = message;
	}

	public CallResult(boolean state) {
		super();
		this.state = state;
		this.message = null;
	}

	public boolean getState() {
		return state;
	}

	public String getMessage() {
		return message;
	}
	
}
