package com.xclenter.test.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public  class ServerInfo{
	public static String  serverIP;
	
    static{
    	Properties prop = new Properties();
    	try {
			prop.load(ServerInfo.class.getClassLoader().getResourceAsStream("server.properties"));
			
			serverIP = prop.getProperty("server.ip");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}