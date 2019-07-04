package com.myfutech.common.util.enums;

/**
 * 1XXX 客户端错误
 * 2XXX 服务器端内部错误
 * 3XXX 警告提示
 */
public enum ResponseCode {
    /**
     * 成功
     */
    SUCCESS_CODE("0000","成功"),
    /**
     * 错误
     */
    ERROR_CODE("0001","错误"),
    /**
     * 鉴权信息失效
     */
    AUTH_INVALID_CODE("0002", "鉴权信息失效"),
    /**
     * 异地登录，被挤出
     */
    SQUEEZZ_OUT_CODE("0003", "异地登录，被挤出"),
    /**
     *  3XXX 警告提示
     */
    WARN_CODE("3001", "此操作被禁止"),
    EXIST_CODE("3002","已存在"),
    OCCUPY_CODE("3003","已占用");

    private String code;
    private String defaultMsg;

    ResponseCode(String code, String defaultMsg) {
        this.code = code;
        this.defaultMsg = defaultMsg;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMsg() {
        return defaultMsg;
    }
}
