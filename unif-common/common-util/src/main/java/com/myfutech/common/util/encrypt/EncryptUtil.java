package com.myfutech.common.util.encrypt;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class EncryptUtil {
	private static final String KEY_SHA = "SHA";
    private static final String KEY_MD5 = "MD5";
    private static final char[] HEX_CODE = "0123456789ABCDEF".toCharArray();
    /**
     * MAC算法可选以下多种算法 
     *  
     * <pre> 
     * HmacMD5  
     * HmacSHA1  
     * HmacSHA256  
     * HmacSHA384  
     * HmacSHA512 
     * </pre> 
     */
    private static final String KEY_MAC = "HmacSHA1";
  
    /** 
     * MD5加密 
     *  
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {  
  
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);  
        md5.update(data);  
  
        return md5.digest();  
  
    }  
  
    /** 
     * SHA加密 
     *  
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {  
  
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);  
        sha.update(data);  
  
        return sha.digest();  
  
    }  
  
    /** 
     * HMAC加密 
     *  
     */
    public static String encryptHMAC(String data, String key) {  
        SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), KEY_MAC);  
        Mac mac;
		try {
			mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);  
		} catch (Exception e) {
			throw new RuntimeException("encrypt error", e);
		}  
		byte[] result = mac.doFinal(data.getBytes(StandardCharsets.UTF_8)); 
		return printHexBinary(result);
    }  
    
    public static String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(HEX_CODE[(b >> 4) & 0xF]);
            r.append(HEX_CODE[(b & 0xF)]);
        }
        return r.toString();
    }
    
    public static void main(String[] args) {
    	String msg = encryptHMAC("123456", "koukq");
    	System.out.println(msg);
	}
}
