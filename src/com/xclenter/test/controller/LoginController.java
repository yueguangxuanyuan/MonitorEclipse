package com.xclenter.test.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.xclenter.test.util.HttpCommon;
import com.xclenter.test.util.LoginAuth;
import com.xclenter.test.util.ServerInfo;
import com.xclenter.test.util.saveFile.SaveFileUtil;

public class LoginController {
	
	private static LoginController loginDao;
	
	private LoginController(){
		
	}
	
	public static LoginController getLoginDao(){
		if(loginDao == null){
			loginDao = new LoginController();
		}
		return loginDao;
	}
	
	public boolean login(String username,String password){
		Map<String,String> loginMesssage = new HashMap<>();
		
		String user_key = LoginAuth.getUser_key();
		
		loginMesssage.put("username", username);
		loginMesssage.put("password", password);
		loginMesssage.put("used_key", user_key);
		
		String url = "http://"+ServerInfo.serverIP+"/login/";
		JSONObject feedBack = HttpCommon.getHttpCommon().getJsonResponseWithParams(url,loginMesssage);
		if(feedBack != null){
			String result = feedBack.getString("result");
			if(result != null && result.equals("ok")){
				String new_user_key =  feedBack.getString("used_key");
				if(user_key == null || !user_key.equals(new_user_key)){
					/*
					 * clear log environment;
					 */
					SaveFileUtil.clearLegacy();
					LoginAuth.saveUser_key(user_key);
				}
				
				LoginAuth.changeLoginState(true,username);
				return true;
			}
		}
		return false;
	}
}
