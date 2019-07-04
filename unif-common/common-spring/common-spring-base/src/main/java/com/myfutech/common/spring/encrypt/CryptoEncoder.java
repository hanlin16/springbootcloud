package com.myfutech.common.spring.encrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Bcrypt 密码加密
 */
public class CryptoEncoder {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 加密
     */
    public static String encode(String password){

        if (password == null){
            throw new IllegalArgumentException("password cannot be null");
        }
        return PASSWORD_ENCODER.encode(password);
    }

    /**
     * 校验密码是否正确
     */
    public static boolean matches(String password, String encodePassword){
        if (password == null){
            throw new IllegalArgumentException("password cannot be null");
        }
        if (encodePassword == null){
            throw new IllegalArgumentException("encodePassword cannot be null");
        }
        return PASSWORD_ENCODER.matches(password, encodePassword);
    }

    public static void main(String[] args) {
        String code = "$2a$10$dYRcFip80f0jIKGzRGulFelK12036xWQKgajanfxT65QB4htsEXNK";
        System.out.println("code = " + matches("12345", code));
        String pas =  encode("oa_form_secret");
        System.out.println("pas = " + pas);
    }
}
