package com.xclenter.test.dao;

public class CallResult {
	private boolean state;

	private String message;

	private Object data;

	public CallResult(boolean state, String message) {
		super();
		this.state = state;
		this.message = message;
	}

	public CallResult(boolean state, String message, Object data) {
		super();
		this.state = state;
		this.message = message;
		this.data = data;
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
	
	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

}
