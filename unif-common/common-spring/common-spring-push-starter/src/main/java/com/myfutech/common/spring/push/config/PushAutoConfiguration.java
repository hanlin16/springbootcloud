package com.myfutech.common.spring.push.config;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushClient;
import com.myfutech.common.spring.push.PushService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author kou
 * @date 2019-02-28
 */
@Configuration
@ConditionalOnClass(PushClient.class)
@EnableConfigurationProperties(PushProperties.class)
public class PushAutoConfiguration {

    @Resource
    private PushProperties pushProperties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "push.jiguang", value = "enabled", havingValue = "true")
    public PushService smsService(){
        ClientConfig config = ClientConfig.getInstance();
        config.setTimeToLive(pushProperties.getLivedTime());
        config.setReadTimeout(pushProperties.getReadTimeout());
        config.setConnectionTimeout(pushProperties.getConnectionTimeout());
        config.setMaxRetryTimes(pushProperties.getMaxRetryTimes());
        config.setApnsProduction(pushProperties.getApnsProduction());
        JPushClient jpushClient = new JPushClient(pushProperties.getMasterSecret(), pushProperties.getAppKey(), null, config);
        return new PushService(jpushClient);
    }
}
