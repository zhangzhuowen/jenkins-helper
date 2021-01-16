package com.navino.jenkinshelper.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangZhuoWen
 * @ClassName HelloController
 * @date 2021/1/16 0:42
 * @Description TODO
 */
@Slf4j
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        log.info("进入了hello");
        return "hello";
    }
}
