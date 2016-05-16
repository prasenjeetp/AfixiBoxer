package com.boxer.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class HashGenerator {
	static String INITIALIZATIO_VECTOR = "AODVNUASDNVVAOVF";

	public static String encrypt(String input, String key) {
		byte[] crypted = null;
		if (input == null || input == "") {
			input = "not avilable";
		}
			try {
				key = key.substring(0, 16);
				SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, skey);
				crypted = cipher.doFinal(input.getBytes());
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		
		return Base64.encodeToString(crypted, Base64.DEFAULT);
	}

	// public static String encryptNew(String plainText, String encryptionKey) {
	// byte[] crypted = null;
	// byte[] cleartext;
	// Cipher cipher;
	// String encryptedPwd = "";
	// try {
	// cleartext = plainText.getBytes("UTF8");
	// DESKeySpec keySpec = new DESKeySpec(encryptionKey.getBytes("UTF8"));
	// SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	// SecretKey key = keyFactory.generateSecret(keySpec);
	//
	// cipher = Cipher.getInstance("DES"); // cipher is not thread safe
	// cipher.init(Cipher.ENCRYPT_MODE, key);
	// encryptedPwd = Base64.encodeToString(cipher.doFinal(cleartext),
	// Base64.DEFAULT);
	//
	// // Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	// //
	// // SecretKeySpec key = new SecretKeySpec(
	// // encryptionKey.getBytes("UTF-8"), "AES");
	// //
	// // cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(
	// // INITIALIZATIO_VECTOR.getBytes("UTF-8")));
	// // crypted = cipher.doFinal(plainText.getBytes("UTF-8"));
	//
	// } catch (Exception e) {
	// System.out.println(e.toString());
	// }
	// return encryptedPwd;
	// }

	public static String decrypt(String plainText, String encryptionKey) {

		byte[] crypted = null;
		Cipher cipher;
		byte[] cleartext;
		try {
			cleartext = plainText.getBytes("UTF8");
			DESKeySpec keySpec = new DESKeySpec(encryptionKey.getBytes("UTF8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);

			cipher = Cipher.getInstance("DES");// cipher is not thread safe
			cipher.init(Cipher.DECRYPT_MODE, key);

			// cipher is not thread safe

			crypted = Base64.decode(cipher.doFinal(cleartext), Base64.DEFAULT);

			// Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			//
			// SecretKeySpec key = new SecretKeySpec(
			// encryptionKey.getBytes("UTF-8"), "AES");
			//
			// cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(
			// INITIALIZATIO_VECTOR.getBytes("UTF-8")));
			// crypted = cipher.doFinal(plainText.getBytes("UTF-8"));

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new String(crypted);

	}
}
