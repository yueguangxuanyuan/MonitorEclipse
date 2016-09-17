package com.xclenter.test.util.file;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadFileUtil {
	public static byte[] readFileInBytes(String filePath) {
		final int readArraySizePerRead = 4096;
		File file = new File(filePath);
		ArrayList<Byte> bytes = new ArrayList<>();
		try {
			if (file.exists()) {
				DataInputStream isr = new DataInputStream(new FileInputStream(
						file));
				byte[] tempchars = new byte[readArraySizePerRead];
				int charsReadCount = 0;

				while ((charsReadCount = isr.read(tempchars)) != -1) {
					for(int i = 0 ; i < charsReadCount ; i++){
						bytes.add (tempchars[i]);
					}
				}
				isr.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return toPrimitives(bytes.toArray(new Byte[0]));
	}
	static byte[] toPrimitives(Byte[] oBytes) {
		byte[] bytes = new byte[oBytes.length];

		for (int i = 0; i < oBytes.length; i++) {
			bytes[i] = oBytes[i];
		}

		return bytes;
	}
	
	public static String readFileInString(String filePath){
		String content = null;
	    int readArraySizePerRead=128;
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
				content = sb.toString();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
}
