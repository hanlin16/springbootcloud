package com.myfutech.common.spring.sms.config;

import cn.jsms.api.common.SMSClient;
import com.myfutech.common.spring.sms.SmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author kou
 * @date 2019-02-27
 */
@Configuration
@ConditionalOnClass(SMSClient.class)
@EnableConfigurationProperties(SmsProperties.class)
public class SmsAutoConfiguration {

    @Resource
    private SmsProperties smsProperties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "sms.jiguang", value = "enabled", havingValue = "true")
    public SmsService smsService(){
        return new SmsService(new SMSClient(smsProperties.getMasterSecret(), smsProperties.getAppKey()));
    }
}
