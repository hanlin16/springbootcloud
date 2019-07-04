package com.myfutech.common.util.encrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil {  
  
	public static final String KEY_ALGORITHM = "RSA";
    public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
    public static final String PUBLIC_KEY = "PUBLIC_KEY";
    public static final String PRIVATE_KEY = "PRIVATE_KEY";

    /** RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 */
    public static final int KEY_SIZE = 1024;

    public static void main(String[] args) {
        Map<String, byte[]> keyMap = generateKeyBytes();

        System.out.println("RSA publicKey: " + Base64Util.encode(keyMap.get(PUBLIC_KEY)));
        System.out.println("RSA privateKey: " + Base64Util.encode(keyMap.get(PRIVATE_KEY)));

        byte[] encodedText = encode(Base64Util.decode(Base64Util.encode(keyMap.get(PUBLIC_KEY))), "test".getBytes());
        System.out.println("RSA encoded: " + Base64Util.encode(encodedText));

        // 解密
        PrivateKey privateKey = restorePrivateKey(Base64Util.decode(Base64Util.encode(keyMap.get(PRIVATE_KEY))));
        System.out.println("RSA decoded: "
                + decode(privateKey, encodedText));
//        String password = "t32t1MuQXMoA7q3Fm72s5gpSpihejclBtBQ1xZPJ7PH+cDhGZkJl/XiY9EqFN17en/gRKcJ94iigkpjj2eQkncedGSHLEBZxNrXPZuqeE7VbZQ2bjxv7F/QC2syL38cOedWECw3xK+YI+I+dUwQa7AMVDYgH7RI00LrSp8mme+U=";
//        String privateKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDNjj7+YBm9zpF58hWl27Y3iMN743jsLe2ukI4wAaoc13LXmQpW5JUP7djuPXDSXWgBIYJCPSJzbB2st1w8X8lftZr2+t0DhOvkFSEZCUfHatHFocCYdApw7IHftJ3sgzLNzf2S3jzUwuQRbiFiK9S2P5EdLroqVXU2djGIvYJppwIDAQAB";
//        String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDNjj7+YBm9zpF58hWl27Y3iMN743jsLe2ukI4wAaoc13LXmQpW5JUP7djuPXDSXWgBIYJCPSJzbB2st1w8X8lftZr2+t0DhOvkFSEZCUfHatHFocCYdApw7IHftJ3sgzLNzf2S3jzUwuQRbiFiK9S2P5EdLroqVXU2djGIvYJppwIDAQAB";
//        PublicKey publicKey = restorePublicKey(Base64Util.decode(publicKeyString));
//        byte[] encodedText = encode(publicKey, "123456".getBytes());
//        System.out.println("RSA encoded: " + Base64Util.encode(encodedText));
//
//        PrivateKey privateKey = restorePrivateKey(Base64Util.decode(privateKeyString));
//        String encodedText1 = decode(privateKey, password.getBytes());
//        System.out.println("RSA encoded: " + encodedText1);
    }

    /**
     * 生成密钥对。注意这里是生成密钥对KeyPair，再由密钥对获取公私钥
     */
    public static Map<String, byte[]> generateKeyBytes() {

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            Map<String, byte[]> keyMap = new HashMap<String, byte[]>();
            keyMap.put(PUBLIC_KEY, publicKey.getEncoded());
            keyMap.put(PRIVATE_KEY, privateKey.getEncoded());
            return keyMap;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成密钥对异常", e);
        }
    }
    
    /**
     * 生成密钥对。注意这里是生成密钥对KeyPair
     */
    public static Map<String, Object> generateKeyBytes2() {

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            Map<String, Object> keyMap = new HashMap<String, Object>();
            keyMap.put(PUBLIC_KEY, publicKey.getEncoded());
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException("生成密钥对异常", e);
        }
    }

    /**
     * 还原公钥，X509EncodedKeySpec 用于构建公钥的规范
     */
    public static PublicKey restorePublicKey(byte[] keyBytes) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 还原私钥，PKCS8EncodedKeySpec 用于构建私钥的规范
     */
    public static PrivateKey restorePrivateKey(byte[] keyBytes) {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = factory
                    .generatePrivate(pkcs8EncodedKeySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 加密
     */
    public static byte[] encode(byte[] key, byte[] plainText) {
    	return encode(restorePublicKey(key), plainText);
    }

    /**
     * 加密
     */
    public static byte[] encode(PublicKey key, byte[] plainText) {

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(plainText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;

    }
    
    /**
     * 解密
     */
    public static String decode(byte[] key, byte[] encodedText) {
    	return decode(restorePrivateKey(key), encodedText);
    }

    /**
     * 解密
     */
    public static String decode(PrivateKey key, byte[] encodedText) {

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(encodedText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            throw new RuntimeException("数据解密异常，加密方式不匹配", e);
        }
    }
}  