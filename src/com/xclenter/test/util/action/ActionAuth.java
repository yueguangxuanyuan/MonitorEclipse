package com.xclenter.test.util.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.xclenter.test.util.saveFile.SaveFileUtil;

public class ActionAuth {
	private static String filePath = SaveFileUtil.utilFileSaveRootPath + File.separator + "user_key";
	
	private static String user_key;
	
	private static boolean LoginState = false;
	
	private static String Username = "unknown";
	
	
	private static final int readArraySizePerRead=64;
	
	public static void saveUser_key(String user_key){
		ActionAuth.user_key = user_key;
		File file = new File(filePath);
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			fw.write(user_key);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getUser_key(){
		if(user_key == null){
			try {
				File file = new File(filePath);
				if(file.exists()){
					InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
					char[] tempchars = new char[readArraySizePerRead];
					int charsReadCount = 0 ;
					StringBuilder sb = new StringBuilder();
					while((charsReadCount = isr.read(tempchars))!= -1){
						sb.append(tempchars, 0, charsReadCount);
					}
					user_key = sb.toString();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return user_key;
	}
	
	public static void changeLoginState(boolean state, String username){
		if(state){
			ActionAuth.Username = username;
		}else{
			ActionAuth.Username = "unknown";
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
	
