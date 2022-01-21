package com.htec.filesystem.config;

import com.htec.filesystem.validator.FileSystemValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public FileSystemValidator fileSystemValidator() {
        return new FileSystemValidator();
    }

}
