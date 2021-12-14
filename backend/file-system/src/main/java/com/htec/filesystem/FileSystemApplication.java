package com.htec.filesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class FileSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileSystemApplication.class, args);
    }
}