package com.myfutech.common.spring.system.util;

import com.myfutech.common.util.exception.AuthException;
import com.myfutech.common.util.vo.auth.UserInfo;

/**
 *   登录用户信息工具类
 *
 *   如果想要增加其他信息。
 *   1、在{@link com.myfutech.common.util.vo.auth.UserInfo} 中加入想要的属性；
 *   2、在认证系统项目unif-system-auth中的com.myfutech.auth.service.UserDetailsServiceImpl#loadUserByUsername方法注入值；
 *   3、在此类中写对应的获取方法，getPrincipal方法返回的map包含UserInfo的属性key，和对应值value；
 */
public class AuthUserUtils {

    private static final ThreadLocal<UserInfo> USER_INFO = new ThreadLocal<>();

    public static UserInfo getUserInfo() {
        return USER_INFO.get();
    }

    public static void init(UserInfo userInfo) {
        if (userInfo == null){
            throw new AuthException("没有找到用户信息");
        }
        USER_INFO.set(userInfo);
    }

    public static void clean() {
        USER_INFO.remove();
    }
}
