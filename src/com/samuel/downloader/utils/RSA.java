package com.samuel.downloader.utils;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Hex;



public class RSA{
/*
	public static String encrypt(byte[] textBytes, KeyPair pair,
			Cipher rsaCipher) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		// get the public key
		PublicKey pk = pair.getPublic();

		// Initialize the cipher for encryption. Use the public key.
		rsaCipher.init(Cipher.ENCRYPT_MODE, pk);

		// Perform the encryption using doFinal
		byte[] encByte = rsaCipher.doFinal(textBytes);

		// converts to base64 for easier display.
		byte[] base64Cipher = Base64.encodeBase64(encByte);

		return new String(base64Cipher);
	}// end encrypt

	public static String decrypt(byte[] cipherBytes, KeyPair pair,
			Cipher rsaCipher) throws IllegalBlockSizeException,
			BadPaddingException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException {
		// get the public key
		PrivateKey pvk = pair.getPrivate();

		// Create a Cipher object
		// Cipher rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding");

		// Initialize the cipher for encryption. Use the public key.
		rsaCipher.init(Cipher.DECRYPT_MODE, pvk);

		// Perform the encryption using doFinal
		byte[] decByte = rsaCipher.doFinal(cipherBytes);

		return new String(decByte);

	}// end decrypt

	
	*/


	public static void main(String args[]) throws Exception {
		RSA ed = new RSA();
		System.out.print(ed.decrypt(ed.encrypt("Hello")));
	}

	public String encrypt(String plaintext) throws Exception {
		Cipher cipher = null;
		PublicKey publicKey = null;
		try {

			KeyPairGenerator keygenerator = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keygenerator.initialize(1024, random);

			KeyPair keypair = keygenerator.generateKeyPair();
			// PrivateKey privateKey = keypair.getPrivate();
			publicKey = keypair.getPublic();
			cipher = Cipher.getInstance("RSA");
		} catch (Exception e) {
		}

		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		String st = "roseindia";
		byte[] cleartext = null;
		cleartext = st.getBytes("UTF-8");

		byte[] encrypted = blockCipherPublic(cleartext, Cipher.ENCRYPT_MODE);
		// return encrypted.toString();
		char[] encryptedTranspherable = Hex.encodeHex(encrypted);
		return new String(encryptedTranspherable);
	}

	// /////////////////////////////////////////////////////////////////////////////

	public String decrypt(String encrypted) throws Exception {
		Cipher cipher = null;
		PrivateKey privateKey = null;
		try {

			KeyPairGenerator keygenerator = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keygenerator.initialize(1024, random);

			KeyPair keypair = keygenerator.generateKeyPair();
			privateKey = keypair.getPrivate();
			// publicKey= keypair.getPublic();
			cipher = Cipher.getInstance("RSA");
		} catch (Exception e) {
		}

		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		byte[] bts = Hex.decodeHex(encrypted.toCharArray());

		byte[] decrypted = blockCipherPrivate(bts, Cipher.DECRYPT_MODE);

		return new String(decrypted, "UTF-8");
	}

	// //////////////////////////////////////////////////////////////////////////////////

	private byte[] blockCipherPublic(byte[] bytes, int mode)
			throws IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException {
		// string initialize 2 buffers.
		// scrambled will hold intermediate results
		Cipher cipher = Cipher.getInstance("RSA");
		PublicKey publicKey = null;
		try {

			KeyPairGenerator keygenerator = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keygenerator.initialize(1024, random);

			KeyPair keypair = keygenerator.generateKeyPair();
			// PrivateKey privateKey = keypair.getPrivate();
			publicKey = keypair.getPublic();

		} catch (Exception e) {
		}
		byte[] scrambled = new byte[0];

		// toReturn will hold the total result
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] toReturn = new byte[0];
		// if we encrypt we use 100 byte long blocks. Decryption requires 128
		// byte long blocks (because of RSA)
		int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;

		// another buffer. this one will hold the bytes that have to be modified
		// in this step
		byte[] buffer = new byte[length];

		for (int i = 0; i < bytes.length; i++) {

			// if we filled our buffer array we have our block ready for de- or
			// encryption
			if ((i > 0) && (i % length == 0)) {
				// execute the operation
				scrambled = cipher.doFinal(buffer);
				// add the result to our total result.
				toReturn = append(toReturn, scrambled);
				// here we calculate the length of the next buffer required
				int newlength = length;

				// if newlength would be longer than remaining bytes in the
				// bytes array we shorten it.
				if (i + length > bytes.length) {
					newlength = bytes.length - i;
				}
				// clean the buffer array
				buffer = new byte[newlength];
			}
			// copy byte into our buffer.
			buffer[i % length] = bytes[i];
		}
		scrambled = cipher.doFinal(buffer);

		// final step before we can return the modified data.
		toReturn = append(toReturn, scrambled);

		return toReturn;

	}

	// /////////////////////////////////////////////////////////////////////////////////////

	private byte[] blockCipherPrivate(byte[] bytes, int mode)
			throws IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException {
		// string initialize 2 buffers.
		// scrambled will hold intermediate results
		Cipher cipher = Cipher.getInstance("RSA");
		PrivateKey privateKey = null;
		try {

			KeyPairGenerator keygenerator = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keygenerator.initialize(1024, random);

			KeyPair keypair = keygenerator.generateKeyPair();
			privateKey = keypair.getPrivate();
			// publicKey= keypair.getPublic();

		} catch (Exception e) {
		}
		byte[] scrambled = new byte[0];

		// toReturn will hold the total result
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		byte[] toReturn = new byte[0];
		// if we encrypt we use 100 byte long blocks. Decryption requires 128
		// byte long blocks (because of RSA)
		int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;

		// another buffer. this one will hold the bytes that have to be modified
		// in this step
		byte[] buffer = new byte[length];

		for (int i = 0; i < bytes.length; i++) {

			// if we filled our buffer array we have our block ready for de- or
			// encryption
			if ((i > 0) && (i % length == 0)) {
				// execute the operation
				scrambled = cipher.doFinal(buffer);
				// add the result to our total result.
				toReturn = append(toReturn, scrambled);
				// here we calculate the length of the next buffer required
				int newlength = length;

				// if newlength would be longer than remaining bytes in the
				// bytes array we shorten it.
				if (i + length > bytes.length) {
					newlength = bytes.length - i;
				}
				// clean the buffer array
				buffer = new byte[newlength];
			}
			// copy byte into our buffer.
			buffer[i % length] = bytes[i];
		}
		scrambled = cipher.doFinal(buffer);

		// final step before we can return the modified data.
		toReturn = append(toReturn, scrambled);

		return toReturn;

	}

	// ///////////////////////////////////////////////////////////////////////////////////
	private byte[] append(byte[] prefix, byte[] suffix) {
		byte[] toReturn = new byte[prefix.length + suffix.length];
		for (int i = 0; i < prefix.length; i++) {
			toReturn[i] = prefix[i];
		}
		for (int i = 0; i < suffix.length; i++) {
			toReturn[i + prefix.length] = suffix[i];
		}
		return toReturn;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
}
