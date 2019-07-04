package com.myfutech.demo.hr.system.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
//@Profile({"local", "test"})
@EnableSwagger2
public class Swagger2 {
	
	private static final Logger log = LoggerFactory.getLogger(Swagger2.class);

    @Bean
    public Docket createRestApi() {
    	 log.info("API document is started, path is [/swagger-ui.html]...");
    	 
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.myfutech.demo.hr.system.ctrl"))
                .paths(PathSelectors.any())
                .build();
       
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("HR 系统 APIs")
                .description("HR 系统 接口说明")
                .version("1.0")
                .build();
    }
}
