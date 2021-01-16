package com.navino.jenkinshelper.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zhangZhuoWen
 * @ClassName LogIpConfig
 * @date 2021/1/16 20:43
 * @Description logback日志获取服务地址
 */
@Slf4j
@Configuration("logIpConfig")
public class LogIpConfig extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取Ip异常:", e);
        }
        return null;
    }
}
