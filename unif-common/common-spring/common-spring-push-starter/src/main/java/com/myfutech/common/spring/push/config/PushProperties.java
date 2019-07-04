package com.myfutech.common.spring.push.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "push.jiguang")
public class PushProperties {

    private Boolean enabled = true;
    private String masterSecret;
    private String appKey;
    private Integer livedTime = 86400;
    private Integer maxRetryTimes = 5;
    private Integer connectionTimeout = 5000;
    private Integer readTimeout = 30_000;
    private Boolean apnsProduction = true;
}
