package com.myfutech.common.spring.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sms.jiguang")
public class SmsProperties {

    private Boolean enabled = true;
    private String masterSecret;
    private String appKey;
}
