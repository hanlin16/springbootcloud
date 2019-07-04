package com.myfutech.common.util.enums;

/**
 * 系统从01开始；
 * 服务从001开始；
 */
public enum  SystemCode {
    /**
     *  门户系统
     */
    PORTAL_SYSTEM("01"),
    /**
     * 认证系统
     */
    AUTH_SYSTEM("02"),
    /**
     *  核心销售系统
     */
    MARKET_SYSTEM("03"),
    /**
     * 移动云平台
     */
    MOBILE_CLOUD_APP("04"),
    /**
     * OA 系统
     */
    OA_SYSTEM("05"),
    /**
     * HR 系统
     */
    HR_SYSTEM("06"),
    /**
     * 金管家 OA 系统
     */
    MANAGER_OA_SYSTEM("07"),

    /**
     * C端管理系统
     */
    C_USER_SYSTEM("09"),


    /**
     * 乾小云 APP
     */
    LITTLE_CLOUD_APP("30"),

    /**
    * C端用户APP
    *
    */
    C_USER_APP("50"),

    /**
     * C端用户微信公众号
     *
     */
    C_USER_WEIXIN("51"),

    ;
    private String code;

     SystemCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
