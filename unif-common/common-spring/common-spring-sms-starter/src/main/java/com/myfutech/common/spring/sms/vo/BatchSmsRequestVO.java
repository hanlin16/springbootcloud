package com.myfutech.common.spring.sms.vo;

import lombok.Data;

import java.util.Map;

@Data
public class BatchSmsRequestVO {

    /**
     * 手机号
     */
    private String mobile;
    /**
     * 模版参数
     */
    private Map<String, String> templateParams;
}
