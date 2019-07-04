package com.myfutech.common.spring.sms.vo;

import lombok.Data;

@Data
public class FailSmsResponseVO {

    /**
     * 手机号
     */
    private String mobile;
    /**
     * 失败编码
     */
    private String errorCode;
    /**
     * 失败信息
     */
    private String errorMessage;

    public FailSmsResponseVO() {
    }

    public FailSmsResponseVO(String mobile, String errorCode, String errorMessage) {
        this.mobile = mobile;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
