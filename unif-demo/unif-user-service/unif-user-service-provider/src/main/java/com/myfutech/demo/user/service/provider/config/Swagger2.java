package com.myfutech.demo.user.service.provider.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .apis(RequestHandlerSelectors.basePackage("com.myfutech.demo.user.service.provider.ctrl"))
                .paths(PathSelectors.any())
                .build();
       
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("用户服务 APIs")
                .description("用户服务接口说明")
                .version("1.0")
                .build();
    }
}
