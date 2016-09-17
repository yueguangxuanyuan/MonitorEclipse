package com.xclenter.test.util.file;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

public class SaveFileUtil {

	public static void saveFile(String saveFilePath,InputStream inputStream){
		final int writeSize = 4096;
		try {
			File file = new File(saveFilePath);
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream outputStream = new FileOutputStream(file);
			
			byte[] buff = new byte[4096];
			int counts = 0;
			while((counts = inputStream.read(buff)) != -1){
				outputStream.write(buff,0,counts);
			}
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void saveFileWithByte(String saveFilePath,byte[] content){
		try {
			File file = new File(saveFilePath);
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			DataOutputStream fw = new DataOutputStream(new FileOutputStream(file));
			fw.write(content);
			fw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void saveFileWithString(String saveFilePath,String content){
		try {
			File file = new File(saveFilePath);
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			fw.write(content);
			fw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
