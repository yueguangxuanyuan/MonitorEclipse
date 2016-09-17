package com.xclenter.test.util.file;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.keyczar.Crypter;
import org.keyczar.exceptions.KeyczarException;
import org.osgi.framework.Bundle;

import com.xclenter.test.Activator;

public class EncryptUtil {
	private static EncryptUtil encryptUtil;

	private Crypter crypter;

	private EncryptUtil() {
		try {
			Bundle bundle = Activator.getDefault().getBundle();    
			URL url = bundle.getResource("/encrypt_key");  
			String fileURL = FileLocator.toFileURL(url).getPath();
			crypter = new Crypter(fileURL);
		} catch (KeyczarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static EncryptUtil getEncryptUtil() {
		if (encryptUtil == null) {
			encryptUtil = new EncryptUtil();
		}

		return encryptUtil;
	}

	public String encryptString(String input) {
		String encrptedString = null;

		if (crypter != null && input != null) {
			try {
				encrptedString = crypter.encrypt(input);
			} catch (KeyczarException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return encrptedString;
	}
	
	public byte[] encryptString(byte[] input) {
		byte[] encrptedString = null;

		if (crypter != null && input != null) {
			try {
				encrptedString = crypter.encrypt(input);
			} catch (KeyczarException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return encrptedString;
	}
	
	public String decryptString(String input) {
		String decryptedString = null;
		if (crypter != null && input != null) {
			try {
				decryptedString = crypter.decrypt(input);
			} catch (KeyczarException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return decryptedString;
	}
	
	public byte[] decryptString(byte[] input) {
		byte[] decryptedString = null;
		if (crypter != null && input != null) {
			try {
				decryptedString = crypter.decrypt(input);
			} catch (KeyczarException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return decryptedString;
	}
}
