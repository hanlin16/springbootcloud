package com.myfutech.common.util.encrypt;

import org.apache.commons.lang3.StringUtils;

/**
 * @program: unif-common
 * @description: 脱敏类
 * @author: LiDong
 * @create: 2019-01-11 14:48
 **/
public class DesensitizationUtil {
    private static String mobiles = "(\\d{3})\\d{4}(\\d{4})";
    private static String encrypt = "(?<=\\w{3})\\w(?=\\w{4})";

    // 手机号码前三后四脱敏
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll(mobiles, "$1****$2");
    }

    //身份证前三后四脱敏
    public static String idEncrypt(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.replaceAll(encrypt, "*");
    }

    //护照前2后3位脱敏，护照一般为8或9位
    public static String idPassport(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.substring(0, 2) + new String(new char[id.length() - 5]).replace("\0", "*") + id.substring(id.length() - 3);
    }
}
