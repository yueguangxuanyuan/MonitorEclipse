package com.xclenter.test.util.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
	/*
	 * "MD5"，"SHA1"，"SHA-256"，"SHA-384"，"SHA-512"
	 */
	public static String getHash(String fileName, String hashType) {
		InputStream fis;
		try {
			fis = new FileInputStream(fileName);
			byte buffer[] = new byte[1024];
			MessageDigest md5;
			md5 = MessageDigest.getInstance(hashType);
			for (int numRead = 0; (numRead = fis.read(buffer)) > 0;) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			/*
			 * byte数组 转化成 String
			 */
			byte[] b = md5.digest();
			String result = "";
			for (int i = 0; i < b.length; i++) {
				result += Integer.toString((b[i] & 0xff) + 0x100, 16)
						.substring(1);// 加0x100是因为有的b[i]的
			}
			return result;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "cannotCalculate";
	}
}
