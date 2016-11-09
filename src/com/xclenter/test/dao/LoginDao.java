package com.xclenter.test.dao;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.xclenter.test.util.HttpCommon;
import com.xclenter.test.util.MAC_Address;
import com.xclenter.test.util.ServerInfo;
import com.xclenter.test.util.action.LoginAuth;
import com.xclenter.test.util.file.SaveFileUtil;

public class LoginDao {

	private static LoginDao loginDao;

	private LoginDao() {

	}

	public static LoginDao getLoginDao() {
		if (loginDao == null) {
			loginDao = new LoginDao();
		}
		return loginDao;
	}

	public CallResult login(String username, String password) {
		boolean state = false;
		String message = null;
		Object data = null; //����new_Login
		Map<String, String> loginMesssage = new HashMap<>();

		String user_key = LoginAuth.getUser_key();

		loginMesssage.put("username", username);
		loginMesssage.put("password", password);
		loginMesssage.put("used_key", user_key);
		loginMesssage.put("MAC", MAC_Address.mac_address);
		String url = "http://" + ServerInfo.serverIP + "/login/";
		JSONObject feedBack = HttpCommon.getHttpCommon()
				.getJsonResponseWithParams(url, loginMesssage);
		if (feedBack != null) {
			String result;
			try {
				result = feedBack.getString("result");
				if (result != null && result.equals("ok")) {
					boolean new_login = feedBack.getBoolean("new_login");
					String new_user_key = feedBack.getString("used_key");
					data = new New_Login(new_login,new_user_key);
					state = true;
				} else if (result != null && result.equals("error")) {
					message = feedBack.getString("msg");
				} else {
					message = "server is not available";
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				message = "server re-msg error";
				e.printStackTrace();
			}
		} else {
			message = "please check network";
		}

		return new CallResult(state, message,data);
	}

	public CallResult logout() {
		boolean state = false;
		String message = null;
		String url = "http://" + ServerInfo.serverIP + "/logout/";
		JSONObject feedBack = HttpCommon.getHttpCommon()
				.getJsonResponseWithParams(url, null);
		if (feedBack != null) {
			String result;
			try {
				result = feedBack.getString("result");

				if (result != null && result.equals("ok")) {
					state = true;
				} else if (result != null && result.equals("error")) {
					message = feedBack.getString("msg");
				} else {
					message = "server is not available";
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				message = "server re-msg error";
				e.printStackTrace();
			}
		} else {
			message = "please check network";
		}
		return new CallResult(state, message);
	}
}
