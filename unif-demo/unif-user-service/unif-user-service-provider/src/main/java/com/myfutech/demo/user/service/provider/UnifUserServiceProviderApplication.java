package com.myfutech.demo.user.service.provider;

import com.myfutech.common.spring.advice.service.ServiceCommonErrorController;
import com.myfutech.common.spring.jpa.base.impl.BaseJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(repositoryBaseClass = BaseJpaRepositoryImpl.class)
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackageClasses = {UnifUserServiceProviderApplication.class, ServiceCommonErrorController.class})
public class UnifUserServiceProviderApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(UnifUserServiceProviderApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UnifUserServiceProviderApplication.class);
    }

}
