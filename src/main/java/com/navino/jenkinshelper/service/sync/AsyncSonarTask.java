package com.navino.jenkinshelper.service.sync;

import com.navino.jenkinshelper.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author zhangZhuoWen
 * @ClassName AsyncService
 * @date 2021/1/18 11:30
 * @Description TODO
 */
@Slf4j
@Component
public class AsyncSonarTask {

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    // 指定使用bean为sonarExecutor的线程池
    @Async("sonarExecutor")
    public void executeAsync(String buildUrl, String lastBuilUrl, String jenkinsName) throws Exception {
        log.info("开始构建项目: " + jenkinsName);

        //1.发送请求开始打包
        //String url ="http://10.130.10.45:7878/view/service_code_check/job/navi_commons_master/build?delay=0sec";
        log.info("buildUrl : " + buildUrl);
        ResponseEntity postResponse = HttpUtils.sendPostRequest(buildUrl, null);

        HttpStatus statusCode = postResponse.getStatusCode();
        if (!statusCode.equals(HttpStatus.CREATED)) {
            throw new Exception("build package fail!");
        }

        //2.由于Jenkins打包时间较长先睡3分钟
        Thread.sleep(180000);

        //3.开始每隔20秒轮询发送请求判断是否打包成功
        //http://10.130.10.45:7878/view/service_code_check/job/service_biz_common_master/lastBuild/
        log.info(lastBuilUrl);
        int lastBuildCount = 1;
        while (true) {
            lastBuildCount++;
            ResponseEntity<String> getResponse = HttpUtils.sendGetRequest(lastBuilUrl);
            HttpStatus getStatusCode = getResponse.getStatusCode();
            if (!getStatusCode.equals(HttpStatus.OK)) {
                break;
            }
            String body = getResponse.getBody();

            if (StringUtils.isEmpty(body)) {
                break;
            }

            //打包成功
            if (body.contains("alt=\"Success\" tooltip=\"Success\"")) {
                break;
            }
            //打包失败
            //TODO
            else if (body.contains("alt=\"Failed\" tooltip=\"Failed\"")) {
                log.info("项目: " + jenkinsName + ",build接口执行失败，重新执行");
                //
            }
            //正在打包中
            else if (body.contains("class=\"build-caption-progress-bar\"")) {
                Thread.sleep(20000);
                //防止死循环
                if (lastBuildCount >= 50) {
                    log.info("projectName : " + jenkinsName + ",请求lastBuild/长时间未完成！请检查！");
                    break;
                }
            } else {
                log.info("项目: " + jenkinsName + "未知返回结果！");
                break;
            }
        }
        log.info("项目: " + jenkinsName + "执行结束！");

    }
}
