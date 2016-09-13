package com.xclenter.test.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.xclenter.test.util.HttpCommon;
import com.xclenter.test.util.ServerInfo;
import com.xclenter.test.util.action.ActionAuth;
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
	
	public CallResult login(String username,String password){
		boolean state=false;
		String message = null;
		Map<String,String> loginMesssage = new HashMap<>();
		
		String user_key = ActionAuth.getUser_key();
		
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
					SaveFileUtil.clearLegacy();
					ActionAuth.saveUser_key(user_key);
					message = "continue";
				}
				
				ActionAuth.changeLoginState(true,username);
				state = true;
			}else if(result != null && result.equals("error")){
				message = feedBack.getString("msg");
			}else{
				message = "server is not available";
			}
		}else{
			message = "please check network";
		}
		
		return new CallResult(state,message);
	}
	
	public CallResult logout(){
		boolean state=false;
		String message = null;
		String url = "http://"+ServerInfo.serverIP+"/logout/";
		JSONObject feedBack = HttpCommon.getHttpCommon().getJsonResponseWithParams(url,null);
		if(feedBack != null){
			String result = feedBack.getString("result");
			if(result != null && result.equals("ok")){
				ActionAuth.changeLoginState(false,"");
				state = true;
			}else if(result != null && result.equals("error")){
				message = feedBack.getString("msg");
			}else{
				message = "server is not available";
			}
		}else{
			message = "please check network";
		}
		return new CallResult(state,message);
	}
}
