package com.samuel.downloader.utils;
/*
 * DesEncrypt.java
 * 
 * Created on 2007-9-20, 16:10:47
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

//思路： 因为   任意一个字符串，都是由若干字节表示的，每个字节实质就是一个
//              有8位的进进制数，
//      又因为   一个8位二进制数，可用两位16进制字符串表示.
//        因此   任意一个字符串可以由两位16进制字符串表示。
//          而   DES是对8位二进制数进行加密，解密。
//        所以   用DES加密解密时，可以把加密所得的8位进进制数，转成
//               两位16进制数进行保存，传输。
//    具体方法：1 把一个字符串转成8位二进制数，用DES加密，得到8位二进制数的
//                密文
//              2 然后把（由1）所得的密文转成两位十六进制字符串
//              3 解密时，把（由2)所得的两位十六进制字符串，转换成8位二进制
//                数的密文
//              4 把子3所得的密文，用DES进行解密，得到8位二进制数形式的明文，
//                并强制转换成字符串。
// 思考：为什么要通过两位16进制数字符串保存密文呢？
//       原因是：一个字符串加密后所得的8位二进制数，通常不再时字符串了，如果
//              直接把这种密文所得的8位二进制数强制转成字符串，有许多信息因为异
//              常而丢失，导制解密失败。因制要把这个8位二制数，直接以数的形式
//              保存下来，而通常是用两位十六进制数表示。

import android.annotation.SuppressLint;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

/**
 * 
 * 使用DES加密与解密,可对byte[],String类型进行加密与解密 密文可使用String,byte[]存储.
 * 
 * 方法: void getKey(String strKey)从strKey的字条生成一个Key
 * 
 * String getEncString(String strMing)对strMing进行加密,返回String密文 String
 * getDesString(String strMi)对strMin进行解密,返回String明文
 * 
 * byte[] getEncCode(byte[] byteS)byte[]型的加密 byte[] getDesCode(byte[]
 * byteD)byte[]型的解密
 */

@SuppressLint("DefaultLocale")
public class DesEncrypt {

	private static final String OPER_ERROR = "ERROR";
	private static final String APPMARKET_SECURITY_KEY = "Foxconn88@2014";

	/**
	 * 根据参数生成KEY
	 * 
	 * @param strKey
	 */
	public static Key getKey(String strKey) {

		KeyGenerator generator = null;
		try {
			generator = KeyGenerator.getInstance("DES");
			generator.init(new SecureRandom(strKey.getBytes()));
			return generator.generateKey();
		} catch (Exception e) {
//			e.printStackTrace();
			return null;
		} finally {
			generator = null;
		}
	}

	/**
	 * 加密以byte[]明文输入,byte[]密文输出
	 * 
	 * @param byteS
	 * @return
	 */
	private static byte[] getEncCode(byte[] byteS, Key key) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 解密以byte[]密文输入,以byte[]明文输出
	 * 
	 * @param byteD
	 * @return
	 */
	private static byte[] getDesCode(byte[] byteD, Key pairKey) {

		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, pairKey);
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
//			e.printStackTrace();
			return OPER_ERROR.getBytes();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 二行制转字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) { // 一个字节的数，
		// 转成16进制字符串
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			// 整数转成十六进制表示
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase(); // 转成大写
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}

		return b2;
	}

	
	
	/**
	 * 获得加密的字符串  ,明文输入,密文输出.
	 * 
	 * @param pairKey   密钥  
	 * @param encString  要加密的字符串 明文 不能为null或者""
	 * @return  加密后的密文
	 */
	public static String getEncString(String pairKey, String encString) {
		
		if(encString==null||encString.length()==0){
			return null;
		}
		Key key = DesEncrypt.getKey(pairKey);
		try {
			return byte2hex(getEncCode(encString.getBytes(), key));
		} catch (Exception e) {
//			e.printStackTrace();
			return OPER_ERROR;
		}
	}

	/**
	 * 获得解密的字符串, 密文输入,明文输出
	 * 
	 * @param pairKey 密钥 
	 * @param decString  密文  密文不能为null或者""
	 * @return  解密后的密文, 如果密钥不正确返回null 
	 */
	public static String getDecString(String pairKey, String decString) {

		if(decString==null||decString.length()==0){
			return null;
		}
		Key key = DesEncrypt.getKey(pairKey);

		try{
			return new String(getDesCode(hex2byte(decString.getBytes()), key));
		}catch(Exception e){
//			e.printStackTrace();
			return OPER_ERROR;
		}
		
	}

	public static void main(String[] args) {

		String strEnc = DesEncrypt.getEncString(APPMARKET_SECURITY_KEY," 开着拖拉机结婚 ,oracle.jdbc.driver.OracleDriver, ADFAF:ASDFADSF,'ADF':'ADSF','ADSF':123,\"3213\":\"256\" ");// 加密字符串,返回String的密文
		
		System.out.println(strEnc);

		String strDes = DesEncrypt.getDecString(APPMARKET_SECURITY_KEY, strEnc);// 把String
																// 类型的密文解密
		System.out.println(strDes);
	}

}