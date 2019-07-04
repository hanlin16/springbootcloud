package com.myfutech.demo.hr.system;

import com.myfutech.common.spring.advice.system.SystemCommonErrorController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication(scanBasePackageClasses = {SystemCommonErrorController.class,
        UnifHrSystemApplication.class})
public class UnifHrSystemApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(UnifHrSystemApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UnifHrSystemApplication.class);
    }
}
