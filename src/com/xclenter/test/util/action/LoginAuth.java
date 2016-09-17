package com.xclenter.test.util.action;

import java.io.File;

import com.xclenter.test.util.file.FileUtil;
import com.xclenter.test.util.file.ReadFileUtil;
import com.xclenter.test.util.file.SaveFileUtil;

public class LoginAuth {
	private static String filePath = FileUtil.utilFileSaveRootPath + File.separator + "user_key";
	
	private static String user_key;
	
	private static boolean LoginState = false;
	
	private static String Username = "unknown";
	
	public static void saveUser_key(String user_key){
		LoginAuth.user_key = user_key;
		SaveFileUtil.saveFileWithString(filePath, user_key);
	}
	
	public static String getUser_key(){
		if(user_key == null){
			user_key = ReadFileUtil.readFileInString(filePath);
		}
		
		return user_key;
	}
	
	public static void changeLoginState(boolean state, String username){
		if(state){
			LoginAuth.Username = username;
		}else{
			LoginAuth.Username = "unknown";
		}
		
		LoginState = state;
		System.setProperty("em_isLogin", state+"");
	}
	
	public static boolean isLogin(){
		return LoginState;
	}
	
	public static String getUsername(){
		return Username;
	}
	
}   
	
