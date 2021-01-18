package com.navino.jenkinshelper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.navino.jenkinshelper.mapper")
public class JenkinsHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(JenkinsHelperApplication.class, args);
    }

}
