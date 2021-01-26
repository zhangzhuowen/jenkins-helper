package com.navino.jenkinshelper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//开启定时功能的注解
@EnableScheduling
@SpringBootApplication
@MapperScan("com.navino.jenkinshelper.mapper")
public class JenkinsHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(JenkinsHelperApplication.class, args);
    }

}
