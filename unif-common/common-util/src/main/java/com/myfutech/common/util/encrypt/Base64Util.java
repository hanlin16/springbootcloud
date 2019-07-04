package com.myfutech.common.util.encrypt;

import java.util.Base64;

public class Base64Util {
	
	public static String encode(byte[] b) {
		if (b != null) {
			return Base64.getEncoder().encodeToString(b);
		}
		
		return null;
	}
	
	public static byte[] decode(String data) {
		if (data != null) {
			return Base64.getDecoder().decode(data);
		}
		return null;
	}
	
	public static byte[] decode(byte[] bytes) {
		if (bytes != null) {
			return Base64.getDecoder().decode(bytes);
		}
		return null;
	}

}
