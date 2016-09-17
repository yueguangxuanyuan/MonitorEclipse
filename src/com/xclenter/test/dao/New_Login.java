package com.xclenter.test.dao;

public class New_Login {
	boolean new_login;
	String user_key;

	public New_Login(boolean new_login, String user_key) {
		super();
		this.new_login = new_login;
		this.user_key = user_key;
	}

	public boolean isNew_login() {
		return new_login;
	}

	public String getUser_key() {
		return user_key;
	}

}
