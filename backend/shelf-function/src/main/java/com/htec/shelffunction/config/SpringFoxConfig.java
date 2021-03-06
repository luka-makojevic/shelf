package com.htec.shelffunction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SpringFoxConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
                .addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.htec.shelffunction.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
                .apiInfo(apiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .version("1.0")
                .license("")
                .licenseUrl("")
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Shelf",
                "The purpose of this project is to build a simple cloud storage service. " +
                        "This document presents a description of the software called Shelf. " +
                        "It will explain the purpose and features of the software, the interfaces of the software, " +
                        "what the software will do and the constraints under which it must operate. " +
                        "This document is intended for users of the software and potential developers. ",
                "v1",
                "",
                new Contact("", "", ""),
                "", "", Collections.emptyList());
    }
}
