package com.myfutech.demo.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class UnifEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnifEurekaApplication.class, args);
    }
}
