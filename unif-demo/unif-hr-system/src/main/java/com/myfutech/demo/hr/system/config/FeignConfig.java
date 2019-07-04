package com.myfutech.demo.hr.system.config;

import com.myfutech.demo.user.service.api.remote.RemoteEmployeeService;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients(basePackageClasses = RemoteEmployeeService.class)
@Configuration
public class FeignConfig {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
