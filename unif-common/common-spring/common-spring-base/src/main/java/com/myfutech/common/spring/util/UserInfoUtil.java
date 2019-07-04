package com.myfutech.common.spring.util;


import com.myfutech.common.util.vo.auth.LoginUserVO;

import java.util.Collections;

/**
 * 服务内获取已登录的用户信息
 * 各系统中请使用 com.myfutech.common.spring.system.util.AuthUserUtils
 */
public class UserInfoUtil {

    public static final LoginUserVO DEFAULT_EMPTY = LoginUserVO.builder()
            .id(null).position("").employeeCode("").employeeId(null).realName("")
            .userCode("").dataPowerSectionIdSet(Collections.singleton(-999L)).build();

    private static final ThreadLocal<LoginUserVO> USER_INFO = new ThreadLocal<>();

    public static LoginUserVO getUserInfo() {
        return USER_INFO.get();
    }

    public static void init(LoginUserVO userInfo) {
        if (userInfo == null){
            userInfo = DEFAULT_EMPTY;
        }
        USER_INFO.set(userInfo);
    }

    public static void clean() {
        USER_INFO.remove();
    }
}
