package com.xclenter.test.util.file;

import it.sauronsoftware.base64.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {
	private static EncryptUtil encryptUtil;
	/**
	 * 密钥算法
	 */
	private static final String KEY_ALGORITHM = "AES";

	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
	
	private static String key = "hYOTz5Il8IzWQSVk";

	private EncryptUtil() {
	}

	public static EncryptUtil getEncryptUtil() {
		if (encryptUtil == null) {
			encryptUtil = new EncryptUtil();
		}

		return encryptUtil;
	}

	
	public byte[] decrypt(String input) {
		try {
			byte[] inputstream = input.getBytes("ISO-8859-1");
			inputstream = Base64.decode(inputstream);
			byte[] iv = Arrays.copyOfRange(inputstream, 0, 16);

			byte[] data = Arrays.copyOfRange(inputstream, 16,
					inputstream.length);
			return decrypt(data, key.getBytes("ISO-8859-1"), iv);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Key toKey(byte[] key) {
		// 生成密钥
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}

	public byte[] decrypt(byte[] data, byte[] key, byte[] iv) {
		// 实例化
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			// 使用密钥初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, toKey(key),
					new IvParameterSpec(iv));
			// 执行操作
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new byte[0];
	}
}
